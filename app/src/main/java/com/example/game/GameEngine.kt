package com.example.game

import com.example.data.HeroCatalog
import com.example.data.ItemCatalog
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.*

class GameEngine(
    val playerHero: Hero,
    val mode: GameMode,
    private val soundAndHaptic: SoundAndHaptic? = null
) {
    private val _entities = MutableStateFlow<List<CombatEntity>>(emptyList())
    val entities: StateFlow<List<CombatEntity>> = _entities.asStateFlow()

    private val _towers = MutableStateFlow<List<Tower>>(emptyList())
    val towers: StateFlow<List<Tower>> = _towers.asStateFlow()

    private val _minions = MutableStateFlow<List<Minion>>(emptyList())
    val minions: StateFlow<List<Minion>> = _minions.asStateFlow()

    private val _jungleMonsters = MutableStateFlow<List<JungleMonster>>(emptyList())
    val jungleMonsters: StateFlow<List<JungleMonster>> = _jungleMonsters.asStateFlow()

    private val _projectiles = MutableStateFlow<List<Projectile>>(emptyList())
    val projectiles: StateFlow<List<Projectile>> = _projectiles.asStateFlow()

    private val _floatingTexts = MutableStateFlow<List<FloatingText>>(emptyList())
    val floatingTexts: StateFlow<List<FloatingText>> = _floatingTexts.asStateFlow()

    private val _killAnnouncements = MutableStateFlow<List<KillAnnouncement>>(emptyList())
    val killAnnouncements: StateFlow<List<KillAnnouncement>> = _killAnnouncements.asStateFlow()

    private val _gameTimeSeconds = MutableStateFlow(0f)
    val gameTimeSeconds: StateFlow<Float> = _gameTimeSeconds.asStateFlow()

    private val _blueScore = MutableStateFlow(0)
    val blueScore: StateFlow<Int> = _blueScore.asStateFlow()

    private val _redScore = MutableStateFlow(0)
    val redScore: StateFlow<Int> = _redScore.asStateFlow()

    private val _isGameOver = MutableStateFlow(false)
    val isGameOver: StateFlow<Boolean> = _isGameOver.asStateFlow()

    private val _isVictory = MutableStateFlow(false)
    val isVictory: StateFlow<Boolean> = _isVictory.asStateFlow()

    // Practice mode toggles
    var practiceGodMode: Boolean = false
    var practiceNoCooldown: Boolean = false

    private var playerRecentKills = 0
    private var lastKillTime = 0f
    private var minionSpawnTimer = 0f
    private var goldPassiveTimer = 0f

    // Current player joystick vector
    private var joystickX = 0f
    private var joystickY = 0f

    init {
        setupMatch()
    }

    private fun setupMatch() {
        val entityList = mutableListOf<CombatEntity>()

        // 1. Create Player
        val player = CombatEntity(
            id = "player_hero",
            name = playerHero.name,
            hero = playerHero,
            team = Team.BLUE,
            x = 120f,
            y = 120f,
            isAi = false,
            lane = if (mode == GameMode.ONE_VS_ONE) Lane.MID else Lane.BOT,
            skill3Cd = if (mode == GameMode.PRACTICE) 0f else playerHero.skill3.cooldown,
            skill3Level = if (mode == GameMode.PRACTICE) 1 else 0
        )
        entityList.add(player)

        // Select other heroes from catalog
        val availableHeroes = HeroCatalog.heroes.filter { it.id != playerHero.id }.shuffled()
        var heroIdx = 0

        if (mode == GameMode.FIVE_VS_FIVE) {
            // 4 Allies for Blue Team
            val allyRoles = listOf(HeroRole.DAU_SI, HeroRole.PHAP_SU, HeroRole.SAT_THU, HeroRole.TRO_THU)
            val allyLanes = listOf(Lane.TOP, Lane.MID, Lane.JUNGLE, Lane.BOT)

            for (i in 0 until 4) {
                val hero = availableHeroes.getOrNull(heroIdx++) ?: HeroCatalog.heroes[i]
                entityList.add(
                    CombatEntity(
                        id = "ally_$i",
                        name = hero.name,
                        hero = hero,
                        team = Team.BLUE,
                        x = 110f + i * 20f,
                        y = 110f + i * 15f,
                        isAi = true,
                        lane = allyLanes[i],
                        skill3Cd = hero.skill3.cooldown,
                        skill3Level = 0,
                        aiActionDelay = 1.0f
                    )
                )
            }

            // 5 Enemies for Red Team
            val enemyLanes = listOf(Lane.TOP, Lane.MID, Lane.BOT, Lane.BOT, Lane.JUNGLE)
            for (i in 0 until 5) {
                val hero = availableHeroes.getOrNull(heroIdx++) ?: HeroCatalog.heroes[(i + 5) % HeroCatalog.heroes.size]
                entityList.add(
                    CombatEntity(
                        id = "enemy_$i",
                        name = hero.name,
                        hero = hero,
                        team = Team.RED,
                        x = 880f - i * 15f,
                        y = 880f - i * 20f,
                        isAi = true,
                        lane = enemyLanes[i],
                        skill3Cd = hero.skill3.cooldown,
                        skill3Level = 0,
                        aiActionDelay = 1.0f
                    )
                )
            }
        } else {
            // 1v1 or Practice mode: 1 enemy bot
            val enemyHero = availableHeroes.firstOrNull() ?: HeroCatalog.heroes.first()
            entityList.add(
                CombatEntity(
                    id = "enemy_duel",
                    name = enemyHero.name,
                    hero = enemyHero,
                    team = Team.RED,
                    x = 880f,
                    y = 880f,
                    isAi = true,
                    lane = Lane.MID,
                    skill3Cd = if (mode == GameMode.PRACTICE) 0f else enemyHero.skill3.cooldown,
                    skill3Level = if (mode == GameMode.PRACTICE) 1 else 0,
                    aiActionDelay = 1.0f
                )
            )
        }

        _entities.value = entityList

        // Setup Towers
        val towerList = mutableListOf<Tower>()
        if (mode == GameMode.FIVE_VS_FIVE) {
            // Blue Towers
            towerList.add(Tower("b_nexus", Team.BLUE, Lane.MID, 4, 80f, 80f, 6000f, 6000f))
            towerList.add(Tower("b_top_1", Team.BLUE, Lane.TOP, 1, 150f, 600f, 3000f, 3000f))
            towerList.add(Tower("b_top_2", Team.BLUE, Lane.TOP, 2, 150f, 350f, 3500f, 3500f))
            towerList.add(Tower("b_mid_1", Team.BLUE, Lane.MID, 1, 400f, 400f, 3000f, 3000f))
            towerList.add(Tower("b_mid_2", Team.BLUE, Lane.MID, 2, 250f, 250f, 3500f, 3500f))
            towerList.add(Tower("b_bot_1", Team.BLUE, Lane.BOT, 1, 600f, 150f, 3000f, 3000f))
            towerList.add(Tower("b_bot_2", Team.BLUE, Lane.BOT, 2, 350f, 150f, 3500f, 3500f))

            // Red Towers
            towerList.add(Tower("r_nexus", Team.RED, Lane.MID, 4, 920f, 920f, 6000f, 6000f))
            towerList.add(Tower("r_top_1", Team.RED, Lane.TOP, 1, 400f, 850f, 3000f, 3000f))
            towerList.add(Tower("r_top_2", Team.RED, Lane.TOP, 2, 650f, 850f, 3500f, 3500f))
            towerList.add(Tower("r_mid_1", Team.RED, Lane.MID, 1, 600f, 600f, 3000f, 3000f))
            towerList.add(Tower("r_mid_2", Team.RED, Lane.MID, 2, 750f, 750f, 3500f, 3500f))
            towerList.add(Tower("r_bot_1", Team.RED, Lane.BOT, 1, 850f, 400f, 3000f, 3000f))
            towerList.add(Tower("r_bot_2", Team.RED, Lane.BOT, 2, 850f, 650f, 3500f, 3500f))
        } else {
            // 1v1 / Practice: Mid lane towers only
            towerList.add(Tower("b_nexus", Team.BLUE, Lane.MID, 4, 100f, 100f, 5000f, 5000f))
            towerList.add(Tower("b_mid_1", Team.BLUE, Lane.MID, 1, 350f, 350f, 2800f, 2800f))
            towerList.add(Tower("r_nexus", Team.RED, Lane.MID, 4, 900f, 900f, 5000f, 5000f))
            towerList.add(Tower("r_mid_1", Team.RED, Lane.MID, 1, 650f, 650f, 2800f, 2800f))
        }
        _towers.value = towerList

        // Jungle Monsters
        val monsters = mutableListOf<JungleMonster>()
        monsters.add(JungleMonster("b_buff_blue", "Gô-lem Xanh", 280f, 550f, 3500f, 3500f, 90f, "Hồi Năng Lượng"))
        monsters.add(JungleMonster("b_buff_red", "Gô-lem Đỏ", 550f, 280f, 3500f, 3500f, 90f, "Làm Chậm & Sát Thương Chuẩn"))
        monsters.add(JungleMonster("r_buff_blue", "Gô-lem Xanh", 720f, 450f, 3500f, 3500f, 90f, "Hồi Năng Lượng"))
        monsters.add(JungleMonster("r_buff_red", "Gô-lem Đỏ", 450f, 720f, 3500f, 3500f, 90f, "Làm Chậm & Sát Thương Chuẩn"))
        monsters.add(JungleMonster("dragon", "Rồng Ánh Sáng", 320f, 300f, 7500f, 7500f, 160f, "Buff Vàng & Công"))
        monsters.add(JungleMonster("caesar", "Caesar Bạo Chúa", 680f, 700f, 11000f, 11000f, 210f, "Buff Siêu Lính & Hồi Phục"))
        _jungleMonsters.value = monsters
    }

    fun updateJoystick(screenVx: Float, screenVy: Float) {
        if (screenVx == 0f && screenVy == 0f) {
            joystickX = 0f
            joystickY = 0f
            return
        }
        // Map 2D screen joystick direction to 3D isometric world coordinates:
        // Push UP on screen (screenVy < 0) -> move forward towards enemy base (+x, +y)
        // Pull DOWN on screen (screenVy > 0) -> move backward towards ally base (-x, -y)
        // Push RIGHT on screen (screenVx > 0) -> move right (+x, -y)
        // Push LEFT on screen (screenVx < 0) -> move left (-x, +y)
        val cx = 0.82f
        val cy = 0.52f
        val wx = (screenVx / cx) - (screenVy / cy)
        val wy = -(screenVy / cy) - (screenVx / cx)

        val mag = hypot(wx, wy)
        if (mag > 0.001f) {
            val inputMag = hypot(screenVx, screenVy).coerceIn(0f, 1f)
            joystickX = (wx / mag) * inputMag
            joystickY = (wy / mag) * inputMag
        } else {
            joystickX = 0f
            joystickY = 0f
        }
    }

    fun tick(dt: Float) {
        if (_isGameOver.value) return

        _gameTimeSeconds.value += dt
        val currentTime = _gameTimeSeconds.value

        // Passive gold every 1 second
        goldPassiveTimer += dt
        if (goldPassiveTimer >= 1.0f) {
            goldPassiveTimer = 0f
            _entities.value.forEach { entity ->
                if (!entity.isDead) entity.gold += 3
            }
        }

        // Minion Spawning every 22 seconds
        minionSpawnTimer += dt
        if (minionSpawnTimer >= 22f || _minions.value.isEmpty()) {
            minionSpawnTimer = 0f
            spawnMinionWave()
        }

        // Update Player Movement from Joystick
        getPlayer()?.let { p ->
            if (!p.isDead && p.stunDuration <= 0f) {
                if (joystickX != 0f || joystickY != 0f) {
                    val speed = (p.hero.moveSpeed + p.speedBonus) * dt
                    p.x = (p.x + joystickX * speed).coerceIn(40f, 960f)
                    p.y = (p.y + joystickY * speed).coerceIn(40f, 960f)
                    p.isRecalling = false
                }
            }
        }

        // Update Entities (AI, Cooldowns, Respawn, Mana regen)
        updateEntities(dt, currentTime)

        // Update Minions
        updateMinions(dt)

        // Update Towers
        updateTowers(dt)

        // Update Projectiles
        updateProjectiles(dt)

        // Update Floating Texts
        updateFloatingTexts(dt)

        // Check Victory / Defeat
        checkGameStatus()
    }

    private fun spawnMinionWave() {
        val current = _minions.value.toMutableList()
        val lanes = if (mode == GameMode.FIVE_VS_FIVE) listOf(Lane.TOP, Lane.MID, Lane.BOT) else listOf(Lane.MID)

        for (lane in lanes) {
            val waveId = System.currentTimeMillis()
            // Blue minions
            val (bx, by) = getLaneStart(Team.BLUE, lane)
            current.add(Minion("b_m_${waveId}_1", Team.BLUE, lane, bx, by, 900f, 900f, 40f, false))
            current.add(Minion("b_m_${waveId}_2", Team.BLUE, lane, bx - 10f, by - 10f, 650f, 650f, 55f, true))

            // Red minions
            val (rx, ry) = getLaneStart(Team.RED, lane)
            current.add(Minion("r_m_${waveId}_1", Team.RED, lane, rx, ry, 900f, 900f, 40f, false))
            current.add(Minion("r_m_${waveId}_2", Team.RED, lane, rx + 10f, ry + 10f, 650f, 650f, 55f, true))
        }
        _minions.value = current
    }

    private fun getLaneStart(team: Team, lane: Lane): Pair<Float, Float> {
        return if (team == Team.BLUE) {
            when (lane) {
                Lane.TOP -> Pair(120f, 180f)
                Lane.MID -> Pair(160f, 160f)
                Lane.BOT -> Pair(180f, 120f)
                Lane.JUNGLE -> Pair(150f, 150f)
            }
        } else {
            when (lane) {
                Lane.TOP -> Pair(820f, 880f)
                Lane.MID -> Pair(840f, 840f)
                Lane.BOT -> Pair(880f, 820f)
                Lane.JUNGLE -> Pair(850f, 850f)
            }
        }
    }

    private fun updateEntities(dt: Float, currentTime: Float) {
        val list = _entities.value
        val towers = _towers.value
        val player = getPlayer()

        for (e in list) {
            // God mode in practice
            if (practiceGodMode && e == player) {
                e.hp = e.maxHp
                e.mp = e.maxMp
            }

            // Cooldowns
            if (practiceNoCooldown && e == player) {
                e.skill1Cd = 0f
                e.skill2Cd = 0f
                e.skill3Cd = 0f
                e.talentCd = 0f
            } else {
                if (e.skill1Cd > 0f) e.skill1Cd = (e.skill1Cd - dt).coerceAtLeast(0f)
                if (e.skill2Cd > 0f) e.skill2Cd = (e.skill2Cd - dt).coerceAtLeast(0f)
                if (e.skill3Cd > 0f) e.skill3Cd = (e.skill3Cd - dt).coerceAtLeast(0f)
                if (e.talentCd > 0f) e.talentCd = (e.talentCd - dt).coerceAtLeast(0f)
            }

            if (e.stunDuration > 0f) {
                e.stunDuration -= dt
            }

            if (e.attackCooldown > 0f) {
                e.attackCooldown = (e.attackCooldown - dt).coerceAtLeast(0f)
            }

            if (e.aiActionDelay > 0f) {
                e.aiActionDelay = (e.aiActionDelay - dt).coerceAtLeast(0f)
            }

            // Health & mana natural regen (18 HP/s out of combat)
            if (!e.isDead) {
                e.hp = (e.hp + 18f * dt).coerceAtMost(e.maxHp)
                e.mp = (e.mp + 12f * dt).coerceAtMost(e.maxMp)
            }

            // Respawn timer
            if (e.isDead) {
                e.respawnTimer -= dt
                if (e.respawnTimer <= 0f) {
                    e.isDead = false
                    e.hp = e.maxHp
                    e.mp = e.maxMp
                    val (fx, fy) = if (e.team == Team.BLUE) Pair(100f, 100f) else Pair(900f, 900f)
                    e.x = fx
                    e.y = fy
                    addFloatingText("Hồi Sinh!", e.x, e.y - 20f, 0xFF00E676)
                }
                continue
            }

            // Recall handling
            if (e.isRecalling) {
                e.recallProgress += dt
                if (e.recallProgress >= 4.0f) {
                    e.isRecalling = false
                    val (fx, fy) = if (e.team == Team.BLUE) Pair(100f, 100f) else Pair(900f, 900f)
                    e.x = fx
                    e.y = fy
                    e.hp = e.maxHp
                    e.mp = e.maxMp
                    addFloatingText("Về Tế Đàn", e.x, e.y, 0xFF00B0FF)
                }
            }

            // Fountain healing
            val isAtBlueFountain = e.team == Team.BLUE && hypot(e.x - 100f, e.y - 100f) < 80f
            val isAtRedFountain = e.team == Team.RED && hypot(e.x - 900f, e.y - 900f) < 80f
            if (isAtBlueFountain || isAtRedFountain) {
                e.hp = (e.hp + 600f * dt).coerceAtMost(e.maxHp)
                e.mp = (e.mp + 400f * dt).coerceAtMost(e.maxMp)
            }

            // AI Decision logic
            if (e.isAi && e.stunDuration <= 0f) {
                updateAiEntity(e, dt)
            }
        }
    }

    private fun updateAiEntity(bot: CombatEntity, dt: Float) {
        // Find nearest visible enemy champion
        val enemy = _entities.value.filter { it.team != bot.team && !it.isDead }
            .minByOrNull { hypot(it.x - bot.x, it.y - bot.y) }

        val distToEnemy = enemy?.let { hypot(it.x - bot.x, it.y - bot.y) } ?: Float.MAX_VALUE

        // If low HP, retreat toward own Nexus
        if (bot.hp < bot.maxHp * 0.25f) {
            val (baseX, baseY) = if (bot.team == Team.BLUE) Pair(100f, 100f) else Pair(900f, 900f)
            moveTowards(bot, baseX, baseY, dt * 1.1f)
            return
        }

        // Combat range check
        if (enemy != null && distToEnemy < bot.hero.attackRange * 1.8f) {
            // Human-like casting interval (prevents 1-frame multi-skill blast)
            if (bot.aiActionDelay <= 0f) {
                val canCastUlt = (bot.level >= 4 || practiceNoCooldown) && bot.skill3Cd <= 0f && distToEnemy < bot.hero.skill3.range
                val canCastS1 = bot.skill1Cd <= 0f && distToEnemy < bot.hero.skill1.range
                val canCastS2 = bot.skill2Cd <= 0f && distToEnemy < bot.hero.skill2.range

                if (canCastUlt) {
                    castSkillAi(bot, 3, enemy)
                    bot.aiActionDelay = 1.1f
                } else if (canCastS1) {
                    castSkillAi(bot, 1, enemy)
                    bot.aiActionDelay = 0.85f
                } else if (canCastS2) {
                    castSkillAi(bot, 2, enemy)
                    bot.aiActionDelay = 0.85f
                } else if (bot.attackCooldown <= 0f && distToEnemy <= bot.hero.attackRange * 1.25f) {
                    performAutoAttack(bot, enemy)
                }
            }

            // Maintain distance or kite
            if (bot.hero.attackRange > 100f && distToEnemy < 85f) {
                // Ranged kite away
                val dx = bot.x - enemy.x
                val dy = bot.y - enemy.y
                val len = max(1f, hypot(dx, dy))
                moveTowards(bot, bot.x + (dx / len) * 85f, bot.y + (dy / len) * 85f, dt)
            } else if (distToEnemy > bot.hero.attackRange) {
                moveTowards(bot, enemy.x, enemy.y, dt)
            }
        } else {
            // Push Lane or hunt minions/monsters
            val enemyTower = _towers.value.filter { it.team != bot.team && !it.isDestroyed }
                .minByOrNull { hypot(it.x - bot.x, it.y - bot.y) }

            val targetMinion = _minions.value.filter { it.team != bot.team && !it.isDead }
                .minByOrNull { hypot(it.x - bot.x, it.y - bot.y) }

            if (targetMinion != null && hypot(targetMinion.x - bot.x, targetMinion.y - bot.y) < 180f) {
                if (hypot(targetMinion.x - bot.x, targetMinion.y - bot.y) <= bot.hero.attackRange) {
                    if (bot.attackCooldown <= 0f) {
                        bot.attackCooldown = 1.0f
                        damageMinion(bot, targetMinion, bot.hero.baseAttack * 0.8f)
                    }
                } else {
                    moveTowards(bot, targetMinion.x, targetMinion.y, dt)
                }
            } else if (enemyTower != null) {
                moveTowards(bot, enemyTower.x, enemyTower.y, dt)
            } else {
                val (targetX, targetY) = if (bot.team == Team.BLUE) Pair(880f, 880f) else Pair(120f, 120f)
                moveTowards(bot, targetX, targetY, dt)
            }
        }
    }

    private fun castSkillAi(caster: CombatEntity, skillIndex: Int, target: CombatEntity) {
        when (skillIndex) {
            1 -> {
                caster.skill1Cd = caster.hero.skill1.cooldown
                applySkillDamage(caster, target, caster.hero.skill1)
            }
            2 -> {
                caster.skill2Cd = caster.hero.skill2.cooldown
                applySkillDamage(caster, target, caster.hero.skill2)
            }
            3 -> {
                caster.skill3Cd = caster.hero.skill3.cooldown
                applySkillDamage(caster, target, caster.hero.skill3)
            }
        }
    }

    private fun moveTowards(entity: CombatEntity, tx: Float, ty: Float, dt: Float) {
        val dx = tx - entity.x
        val dy = ty - entity.y
        val dist = hypot(dx, dy)
        if (dist > 5f) {
            val step = min(dist, (entity.hero.moveSpeed + entity.speedBonus) * dt)
            entity.x += (dx / dist) * step
            entity.y += (dy / dist) * step
        }
    }

    private fun updateMinions(dt: Float) {
        val minions = _minions.value
        val towers = _towers.value
        val heroes = _entities.value

        for (m in minions) {
            if (m.isDead) continue

            // Find enemy targets: Minions first, then heroes, then towers
            val oppMinions = minions.filter { it.team != m.team && !it.isDead }
                .minByOrNull { hypot(it.x - m.x, it.y - m.y) }

            val oppHeroes = heroes.filter { it.team != m.team && !it.isDead }
                .minByOrNull { hypot(it.x - m.x, it.y - m.y) }

            val oppTower = towers.filter { it.team != m.team && !it.isDestroyed }
                .minByOrNull { hypot(it.x - m.x, it.y - m.y) }

            val targetMinion = oppMinions
            val targetHero = oppHeroes
            val targetDist = if (targetMinion != null) hypot(targetMinion.x - m.x, targetMinion.y - m.y)
                             else if (targetHero != null) hypot(targetHero.x - m.x, targetHero.y - m.y)
                             else Float.MAX_VALUE

            val range = if (m.isRanged) 110f else 40f
            if (targetDist <= range) {
                // Attack cooldown
                m.attackCooldown -= dt
                if (m.attackCooldown <= 0f) {
                    m.attackCooldown = 1.2f
                    if (targetMinion != null) {
                        targetMinion.hp -= m.attack
                        if (targetMinion.hp <= 0f) targetMinion.isDead = true
                    } else if (targetHero != null) {
                        dealDamageToHero(null, targetHero, m.attack, DamageType.VAT_LY)
                    }
                }
            } else if (oppTower != null && hypot(oppTower.x - m.x, oppTower.y - m.y) <= range + 30f) {
                m.attackCooldown -= dt
                if (m.attackCooldown <= 0f) {
                    m.attackCooldown = 1.2f
                    oppTower.hp -= m.attack * 1.4f
                    if (oppTower.hp <= 0f) {
                        oppTower.isDestroyed = true
                        soundAndHaptic?.playTowerDestroyed()
                        addFloatingText("Phá Trụ!", oppTower.x, oppTower.y, 0xFFFFD700)
                    }
                }
            } else {
                // Move towards opponent base
                val (goalX, goalY) = if (m.team == Team.BLUE) Pair(900f, 900f) else Pair(100f, 100f)
                val dx = goalX - m.x
                val dy = goalY - m.y
                val dist = hypot(dx, dy)
                if (dist > 5f) {
                    val speed = 55f * dt
                    m.x += (dx / dist) * speed
                    m.y += (dy / dist) * speed
                }
            }
        }

        // Clean dead minions
        _minions.value = minions.filter { !it.isDead }
    }

    private fun updateTowers(dt: Float) {
        val towers = _towers.value
        val minions = _minions.value
        val heroes = _entities.value

        for (t in towers) {
            if (t.isDestroyed) continue
            t.attackCooldown -= dt
            if (t.attackCooldown <= 0f) {
                // Target enemy minions first inside tower range (140f)
                val targetMinion = minions.filter { it.team != t.team && !it.isDead && hypot(it.x - t.x, it.y - t.y) < 140f }
                    .minByOrNull { hypot(it.x - t.x, it.y - t.y) }

                val targetHero = heroes.filter { it.team != t.team && !it.isDead && hypot(it.x - t.x, it.y - t.y) < 140f }
                    .minByOrNull { hypot(it.x - t.x, it.y - t.y) }

                val targetX = targetMinion?.x ?: targetHero?.x
                val targetY = targetMinion?.y ?: targetHero?.y
                if (targetX != null && targetY != null) {
                    t.attackCooldown = 1.3f
                    // Shoot tower laser / projectile
                    createProjectile(
                        shooterId = t.id,
                        shooterTeam = t.team,
                        startX = t.x,
                        startY = t.y,
                        targetX = targetX,
                        targetY = targetY,
                        damage = 420f,
                        type = DamageType.VAT_LY,
                        color = if (t.team == Team.BLUE) 0xFF00E5FF else 0xFFFF1744,
                        isHoming = true,
                        targetId = targetHero?.id
                    )
                }
            }
        }
    }

    private fun updateProjectiles(dt: Float) {
        val projs = _projectiles.value.toMutableList()
        val removeList = mutableListOf<Projectile>()

        for (p in projs) {
            val dx = p.targetX - p.x
            val dy = p.targetY - p.y
            val dist = hypot(dx, dy)
            val moveStep = p.speed * dt

            if (dist <= moveStep) {
                // Hit target
                p.x = p.targetX
                p.y = p.targetY
                removeList.add(p)

                // Damage target
                if (p.targetEntityId != null) {
                    val hitEntity = _entities.value.find { it.id == p.targetEntityId && !it.isDead }
                    hitEntity?.let {
                        val killer = _entities.value.find { it.id == p.shooterId }
                        dealDamageToHero(killer, it, p.damage, p.damageType)
                        if (p.stunSeconds > 0f) {
                            it.stunDuration = p.stunSeconds
                            addFloatingText("Choáng!", it.x, it.y - 25f, 0xFFFFD700)
                        }
                    }
                } else {
                    // Check AOE collision
                    val enemiesHit = _entities.value.filter { it.team != p.shooterTeam && !it.isDead && hypot(it.x - p.x, it.y - p.y) < p.radius + 20f }
                    val killer = _entities.value.find { it.id == p.shooterId }
                    for (eh in enemiesHit) {
                        dealDamageToHero(killer, eh, p.damage, p.damageType)
                    }
                }
            } else {
                p.x += (dx / dist) * moveStep
                p.y += (dy / dist) * moveStep
            }
        }

        projs.removeAll(removeList)
        _projectiles.value = projs
    }

    private fun updateFloatingTexts(dt: Float) {
        val texts = _floatingTexts.value.toMutableList()
        val removeList = mutableListOf<FloatingText>()

        for (ft in texts) {
            ft.lifeTime -= dt
            ft.y -= 25f * dt
            ft.alpha = (ft.lifeTime / 1.0f).coerceIn(0f, 1f)
            if (ft.lifeTime <= 0f) {
                removeList.add(ft)
            }
        }
        texts.removeAll(removeList)
        _floatingTexts.value = texts
    }

    // Player action: Normal Attack
    fun playerBasicAttack() {
        val player = getPlayer() ?: return
        if (player.isDead || player.stunDuration > 0f) return
        if (player.attackCooldown > 0f) return

        player.isRecalling = false

        // Cooldown between basic attacks based on attack speed
        val atkSpeed = calculateAttackSpeed(player)
        player.attackCooldown = (1.0f / atkSpeed).coerceIn(0.35f, 1.2f)

        // Find nearest enemy (Hero preferred, then Jungle, then Minion, then Tower)
        val enemyHero = _entities.value.filter { it.team != player.team && !it.isDead && hypot(it.x - player.x, it.y - player.y) <= player.hero.attackRange * 1.2f }
            .minByOrNull { hypot(it.x - player.x, it.y - player.y) }

        val enemyMonster = _jungleMonsters.value.filter { !it.isDead && hypot(it.x - player.x, it.y - player.y) <= player.hero.attackRange * 1.2f }
            .minByOrNull { hypot(it.x - player.x, it.y - player.y) }

        val enemyMinion = _minions.value.filter { it.team != player.team && !it.isDead && hypot(it.x - player.x, it.y - player.y) <= player.hero.attackRange * 1.2f }
            .minByOrNull { hypot(it.x - player.x, it.y - player.y) }

        val enemyTower = _towers.value.filter { it.team != player.team && !it.isDestroyed && hypot(it.x - player.x, it.y - player.y) <= player.hero.attackRange * 1.2f }
            .minByOrNull { hypot(it.x - player.x, it.y - player.y) }

        val attackPower = calculateTotalAttack(player)

        if (enemyHero != null) {
            performAutoAttack(player, enemyHero)
            soundAndHaptic?.playAttackHit()
        } else if (enemyMonster != null) {
            enemyMonster.hp -= attackPower
            addFloatingText("-${attackPower.toInt()}", enemyMonster.x, enemyMonster.y - 15f, 0xFFFF9100)
            if (enemyMonster.hp <= 0f) {
                enemyMonster.isDead = true
                player.gold += 120
                player.exp += 250f
                checkLevelUp(player)
                addFloatingText("+120 Vàng (${enemyMonster.buffType})", player.x, player.y - 25f, 0xFFFFD700)
            }
            soundAndHaptic?.playAttackHit()
        } else if (enemyMinion != null) {
            damageMinion(player, enemyMinion, attackPower)
            soundAndHaptic?.playAttackHit()
        } else if (enemyTower != null) {
            enemyTower.hp -= attackPower
            addFloatingText("-${attackPower.toInt()}", enemyTower.x, enemyTower.y - 20f, 0xFFFF9100)
            if (enemyTower.hp <= 0f) {
                enemyTower.isDestroyed = true
                player.gold += 200
                soundAndHaptic?.playTowerDestroyed()
                addFloatingText("Trụ Đã Phá Huỷ!", enemyTower.x, enemyTower.y, 0xFFFFD700)
            }
            soundAndHaptic?.playAttackHit()
        }
    }

    // Player action: Focus Minion/Monster Attack
    fun playerAttackMinion() {
        val player = getPlayer() ?: return
        if (player.isDead || player.stunDuration > 0f) return
        if (player.attackCooldown > 0f) return
        player.isRecalling = false

        val atkSpeed = calculateAttackSpeed(player)
        player.attackCooldown = (1.0f / atkSpeed).coerceIn(0.35f, 1.2f)

        val enemyMinion = _minions.value.filter { it.team != player.team && !it.isDead && hypot(it.x - player.x, it.y - player.y) <= player.hero.attackRange * 1.3f }
            .minByOrNull { hypot(it.x - player.x, it.y - player.y) }
        val enemyMonster = _jungleMonsters.value.filter { !it.isDead && hypot(it.x - player.x, it.y - player.y) <= player.hero.attackRange * 1.3f }
            .minByOrNull { hypot(it.x - player.x, it.y - player.y) }

        val attackPower = calculateTotalAttack(player)
        if (enemyMinion != null) {
            damageMinion(player, enemyMinion, attackPower)
            soundAndHaptic?.playAttackHit()
        } else if (enemyMonster != null) {
            enemyMonster.hp -= attackPower
            addFloatingText("-${attackPower.toInt()}", enemyMonster.x, enemyMonster.y - 15f, 0xFFFF9100)
            if (enemyMonster.hp <= 0f) {
                enemyMonster.isDead = true
                player.gold += 120
                player.exp += 250f
                checkLevelUp(player)
                addFloatingText("+120 Vàng (${enemyMonster.buffType})", player.x, player.y - 25f, 0xFFFFD700)
            }
            soundAndHaptic?.playAttackHit()
        } else {
            playerBasicAttack()
        }
    }

    // Player action: Focus Tower Attack
    fun playerAttackTower() {
        val player = getPlayer() ?: return
        if (player.isDead || player.stunDuration > 0f) return
        if (player.attackCooldown > 0f) return
        player.isRecalling = false

        val atkSpeed = calculateAttackSpeed(player)
        player.attackCooldown = (1.0f / atkSpeed).coerceIn(0.35f, 1.2f)

        val enemyTower = _towers.value.filter { it.team != player.team && !it.isDestroyed && hypot(it.x - player.x, it.y - player.y) <= player.hero.attackRange * 1.3f }
            .minByOrNull { hypot(it.x - player.x, it.y - player.y) }

        if (enemyTower != null) {
            val attackPower = calculateTotalAttack(player)
            enemyTower.hp -= attackPower
            addFloatingText("-${attackPower.toInt()}", enemyTower.x, enemyTower.y - 20f, 0xFFFF9100)
            if (enemyTower.hp <= 0f) {
                enemyTower.isDestroyed = true
                player.gold += 200
                soundAndHaptic?.playTowerDestroyed()
                addFloatingText("Trụ Đã Phá Huỷ!", enemyTower.x, enemyTower.y, 0xFFFFD700)
            }
            soundAndHaptic?.playAttackHit()
        } else {
            playerBasicAttack()
        }
    }

    // Player upgrades skill point
    fun upgradeSkill(skillNum: Int) {
        val player = getPlayer() ?: return
        if (player.availableSkillPoints <= 0) return
        when (skillNum) {
            1 -> { player.skill1Level += 1; player.availableSkillPoints -= 1 }
            2 -> { player.skill2Level += 1; player.availableSkillPoints -= 1 }
            3 -> { player.skill3Level += 1; player.availableSkillPoints -= 1 }
        }
        addFloatingText("Cộng Điểm Chiêu $skillNum!", player.x, player.y - 30f, 0xFFFFD700)
    }

    // Ping quick chat
    fun sendPing(text: String) {
        val player = getPlayer() ?: return
        addFloatingText("📢 $text", player.x, player.y - 35f, 0xFFFFD700)
        soundAndHaptic?.playSkillCast()
    }

    private fun performAutoAttack(attacker: CombatEntity, target: CombatEntity) {
        val totalAtk = calculateTotalAttack(attacker)
        var isCrit = false
        var critBonus = 1.0f
        var totalCritChance = 0f
        attacker.inventory.forEach {
            totalCritChance += it.critChance
            if (it.id == "thanh_kiem") critBonus = 1.5f
        }
        if (Math.random() < totalCritChance) {
            isCrit = true
        }
        val finalDmg = if (isCrit) totalAtk * (1.5f + critBonus * 0.5f) else totalAtk

        if (attacker.hero.attackRange > 100f) {
            // Ranged projectile
            createProjectile(
                shooterId = attacker.id,
                shooterTeam = attacker.team,
                startX = attacker.x,
                startY = attacker.y,
                targetX = target.x,
                targetY = target.y,
                damage = finalDmg,
                type = attacker.hero.damageType,
                color = attacker.hero.themeColorHex,
                isHoming = true,
                targetId = target.id
            )
        } else {
            // Melee instant strike
            dealDamageToHero(attacker, target, finalDmg, attacker.hero.damageType)
        }
        if (isCrit) {
            addFloatingText("CHÍ MẠNG!", attacker.x, attacker.y - 30f, 0xFFFF1744)
        }
    }

    private fun damageMinion(attacker: CombatEntity?, minion: Minion, damage: Float) {
        minion.hp -= damage
        addFloatingText("-${damage.toInt()}", minion.x, minion.y - 15f, 0xFFFF9100)
        if (minion.hp <= 0f) {
            minion.isDead = true
            attacker?.let {
                it.gold += 45
                it.exp += 80f
                checkLevelUp(it)
                addFloatingText("+45 Vàng", it.x, it.y - 20f, 0xFFFFD700)
            }
        }
    }

    // Player casts Skill 1, 2, or 3
    fun playerCastSkill(skillNum: Int) {
        val player = getPlayer() ?: return
        if (player.isDead || player.stunDuration > 0f) return

        player.isRecalling = false

        when (skillNum) {
            1 -> {
                if (player.skill1Level <= 0 && !practiceGodMode && mode != GameMode.PRACTICE) return
                if (player.skill1Cd > 0f && !practiceNoCooldown) return
                if (player.mp < player.hero.skill1.manaCost && !practiceGodMode) return
                player.mp -= player.hero.skill1.manaCost
                player.skill1Cd = player.hero.skill1.cooldown
                executeHeroSkill(player, player.hero.skill1)
                soundAndHaptic?.playSkillCast()
            }
            2 -> {
                if (player.skill2Level <= 0 && !practiceGodMode && mode != GameMode.PRACTICE) return
                if (player.skill2Cd > 0f && !practiceNoCooldown) return
                if (player.mp < player.hero.skill2.manaCost && !practiceGodMode) return
                player.mp -= player.hero.skill2.manaCost
                player.skill2Cd = player.hero.skill2.cooldown
                executeHeroSkill(player, player.hero.skill2)
                soundAndHaptic?.playSkillCast()
            }
            3 -> {
                if (player.skill3Level <= 0 && !practiceGodMode && mode != GameMode.PRACTICE) return
                if (player.skill3Cd > 0f && !practiceNoCooldown) return
                if (player.mp < player.hero.skill3.manaCost && !practiceGodMode) return
                player.mp -= player.hero.skill3.manaCost
                player.skill3Cd = player.hero.skill3.cooldown
                executeHeroSkill(player, player.hero.skill3)
                soundAndHaptic?.playSkillCast()
            }
        }
    }

    private fun executeHeroSkill(caster: CombatEntity, skill: HeroSkill) {
        // Find targets within skill range
        val enemies = _entities.value.filter { it.team != caster.team && !it.isDead && hypot(it.x - caster.x, it.y - caster.y) <= skill.range }
        val nearestEnemy = enemies.minByOrNull { hypot(it.x - caster.x, it.y - caster.y) }

        // Specific hero skill animations & logic
        when (skill.id) {
            "val_s2" -> { // Valhein yellow stun dart
                nearestEnemy?.let {
                    createProjectile(caster.id, caster.team, caster.x, caster.y, it.x, it.y, skill.baseDamage, DamageType.PHEP, 0xFFFFD700, true, it.id, stunSeconds = 1.0f)
                }
            }
            "nak_s1", "nak_s2" -> { // Nakroth dash & airborne
                // Dash caster forward
                val dashDist = 80f
                nearestEnemy?.let {
                    val dx = it.x - caster.x
                    val dy = it.y - caster.y
                    val len = max(1f, hypot(dx, dy))
                    caster.x += (dx / len) * dashDist
                    caster.y += (dy / len) * dashDist
                    dealDamageToHero(caster, it, skill.baseDamage, DamageType.VAT_LY)
                    it.stunDuration = 0.8f
                    addFloatingText("Hất Tung!", it.x, it.y - 25f, 0xFFFF5252)
                } ?: run {
                    caster.x += joystickX * dashDist
                    caster.y += joystickY * dashDist
                }
            }
            "flo_s1" -> { // Florentino throw flower
                nearestEnemy?.let {
                    dealDamageToHero(caster, it, skill.baseDamage, DamageType.VAT_LY)
                    addFloatingText("Đóa Hoa!", it.x, it.y - 25f, 0xFFFF4081)
                    caster.speedBonus = 60f
                }
            }
            "tul_s3" -> { // Tulen thunderbird ult
                nearestEnemy?.let {
                    createProjectile(caster.id, caster.team, caster.x, caster.y, it.x, it.y, skill.baseDamage + (it.maxHp - it.hp) * 0.25f, DamageType.PHEP, 0xFF00E5FF, true, it.id)
                }
            }
            "kri_s2" -> { // Krixi airborne
                for (en in enemies) {
                    dealDamageToHero(caster, en, skill.baseDamage, DamageType.PHEP)
                    en.stunDuration = 1.2f
                    addFloatingText("Bão Lá Hất Tung!", en.x, en.y - 25f, 0xFF76FF03)
                }
            }
            "gra_s2" -> { // Grakk hook
                nearestEnemy?.let {
                    createProjectile(caster.id, caster.team, caster.x, caster.y, it.x, it.y, skill.baseDamage, DamageType.PHEP, 0xFF00BFA5, true, it.id)
                    // Pull to Grakk
                    it.x = caster.x + 30f
                    it.y = caster.y + 30f
                    it.stunDuration = 0.8f
                    addFloatingText("Bị Kéo!", it.x, it.y - 25f, 0xFFFF1744)
                }
            }
            "mur_s3" -> { // Murad ultimate slashes
                for (en in enemies) {
                    dealDamageToHero(caster, en, skill.baseDamage, DamageType.VAT_LY)
                    addFloatingText("Ảo Ảnh Trảm x5!", en.x, en.y - 25f, 0xFFFF6D00)
                }
            }
            else -> {
                // Generic skill projectile or AOE
                if (nearestEnemy != null) {
                    applySkillDamage(caster, nearestEnemy, skill)
                } else {
                    // Fire in direction of movement or forward
                    val targetX = caster.x + (if (joystickX != 0f) joystickX else 1f) * skill.range
                    val targetY = caster.y + (if (joystickY != 0f) joystickY else 0f) * skill.range
                    createProjectile(caster.id, caster.team, caster.x, caster.y, targetX, targetY, skill.baseDamage, caster.hero.damageType, caster.hero.themeColorHex)
                }
            }
        }
    }

    private fun applySkillDamage(caster: CombatEntity, target: CombatEntity, skill: HeroSkill) {
        val totalDmg = skill.baseDamage + (caster.level * 22f)
        dealDamageToHero(caster, target, totalDmg, caster.hero.damageType)
    }

    // Talent Spell: Tốc Biến / Flash or Cấp Cứu
    fun playerCastTalent() {
        val player = getPlayer() ?: return
        if (player.isDead || (player.talentCd > 0f && !practiceNoCooldown)) return

        player.talentCd = 60f
        // Flash forward in joystick direction
        val flashDist = 140f
        val dirX = if (joystickX != 0f || joystickY != 0f) joystickX else 1f
        val dirY = if (joystickX != 0f || joystickY != 0f) joystickY else 0f
        player.x = (player.x + dirX * flashDist).coerceIn(40f, 960f)
        player.y = (player.y + dirY * flashDist).coerceIn(40f, 960f)
        addFloatingText("Tốc Biến!", player.x, player.y - 20f, 0xFFFFD700)
        soundAndHaptic?.playSkillCast()
    }

    // Recall
    fun playerStartRecall() {
        val player = getPlayer() ?: return
        if (player.isDead) return
        player.isRecalling = true
        player.recallProgress = 0f
        addFloatingText("Đang Biến Về...", player.x, player.y - 20f, 0xFF00B0FF)
    }

    // Buy Item
    fun buyItem(item: MobaItem): Boolean {
        val player = getPlayer() ?: return false
        if (player.gold >= item.cost && player.inventory.size < 6) {
            player.gold -= item.cost
            player.inventory.add(item)
            // Apply stats
            player.maxHp += item.hpBonus
            player.hp += item.hpBonus
            addFloatingText("+ Trang Bị: ${item.name}", player.x, player.y - 25f, 0xFFFFD700)
            return true
        }
        return false
    }

    private fun dealDamageToHero(killer: CombatEntity?, victim: CombatEntity, amount: Float, type: DamageType) {
        if (victim.isDead) return

        var actualDmg = amount
        when (type) {
            DamageType.VAT_LY -> {
                val armor = calculateTotalArmor(victim)
                actualDmg = amount * (600f / (600f + armor))
            }
            DamageType.PHEP -> {
                val magicResist = calculateTotalMagicResist(victim)
                actualDmg = amount * (600f / (600f + magicResist))
            }
            DamageType.CHUAN -> {
                actualDmg = amount
            }
        }

        // Damage Cap: Prevent instant 1-shot (ensures authentic Liên Quân TTK and skill combos)
        // A single skill/hit cannot wipe more than 28% max HP at early game (level < 4), or 38% at late game
        val maxSingleHit = if (victim.level < 4) victim.maxHp * 0.28f else victim.maxHp * 0.38f
        if (actualDmg > maxSingleHit && type != DamageType.CHUAN) {
            actualDmg = maxSingleHit
        }

        // Shield absorption
        if (victim.shield > 0f) {
            if (victim.shield >= actualDmg) {
                victim.shield -= actualDmg
                addFloatingText("Chặn Lớp Khiên!", victim.x, victim.y - 20f, 0xFFFFFFFF)
                return
            } else {
                actualDmg -= victim.shield
                victim.shield = 0f
            }
        }

        victim.hp = (victim.hp - actualDmg).coerceAtLeast(0f)

        val color = when (type) {
            DamageType.VAT_LY -> 0xFFFF9100 // Orange
            DamageType.PHEP -> 0xFF9D4EDD   // Purple
            DamageType.CHUAN -> 0xFFFFFFFF  // White
        }
        addFloatingText("-${actualDmg.toInt()}", victim.x, victim.y - 20f, color)

        if (victim.hp <= 0f) {
            victim.hp = 0f
            victim.isDead = true
            victim.deaths += 1
            victim.respawnTimer = 10f + victim.level * 2f

            if (killer != null) {
                killer.kills += 1
                killer.gold += 200
                killer.exp += 300f
                checkLevelUp(killer)
                handleKillAnnouncement(killer, victim)
            } else {
                handleKillAnnouncement(null, victim)
            }
        }
    }

    private fun handleKillAnnouncement(killer: CombatEntity?, victim: CombatEntity) {
        val killerTeam = killer?.team ?: if (victim.team == Team.BLUE) Team.RED else Team.BLUE
        val killerName = killer?.name ?: "Trụ Phòng Thủ"

        if (killerTeam == Team.BLUE) {
            _blueScore.value += 1
        } else {
            _redScore.value += 1
        }

        var announcementTitle = "Hạ Gục!"
        if (killer != null && !killer.isAi) {
            // Player scored kill!
            val now = _gameTimeSeconds.value
            if (now - lastKillTime < 9.0f) {
                playerRecentKills += 1
            } else {
                playerRecentKills = 1
            }
            lastKillTime = now

            announcementTitle = when (playerRecentKills) {
                2 -> "DOUBLE KILL!"
                3 -> "TRIPLE KILL!"
                4 -> "QUADRA KILL!"
                5 -> "MEGA KILL (PENTAKILL)!"
                else -> if (_blueScore.value + _redScore.value == 1) "CHIẾN CÔNG ĐẦU!" else "ĐÃ HẠ GỤC!"
            }
            soundAndHaptic?.playKillFeedback()
        }

        val announce = KillAnnouncement(
            id = System.currentTimeMillis().toString(),
            killerName = killerName,
            victimName = victim.name,
            killerTeam = killerTeam,
            title = announcementTitle,
            message = "$killerName đã hạ gục ${victim.name}!"
        )
        val list = _killAnnouncements.value.toMutableList()
        list.add(0, announce)
        _killAnnouncements.value = list.take(5)
    }

    private fun checkLevelUp(entity: CombatEntity) {
        val nextExp = entity.level * 220f
        if (entity.exp >= nextExp && entity.level < 15) {
            entity.exp -= nextExp
            entity.level += 1
            val hpGain = when (entity.hero.role) {
                HeroRole.DO_DON, HeroRole.TRO_THU -> 480f
                HeroRole.DAU_SI -> 400f
                else -> 330f
            }
            entity.maxHp += hpGain
            entity.hp += hpGain
            entity.maxMp += 40f
            entity.availableSkillPoints += 1

            if (entity.isAi) {
                if (entity.level in listOf(4, 8, 12)) {
                    entity.skill3Level += 1
                } else if (entity.skill1Level <= entity.skill2Level) {
                    entity.skill1Level += 1
                } else {
                    entity.skill2Level += 1
                }
            }
            addFloatingText("CẤP ${entity.level}!", entity.x, entity.y - 30f, 0xFFFFD700)
        }
    }

    private fun calculateTotalAttack(entity: CombatEntity): Float {
        var bonus = 0f
        entity.inventory.forEach { bonus += it.attackBonus }
        return entity.hero.baseAttack + (entity.level * 14f) + bonus
    }

    private fun calculateTotalArmor(entity: CombatEntity): Float {
        var bonus = 0f
        entity.inventory.forEach { bonus += it.armorBonus }
        return entity.hero.baseDefense + (entity.level * 18f) + bonus
    }

    private fun calculateTotalMagicResist(entity: CombatEntity): Float {
        var bonus = 0f
        entity.inventory.forEach { bonus += it.magicResistBonus }
        return entity.hero.baseMagicResist + (entity.level * 14f) + bonus
    }

    private fun calculateAttackSpeed(entity: CombatEntity): Float {
        var bonus = 0f
        entity.inventory.forEach { bonus += it.attackSpeed }
        return 1.0f + bonus
    }

    private fun createProjectile(
        shooterId: String,
        shooterTeam: Team,
        startX: Float,
        startY: Float,
        targetX: Float,
        targetY: Float,
        damage: Float,
        type: DamageType,
        color: Long,
        isHoming: Boolean = false,
        targetId: String? = null,
        stunSeconds: Float = 0f
    ) {
        val proj = Projectile(
            id = "proj_${System.currentTimeMillis()}_${(0..999).random()}",
            shooterId = shooterId,
            shooterTeam = shooterTeam,
            x = startX,
            y = startY,
            targetX = targetX,
            targetY = targetY,
            speed = 400f,
            damage = damage,
            damageType = type,
            color = color,
            isHoming = isHoming,
            targetEntityId = targetId,
            stunSeconds = stunSeconds
        )
        val list = _projectiles.value.toMutableList()
        list.add(proj)
        _projectiles.value = list
    }

    private fun addFloatingText(text: String, x: Float, y: Float, color: Long) {
        val ft = FloatingText(
            id = "ft_${System.currentTimeMillis()}_${(0..999).random()}",
            text = text,
            x = x,
            y = y,
            color = color
        )
        val list = _floatingTexts.value.toMutableList()
        list.add(ft)
        _floatingTexts.value = list
    }

    private fun checkGameStatus() {
        val blueNexus = _towers.value.find { it.id == "b_nexus" }
        val redNexus = _towers.value.find { it.id == "r_nexus" }

        if (redNexus?.isDestroyed == true && !_isGameOver.value) {
            _isGameOver.value = true
            _isVictory.value = true
            soundAndHaptic?.playVictory()
        } else if (blueNexus?.isDestroyed == true && !_isGameOver.value) {
            _isGameOver.value = true
            _isVictory.value = false
            soundAndHaptic?.playVictory()
        }
    }

    fun getPlayer(): CombatEntity? {
        return _entities.value.find { !it.isAi }
    }
}
