package com.example.model

enum class Team(val displayName: String) {
    BLUE("Đội Xanh"),
    RED("Đội Đỏ")
}

enum class GameMode(val title: String, val subtitle: String) {
    FIVE_VS_FIVE("5v5 Bình Nguyên Vô Tận", "Chiến trường 3 đường kinh điển, quái rừng & Caesar"),
    ONE_VS_ONE("1v1 Cầu Độc Đạo", "Đấu trường đơn 1 đường solo kỹ năng đỉnh cao"),
    PRACTICE("Phòng Tập Luyện", "Thử tướng, bất tử, hồi chiêu 0s, thử sát thương")
}

enum class Lane {
    TOP, MID, BOT, JUNGLE
}

data class CombatEntity(
    val id: String,
    val name: String,
    val hero: Hero,
    val team: Team,
    var x: Float,
    var y: Float,
    var targetX: Float = x,
    var targetY: Float = y,
    var hp: Float = hero.baseHp,
    var maxHp: Float = hero.baseHp,
    var mp: Float = hero.baseMp,
    var maxMp: Float = hero.baseMp,
    var level: Int = 1,
    var exp: Float = 0f,
    var gold: Int = 300,
    var kills: Int = 0,
    var deaths: Int = 0,
    var assists: Int = 0,
    var isDead: Boolean = false,
    var respawnTimer: Float = 0f,
    var skill1Cd: Float = 0f,
    var skill2Cd: Float = 0f,
    var skill3Cd: Float = 0f,
    var talentCd: Float = 0f,
    var skill1Level: Int = 1,
    var skill2Level: Int = 0,
    var skill3Level: Int = 0,
    var availableSkillPoints: Int = 0,
    val inventory: MutableList<MobaItem> = mutableListOf(),
    var isRecalling: Boolean = false,
    var recallProgress: Float = 0f,
    var stunDuration: Float = 0f,
    var shield: Float = 0f,
    var speedBonus: Float = 0f,
    var attackCooldown: Float = 0f,
    var aiActionDelay: Float = 0f,
    var isAi: Boolean = true,
    var lane: Lane = Lane.MID,
    var targetEnemyId: String? = null
)

data class Tower(
    val id: String,
    val team: Team,
    val lane: Lane,
    val tier: Int, // 1: Outer, 2: Inner, 3: High ground, 4: Nexus
    val x: Float,
    val y: Float,
    var hp: Float,
    var maxHp: Float,
    var isDestroyed: Boolean = false,
    var attackCooldown: Float = 0f
)

data class Minion(
    val id: String,
    val team: Team,
    val lane: Lane,
    var x: Float,
    var y: Float,
    var hp: Float,
    var maxHp: Float,
    val attack: Float,
    val isRanged: Boolean = false,
    var isDead: Boolean = false,
    var attackCooldown: Float = 0f
)

data class JungleMonster(
    val id: String,
    val name: String,
    val x: Float,
    val y: Float,
    var hp: Float,
    var maxHp: Float,
    val attack: Float,
    val buffType: String,
    var isDead: Boolean = false,
    var respawnTimer: Float = 0f,
    var attackCooldown: Float = 0f
)

data class Projectile(
    val id: String,
    val shooterId: String,
    val shooterTeam: Team,
    var x: Float,
    var y: Float,
    val targetX: Float,
    val targetY: Float,
    val speed: Float,
    val damage: Float,
    val damageType: DamageType,
    val radius: Float = 10f,
    val color: Long,
    val isHoming: Boolean = false,
    val targetEntityId: String? = null,
    val stunSeconds: Float = 0f
)

data class FloatingText(
    val id: String,
    val text: String,
    var x: Float,
    var y: Float,
    val color: Long,
    var alpha: Float = 1.0f,
    var lifeTime: Float = 1.0f
)

data class KillAnnouncement(
    val id: String,
    val killerName: String,
    val victimName: String,
    val killerTeam: Team,
    val title: String,
    val message: String
)
