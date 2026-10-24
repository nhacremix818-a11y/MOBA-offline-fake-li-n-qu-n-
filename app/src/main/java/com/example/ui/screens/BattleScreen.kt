package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.ItemCatalog
import com.example.game.GameEngine
import com.example.model.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.*

@Composable
fun BattleScreen(
    gameEngine: GameEngine,
    onMatchEnd: (isVictory: Boolean, kills: Int, deaths: Int, assists: Int, goldEarned: Int) -> Unit,
    onSurrender: () -> Unit
) {
    BackHandler { onSurrender() }

    val entities by gameEngine.entities.collectAsState()
    val towers by gameEngine.towers.collectAsState()
    val minions by gameEngine.minions.collectAsState()
    val jungleMonsters by gameEngine.jungleMonsters.collectAsState()
    val projectiles by gameEngine.projectiles.collectAsState()
    val floatingTexts by gameEngine.floatingTexts.collectAsState()
    val killAnnouncements by gameEngine.killAnnouncements.collectAsState()
    val gameTime by gameEngine.gameTimeSeconds.collectAsState()
    val blueScore by gameEngine.blueScore.collectAsState()
    val redScore by gameEngine.redScore.collectAsState()
    val isGameOver by gameEngine.isGameOver.collectAsState()
    val isVictory by gameEngine.isVictory.collectAsState()

    val player = entities.find { !it.isAi }
    val enemyHeroes = entities.filter { it.team == Team.RED && it.hero != null }

    var showShopDialog by remember { mutableStateOf(false) }

    // 60 FPS Game Loop
    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            var lastTime = System.nanoTime()
            while (true) {
                val now = System.nanoTime()
                val dt = ((now - lastTime) / 1_000_000_000f).coerceIn(0.005f, 0.05f)
                lastTime = now
                gameEngine.tick(dt)
                delay(16) // ~60fps
            }
        }
    }

    // Trigger end match when game over
    LaunchedEffect(isGameOver) {
        if (isGameOver) {
            delay(1500)
            player?.let { p ->
                onMatchEnd(isVictory, p.kills, p.deaths, p.assists, p.gold / 2)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070B14))
    ) {
        // Main 3D Isometric Battleground Arena Canvas
        val playerX = player?.x ?: 500f
        val playerY = player?.y ?: 500f

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .testTag("battle_arena_canvas")
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val centerX = canvasWidth / 2f
            val centerY = canvasHeight / 2f
            val zoom = 1.35f

            // 3D Isometric Projection Helper
            fun p3D(wx: Float, wy: Float, wz: Float = 0f): Offset {
                val dx = wx - playerX
                val dy = wy - playerY
                val rx = (dx - dy) * 0.82f
                val ry = -(dx + dy) * 0.52f - wz
                return Offset(centerX + rx * zoom, centerY + ry * zoom)
            }

            // 1. Draw 3D Isometric Ground Diamond
            val p00 = p3D(0f, 0f, 0f)
            val p10 = p3D(1000f, 0f, 0f)
            val p11 = p3D(1000f, 1000f, 0f)
            val p01 = p3D(0f, 1000f, 0f)

            val groundPath = Path().apply {
                moveTo(p00.x, p00.y)
                lineTo(p10.x, p10.y)
                lineTo(p11.x, p11.y)
                lineTo(p01.x, p01.y)
                close()
            }
            drawPath(groundPath, color = Color(0xFF14241B))

            // Border of the map
            drawPath(groundPath, color = Color(0xFF2C3B6E), style = Stroke(width = 3f * zoom))

            // 2. Draw River in 3D Perspective (flows across map)
            val riverPath = Path().apply {
                val r1 = p3D(0f, 500f, 0f)
                val r2 = p3D(0f, 580f, 0f)
                val r3 = p3D(1000f, 580f, 0f)
                val r4 = p3D(1000f, 500f, 0f)
                moveTo(r1.x, r1.y)
                lineTo(r2.x, r2.y)
                lineTo(r3.x, r3.y)
                lineTo(r4.x, r4.y)
                close()
            }
            drawPath(riverPath, color = Color(0xFF0D3B66))

            // 3. Draw Lanes (Mid, Top, Bot) with 3D paved stone effect
            // Mid Lane (Diagonal from Blue (100,100) to Red (900,900))
            val mStart = p3D(100f, 100f, 0f)
            val mEnd = p3D(900f, 900f, 0f)
            drawLine(
                color = Color(0xFF2B3A4A),
                start = mStart,
                end = mEnd,
                strokeWidth = 48f * zoom
            )
            drawLine(
                color = Color(0xFF3E5066),
                start = mStart,
                end = mEnd,
                strokeWidth = 36f * zoom
            )

            // Top Lane (Left side in iso)
            val tStart = p3D(100f, 100f, 0f)
            val tCorner = p3D(100f, 900f, 0f)
            val tEnd = p3D(900f, 900f, 0f)
            drawLine(Color(0xFF2B3A4A), tStart, tCorner, 36f * zoom)
            drawLine(Color(0xFF2B3A4A), tCorner, tEnd, 36f * zoom)

            // Bot Lane (Right side in iso)
            val bStart = p3D(100f, 100f, 0f)
            val bCorner = p3D(900f, 100f, 0f)
            val bEnd = p3D(900f, 900f, 0f)
            drawLine(Color(0xFF2B3A4A), bStart, bCorner, 36f * zoom)
            drawLine(Color(0xFF2B3A4A), bCorner, bEnd, 36f * zoom)

            // 4. Draw Base Sanctums (Tế Đàn Xanh & Tế Đàn Đỏ)
            val blueBase = p3D(100f, 100f, 0f)
            val redBase = p3D(900f, 900f, 0f)

            // Blue Runic Base Circle
            drawCircle(Color(0xFF00B0FF).copy(alpha = 0.25f), 65f * zoom, blueBase)
            drawCircle(Color(0xFF00B0FF).copy(alpha = 0.6f), 45f * zoom, blueBase, style = Stroke(2f * zoom))

            // Red Runic Base Circle
            drawCircle(Color(0xFFFF1744).copy(alpha = 0.25f), 65f * zoom, redBase)
            drawCircle(Color(0xFFFF1744).copy(alpha = 0.6f), 45f * zoom, redBase, style = Stroke(2f * zoom))

            // 5. Draw Bushes (Bụi Cỏ) with 3D clusters
            val bushes = listOf(
                Pair(450f, 400f), Pair(550f, 600f), Pair(200f, 500f), Pair(800f, 500f),
                Pair(350f, 350f), Pair(650f, 650f)
            )
            for (bush in bushes) {
                val bPos = p3D(bush.first, bush.second, 0f)
                drawCircle(Color(0xFF1B5E20).copy(alpha = 0.85f), 22f * zoom, bPos)
                drawCircle(Color(0xFF2E7D32), 16f * zoom, Offset(bPos.x, bPos.y - 4f * zoom))
                drawCircle(Color(0xFF4CAF50), 10f * zoom, Offset(bPos.x, bPos.y - 8f * zoom))
            }

            // 6. Draw 3D Defensive Towers
            for (t in towers) {
                if (t.isDestroyed) continue
                val basePos = p3D(t.x, t.y, 0f)
                val topPos = p3D(t.x, t.y, 36f)
                val crystalPos = p3D(t.x, t.y, 48f)
                val towerColor = if (t.team == Team.BLUE) Color(0xFF00B0FF) else Color(0xFFFF1744)

                // Tower shadow on ground
                drawCircle(Color.Black.copy(alpha = 0.45f), 18f * zoom, basePos)

                // 3D Pillar Cylinder (Connecting base to top)
                drawLine(
                    color = Color(0xFF37474F),
                    start = basePos,
                    end = topPos,
                    strokeWidth = 20f * zoom
                )
                drawCircle(Color(0xFF455A64), 14f * zoom, topPos)

                // Floating Glowing Energy Crystal at apex
                drawCircle(towerColor.copy(alpha = 0.4f), 16f * zoom, crystalPos)
                drawCircle(towerColor, 10f * zoom, crystalPos)
                drawCircle(Color.White, 5f * zoom, crystalPos)

                // Tower HP Bar floating in 3D above tower
                val hpPos = p3D(t.x, t.y, 62f)
                val barW = 38f * zoom
                val hpPct = (t.hp / t.maxHp).coerceIn(0f, 1f)
                drawRect(Color.Black, Offset(hpPos.x - barW / 2f, hpPos.y - 3f), Size(barW, 5f * zoom))
                drawRect(towerColor, Offset(hpPos.x - barW / 2f, hpPos.y - 3f), Size(barW * hpPct, 5f * zoom))
            }

            // 7. Draw Jungle Monsters in 3D
            for (jm in jungleMonsters) {
                if (jm.isDead) continue
                val basePos = p3D(jm.x, jm.y, 0f)
                val bodyPos = p3D(jm.x, jm.y, 22f)
                val monsterColor = when {
                    jm.id.contains("blue") -> Color(0xFF2979FF)
                    jm.id.contains("red") -> Color(0xFFFF3D00)
                    jm.id == "dragon" -> Color(0xFFFFD600)
                    else -> Color(0xFFAA00FF)
                }

                // Shadow
                drawCircle(Color.Black.copy(alpha = 0.5f), 16f * zoom, basePos)

                // 3D Golem/Monster Body
                drawCircle(monsterColor.copy(alpha = 0.4f), 20f * zoom, bodyPos)
                drawCircle(monsterColor, 14f * zoom, bodyPos)
                drawCircle(Color.White, 6f * zoom, bodyPos)

                // Name & HP Bar
                val hpPos = p3D(jm.x, jm.y, 38f)
                val barW = 34f * zoom
                val hpPct = (jm.hp / jm.maxHp).coerceIn(0f, 1f)
                drawRect(Color.Black, Offset(hpPos.x - barW / 2f, hpPos.y), Size(barW, 4f * zoom))
                drawRect(monsterColor, Offset(hpPos.x - barW / 2f, hpPos.y), Size(barW * hpPct, 4f * zoom))
            }

            // 8. Draw Minions in 3D
            for (m in minions) {
                if (m.isDead) continue
                val mBase = p3D(m.x, m.y, 0f)
                val mBody = p3D(m.x, m.y, 12f)
                val mColor = if (m.team == Team.BLUE) Color(0xFF40C4FF) else Color(0xFFFF5252)

                drawCircle(Color.Black.copy(alpha = 0.4f), 8f * zoom, mBase)
                drawCircle(mColor, if (m.isRanged) 7f * zoom else 9f * zoom, mBody)

                val hpPos = p3D(m.x, m.y, 20f)
                val hpPct = (m.hp / m.maxHp).coerceIn(0f, 1f)
                drawRect(Color.Black, Offset(hpPos.x - 10f * zoom, hpPos.y), Size(20f * zoom, 2.5f * zoom))
                drawRect(mColor, Offset(hpPos.x - 10f * zoom, hpPos.y), Size(20f * zoom * hpPct, 2.5f * zoom))
            }

            // 9. Draw Projectiles in 3D Space (with arc trajectory)
            for (p in projectiles) {
                val pPos = p3D(p.x, p.y, 16f)
                drawCircle(Color(p.color).copy(alpha = 0.4f), (p.radius + 4f) * zoom, pPos)
                drawCircle(Color(p.color), p.radius * zoom, pPos)
                drawCircle(Color.White, p.radius * 0.4f * zoom, pPos)
            }

            // 10. Draw Champions in 3D (Liên Quân Style with segment health bars)
            for (e in entities) {
                if (e.isDead) continue
                val cBase = p3D(e.x, e.y, 0f)
                val cBody = p3D(e.x, e.y, 18f)
                val teamColor = if (e.team == Team.BLUE) Color(0xFF00B0FF) else Color(0xFFFF1744)

                // Ground Range Circle if Player
                if (e == player) {
                    drawCircle(
                        color = MobaGold.copy(alpha = 0.15f),
                        radius = e.hero.attackRange * zoom * 0.82f,
                        center = cBase
                    )
                    drawCircle(
                        color = MobaGold.copy(alpha = 0.4f),
                        radius = e.hero.attackRange * zoom * 0.82f,
                        center = cBase,
                        style = Stroke(1.5f * zoom)
                    )
                }

                // 3D Drop Shadow
                drawCircle(Color.Black.copy(alpha = 0.55f), 15f * zoom, cBase)

                // 3D Champion Pedestal & Body
                drawCircle(Color(e.hero.themeColorHex).copy(alpha = 0.4f), 22f * zoom, cBody)
                drawCircle(Color(e.hero.themeColorHex), 16f * zoom, cBody)
                drawCircle(
                    color = if (e == player) MobaGold else teamColor,
                    radius = 18f * zoom,
                    center = cBody,
                    style = Stroke(width = if (e == player) 3.5f * zoom else 2f * zoom)
                )

                // 3D Overhead Health Bar & Level Badge
                val headPos = p3D(e.x, e.y, 36f)
                val barW = 46f * zoom
                val barH = 6f * zoom
                val hpPct = (e.hp / e.maxHp).coerceIn(0f, 1f)

                // Level Badge circle on left
                drawCircle(Color(0xFF1E293B), 7f * zoom, Offset(headPos.x - barW / 2f - 9f * zoom, headPos.y + barH / 2f))
                drawCircle(if (e.team == Team.BLUE) Color(0xFF00B0FF) else Color(0xFFFF1744), 6f * zoom, Offset(headPos.x - barW / 2f - 9f * zoom, headPos.y + barH / 2f))

                // HP Bar Background & Fill
                drawRect(Color(0xFF0F172A), Offset(headPos.x - barW / 2f, headPos.y), Size(barW, barH))
                val hpColor = if (e.team == Team.BLUE) Color(0xFF00E676) else Color(0xFFFF1744)
                drawRect(hpColor, Offset(headPos.x - barW / 2f, headPos.y), Size(barW * hpPct, barH))

                // 1000-HP Tick marks (Đặc trưng Liên Quân Mobile!)
                val totalTicks = (e.maxHp / 1000f).toInt()
                if (totalTicks > 1) {
                    val step = barW / (e.maxHp / 1000f)
                    for (k in 1 until totalTicks) {
                        val tickX = headPos.x - barW / 2f + k * step
                        drawLine(Color.Black.copy(alpha = 0.7f), Offset(tickX, headPos.y), Offset(tickX, headPos.y + barH), 1f * zoom)
                    }
                }

                // Mana Bar below HP
                if (e.maxMp > 0f) {
                    val mpPct = (e.mp / e.maxMp).coerceIn(0f, 1f)
                    drawRect(Color(0xFF0D47A1), Offset(headPos.x - barW / 2f, headPos.y + barH + 1f), Size(barW * mpPct, 2.5f * zoom))
                }

                // Draw Champion Name Text
                drawContext.canvas.nativeCanvas.apply {
                    val p = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 22f * zoom
                        isFakeBoldText = true
                        textAlign = android.graphics.Paint.Align.CENTER
                        setShadowLayer(4f, 0f, 1f, android.graphics.Color.BLACK)
                    }
                    drawText(e.name, headPos.x, headPos.y - 4f * zoom, p)

                    // Draw Level number
                    val lp = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 14f * zoom
                        isFakeBoldText = true
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    drawText("${e.level}", headPos.x - barW / 2f - 9f * zoom, headPos.y + barH - 1f, lp)
                }
            }

            // 11. Draw 3D Floating Damage Numbers
            for (ft in floatingTexts) {
                val ftPos = p3D(ft.x, ft.y, 45f)
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.argb(
                            (ft.alpha * 255).toInt(),
                            ((ft.color shr 16) and 0xFF).toInt(),
                            ((ft.color shr 8) and 0xFF).toInt(),
                            (ft.color and 0xFF).toInt()
                        )
                        textSize = 30f * zoom
                        isFakeBoldText = true
                        textAlign = android.graphics.Paint.Align.CENTER
                        setShadowLayer(5f, 0f, 2f, android.graphics.Color.BLACK)
                    }
                    drawText(ft.text, ftPos.x, ftPos.y, paint)
                }
            }
        }

        // Landscape HUD - Top Bar
        LandscapeTopBar(
            blueScore = blueScore,
            redScore = redScore,
            seconds = gameTime.toInt(),
            player = player,
            enemyHeroes = enemyHeroes,
            onOpenShop = { showShopDialog = true },
            onSurrender = onSurrender
        )

        // Landscape Mini-map & Quick Pings (Top-Left)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 44.dp, start = 12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Interactive Mini-map
                LandscapeMiniMap(
                    entities = entities,
                    towers = towers,
                    player = player
                )

                // Quick Ping Column (Liên Quân Style: Tấn công, Rút lui, Tập hợp)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    MiniPingButton("⚔️", "Tấn công") { gameEngine.sendPing("TẤN CÔNG!") }
                    MiniPingButton("🛡️", "Rút lui") { gameEngine.sendPing("RÚT LUI!") }
                    MiniPingButton("🚩", "Tập hợp") { gameEngine.sendPing("TẬP HỢP GIAO TRANH!") }
                }
            }
        }

        // Kill Announcement Banner
        if (killAnnouncements.isNotEmpty()) {
            val latest = killAnnouncements.first()
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 50.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF090D1A).copy(alpha = 0.95f),
                border = androidx.compose.foundation.BorderStroke(2.dp, if (latest.killerTeam == Team.BLUE) MobaBlueTeam else MobaRedTeam)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = latest.title,
                        color = MobaGold,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    )
                    Text(
                        text = latest.message,
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Practice Mode Lab Controls Panel
        if (gameEngine.mode == GameMode.PRACTICE) {
            Surface(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 12.dp),
                shape = RoundedCornerShape(10.dp),
                color = MobaSurface.copy(alpha = 0.92f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MobaHealGreen)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "🛠️ PHÒNG TẬP",
                        color = MobaHealGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                    Button(
                        onClick = { player?.let { it.gold += 1500 } },
                        modifier = Modifier.height(28.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MobaGold, contentColor = Color.Black)
                    ) {
                        Text("+1500 Vàng", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { gameEngine.practiceGodMode = !gameEngine.practiceGodMode },
                        modifier = Modifier.height(28.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (gameEngine.practiceGodMode) MobaHealGreen else MobaSurfaceVariant
                        )
                    ) {
                        Text(if (gameEngine.practiceGodMode) "Bất Tử: BẬT" else "Bất Tử: TẮT", fontSize = 10.sp)
                    }
                    Button(
                        onClick = { gameEngine.practiceNoCooldown = !gameEngine.practiceNoCooldown },
                        modifier = Modifier.height(28.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (gameEngine.practiceNoCooldown) MobaBlueTeam else MobaSurfaceVariant
                        )
                    ) {
                        Text(if (gameEngine.practiceNoCooldown) "Hồi Chiêu 0s" else "Hồi Chiêu Thường", fontSize = 10.sp)
                    }
                }
            }
        }

        // Quick Buy Recommendation Banner (Mid-Left above joystick)
        player?.let { p ->
            val nextRecommended = p.hero.recommendedItems
                .mapNotNull { ItemCatalog.getItemById(it) }
                .firstOrNull { rec -> !p.inventory.any { it.id == rec.id } && p.gold >= rec.cost }

            if (nextRecommended != null && p.inventory.size < 6) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 12.dp, top = 80.dp)
                        .clickable { gameEngine.buyItem(nextRecommended) }
                        .testTag("quick_buy_button"),
                    shape = RoundedCornerShape(10.dp),
                    color = MobaSurface.copy(alpha = 0.95f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MobaGold)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = nextRecommended.iconEmoji, fontSize = 20.sp)
                        Column {
                            Text(
                                text = "MUA: ${nextRecommended.name}",
                                color = MobaGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "${nextRecommended.cost} Vàng",
                                color = TextPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Bottom-Center: Player Status Bar (HP/MP Numbers + Recall & Talent)
        player?.let { p ->
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(12.dp),
                color = MobaDarkBackground.copy(alpha = 0.9f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MobaCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Recall Button
                    MiniActionButton(
                        iconEmoji = "🏰",
                        label = "Biến Về",
                        cooldown = 0f,
                        onClick = { gameEngine.playerStartRecall() },
                        testTag = "recall_button"
                    )

                    // HP & MP Details
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(130.dp)
                    ) {
                        Text(
                            text = "HP ${p.hp.toInt()} / ${p.maxHp.toInt()}",
                            color = MobaHealGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "MP ${p.mp.toInt()} / ${p.maxMp.toInt()}",
                            color = MobaBlueTeam,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }

                    // Talent Spell
                    MiniActionButton(
                        iconEmoji = "⚡",
                        label = "Tốc Biến",
                        cooldown = p.talentCd,
                        onClick = { gameEngine.playerCastTalent() },
                        testTag = "talent_button"
                    )
                }
            }
        }

        // Bottom Touch Controls: Left Joystick, Right Skill Arc
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 12.dp, start = 16.dp, end = 16.dp)
        ) {
            // Virtual Joystick on Bottom-Left
            VirtualJoystick(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .testTag("virtual_joystick"),
                onMove = { vx, vy ->
                    gameEngine.updateJoystick(vx, vy)
                }
            )

            // Classic Liên Quân Skill Cluster on Bottom-Right
            player?.let { p ->
                LienQuanSkillCluster(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    player = p,
                    onBasicAttack = { gameEngine.playerBasicAttack() },
                    onAttackMinion = { gameEngine.playerAttackMinion() },
                    onAttackTower = { gameEngine.playerAttackTower() },
                    onCastSkill1 = { gameEngine.playerCastSkill(1) },
                    onCastSkill2 = { gameEngine.playerCastSkill(2) },
                    onCastSkill3 = { gameEngine.playerCastSkill(3) },
                    onUpgradeSkill = { s -> gameEngine.upgradeSkill(s) }
                )
            }
        }
    }

    // In-game Item Shop Modal Dialog
    if (showShopDialog) {
        InGameShopDialog(
            player = player,
            onBuyItem = { item -> gameEngine.buyItem(item) },
            onDismiss = { showShopDialog = false }
        )
    }
}

@Composable
fun LandscapeTopBar(
    blueScore: Int,
    redScore: Int,
    seconds: Int,
    player: CombatEntity?,
    enemyHeroes: List<CombatEntity>,
    onOpenShop: () -> Unit,
    onSurrender: () -> Unit
) {
    val mins = seconds / 60
    val secs = seconds % 60
    val timeFormatted = String.format("%02d:%02d", mins, secs)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp),
        color = Color(0xFF090D1A).copy(alpha = 0.95f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MobaCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Player Gold & KDA
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MobaSurfaceVariant,
                    modifier = Modifier.clickable { onOpenShop() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("🪙", fontSize = 11.sp)
                        Text(
                            text = "${player?.gold ?: 0}",
                            color = MobaGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }

                Text(
                    text = "${player?.kills ?: 0} / ${player?.deaths ?: 0} / ${player?.assists ?: 0}",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )

                Text(
                    text = "60 FPS • 24ms",
                    color = MobaHealGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Center: Score Capsule
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MobaSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MobaCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "$blueScore",
                        color = MobaBlueTeam,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp
                    )
                    Text(
                        text = timeFormatted,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$redScore",
                        color = MobaRedTeam,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp
                    )
                }
            }

            // Right: Enemy Avatars Status + Shop + Exit
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Enemy Portrait Status Bar (alive or respawn timer)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    enemyHeroes.forEach { enemy ->
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (enemy.isDead) Color.DarkGray else Color(enemy.hero.themeColorHex))
                                .border(1.dp, if (enemy.isDead) Color.Gray else MobaRedTeam, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (enemy.isDead) {
                                Text(
                                    text = "${enemy.respawnTimer.toInt()}",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = enemy.name.take(1).uppercase(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                IconButton(
                    onClick = onOpenShop,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = "Kho Đồ",
                        tint = MobaGold,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onSurrender,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Rời Trận",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LandscapeMiniMap(
    entities: List<CombatEntity>,
    towers: List<Tower>,
    player: CombatEntity?
) {
    Canvas(
        modifier = Modifier
            .size(86.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF090D1A).copy(alpha = 0.88f))
            .border(1.5.dp, MobaCardBorder, RoundedCornerShape(8.dp))
    ) {
        val s = size.width / 1000f

        // Draw Lanes
        drawLine(Color(0xFF334155), Offset(100f * s, 100f * s), Offset(900f * s, 900f * s), 2f)
        drawLine(Color(0xFF334155), Offset(100f * s, 100f * s), Offset(100f * s, 900f * s), 1.5f)
        drawLine(Color(0xFF334155), Offset(100f * s, 900f * s), Offset(900f * s, 900f * s), 1.5f)
        drawLine(Color(0xFF334155), Offset(100f * s, 100f * s), Offset(900f * s, 100f * s), 1.5f)
        drawLine(Color(0xFF334155), Offset(900f * s, 100f * s), Offset(900f * s, 900f * s), 1.5f)

        // Draw Towers
        for (t in towers) {
            if (t.isDestroyed) continue
            val color = if (t.team == Team.BLUE) Color(0xFF00B0FF) else Color(0xFFFF1744)
            drawCircle(color, 2.5f, Offset(t.x * s, t.y * s))
        }

        // Draw Entities
        for (e in entities) {
            if (e.isDead) continue
            val color = if (e == player) MobaGold else if (e.team == Team.BLUE) Color(0xFF00B0FF) else Color(0xFFFF1744)
            val radius = if (e == player) 4.5f else 3f
            drawCircle(color, radius, Offset(e.x * s, e.y * s))
        }
    }
}

@Composable
fun MiniPingButton(iconEmoji: String, label: String, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = MobaSurface.copy(alpha = 0.9f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MobaGoldDark),
        modifier = Modifier
            .size(26.dp)
            .clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = iconEmoji, fontSize = 12.sp)
        }
    }
}

@Composable
fun MiniActionButton(
    iconEmoji: String,
    label: String,
    cooldown: Float,
    onClick: () -> Unit,
    testTag: String
) {
    val isOnCooldown = cooldown > 0.05f

    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(MobaSurface)
            .border(1.dp, MobaGoldDark, CircleShape)
            .clickable(enabled = !isOnCooldown) { onClick() }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = iconEmoji, fontSize = 13.sp)
            Text(text = label, color = TextSecondary, fontSize = 7.sp)
        }
        if (isOnCooldown) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${cooldown.toInt()}s",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Classic Liên Quân Skill Cluster (Main Attack + Tower + Minion buttons + 3 Skill Arc)
@Composable
fun LienQuanSkillCluster(
    modifier: Modifier = Modifier,
    player: CombatEntity,
    onBasicAttack: () -> Unit,
    onAttackMinion: () -> Unit,
    onAttackTower: () -> Unit,
    onCastSkill1: () -> Unit,
    onCastSkill2: () -> Unit,
    onCastSkill3: () -> Unit,
    onUpgradeSkill: (Int) -> Unit
) {
    Box(
        modifier = modifier.size(240.dp, 210.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        // 1. Tower Attack Button (Above Main Attack)
        TargetSubButton(
            modifier = Modifier.offset(x = (-30).dp, y = (-85).dp),
            iconEmoji = "🏰",
            label = "Trụ",
            onClick = onAttackTower,
            testTag = "target_tower_button"
        )

        // 2. Minion Attack Button (Below Main Attack)
        TargetSubButton(
            modifier = Modifier.offset(x = (-95).dp, y = (-10).dp),
            iconEmoji = "🗡️",
            label = "Lính",
            onClick = onAttackMinion,
            testTag = "target_minion_button"
        )

        // 3. Main Basic Attack Button (Large Center)
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(MobaGoldBright, MobaGoldDark)
                    )
                )
                .border(2.5.dp, MobaGold, CircleShape)
                .clickable { onBasicAttack() }
                .testTag("basic_attack_button"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "⚔️", fontSize = 28.sp)
                Text(
                    text = "ĐÁNH",
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 9.sp
                )
            }
        }

        // 4. Chiêu 1 (Q) with Level Up (+) Button
        SkillWithUpgradeButton(
            modifier = Modifier.offset(x = (-135).dp, y = (-75).dp),
            skill = player.hero.skill1,
            cooldown = player.skill1Cd,
            skillLevel = player.skill1Level,
            canUpgrade = player.availableSkillPoints > 0,
            onCast = onCastSkill1,
            onUpgrade = { onUpgradeSkill(1) },
            testTag = "skill_1_button"
        )

        // 5. Chiêu 2 (W) with Level Up (+) Button
        SkillWithUpgradeButton(
            modifier = Modifier.offset(x = (-100).dp, y = (-135).dp),
            skill = player.hero.skill2,
            cooldown = player.skill2Cd,
            skillLevel = player.skill2Level,
            canUpgrade = player.availableSkillPoints > 0,
            onCast = onCastSkill2,
            onUpgrade = { onUpgradeSkill(2) },
            testTag = "skill_2_button"
        )

        // 6. Chiêu 3 / Chiêu Cuối Ultimate (R) with Level Up (+) Button
        SkillWithUpgradeButton(
            modifier = Modifier.offset(x = (-30).dp, y = (-150).dp),
            skill = player.hero.skill3,
            cooldown = player.skill3Cd,
            skillLevel = player.skill3Level,
            isUltimate = true,
            canUpgrade = player.availableSkillPoints > 0,
            onCast = onCastSkill3,
            onUpgrade = { onUpgradeSkill(3) },
            testTag = "skill_3_button"
        )
    }
}

@Composable
fun TargetSubButton(
    modifier: Modifier = Modifier,
    iconEmoji: String,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(MobaSurface)
            .border(1.5.dp, MobaGoldDark, CircleShape)
            .clickable { onClick() }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = iconEmoji, fontSize = 14.sp)
            Text(text = label, color = TextPrimary, fontSize = 7.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SkillWithUpgradeButton(
    modifier: Modifier = Modifier,
    skill: HeroSkill,
    cooldown: Float,
    skillLevel: Int,
    isUltimate: Boolean = false,
    canUpgrade: Boolean,
    onCast: () -> Unit,
    onUpgrade: () -> Unit,
    testTag: String
) {
    val isLocked = skillLevel <= 0
    val isOnCooldown = cooldown > 0.05f

    Box(modifier = modifier) {
        // Upgrade (+) Button Glowing Above Skill
        if (canUpgrade) {
            Surface(
                shape = CircleShape,
                color = MobaGold,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White),
                modifier = Modifier
                    .offset(x = 14.dp, y = (-14).dp)
                    .size(26.dp)
                    .clickable { onUpgrade() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("+", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 16.sp)
                }
            }
        }

        // Skill Button
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    if (isLocked) Color(0xFF1E293B).copy(alpha = 0.85f)
                    else if (isUltimate) MobaRedTeam
                    else MobaSurfaceVariant
                )
                .border(
                    width = if (isUltimate) 2.dp else 1.5.dp,
                    color = if (isLocked) Color(0xFF475569) else if (isUltimate) MobaGold else MobaBlueTeam,
                    shape = CircleShape
                )
                .clickable(enabled = !isLocked && !isOnCooldown) { onCast() }
                .testTag(testTag),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isLocked) "🔒" else skill.iconSymbol,
                    fontSize = 18.sp
                )
                Text(
                    text = if (isLocked) "KHÓA" else if (isUltimate) "ULTI" else skill.name.take(4),
                    color = if (isLocked) TextSecondary else TextPrimary,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

            if (isOnCooldown && !isLocked) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.75f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${cooldown.toInt() + 1}s",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Skill Level Pips below button
        if (skillLevel > 0) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 4.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(2.dp)
            ) {
                val maxPips = if (isUltimate) 3 else 6
                for (p in 1..maxPips) {
                    Box(
                        modifier = Modifier
                            .size(3.5.dp)
                            .clip(CircleShape)
                            .background(if (p <= skillLevel) MobaGold else Color(0xFF475569))
                    )
                }
            }
        }
    }
}

@Composable
fun VirtualJoystick(
    modifier: Modifier = Modifier,
    onMove: (Float, Float) -> Unit
) {
    var thumbOffset by remember { mutableStateOf(Offset.Zero) }
    val maxRadius = 45.dp

    Box(
        modifier = modifier
            .size(115.dp)
            .clip(CircleShape)
            .background(MobaSurface.copy(alpha = 0.5f))
            .border(1.5.dp, MobaBlueTeam.copy(alpha = 0.4f), CircleShape)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { },
                    onDragEnd = {
                        thumbOffset = Offset.Zero
                        onMove(0f, 0f)
                    },
                    onDragCancel = {
                        thumbOffset = Offset.Zero
                        onMove(0f, 0f)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val newOffset = thumbOffset + dragAmount
                        val dist = hypot(newOffset.x, newOffset.y)
                        val maxPx = maxRadius.toPx()
                        val clampedOffset = if (dist > maxPx) {
                            Offset((newOffset.x / dist) * maxPx, (newOffset.y / dist) * maxPx)
                        } else {
                            newOffset
                        }
                        thumbOffset = clampedOffset
                        val normX = (clampedOffset.x / maxPx).coerceIn(-1f, 1f)
                        val normY = (clampedOffset.y / maxPx).coerceIn(-1f, 1f)
                        onMove(normX, normY)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(thumbOffset.x.roundToInt(), thumbOffset.y.roundToInt()) }
                .size(46.dp)
                .clip(CircleShape)
                .background(MobaBlueTeam.copy(alpha = 0.85f))
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Games,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
fun InGameShopDialog(
    player: CombatEntity?,
    onBuyItem: (MobaItem) -> Boolean,
    onDismiss: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(ItemCategory.CONG) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.88f),
            shape = RoundedCornerShape(14.dp),
            color = MobaDarkBackground,
            border = androidx.compose.foundation.BorderStroke(1.dp, MobaGold)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🪙", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${player?.gold ?: 0} Vàng",
                            color = MobaGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Đóng", tint = TextPrimary, modifier = Modifier.size(18.dp))
                    }
                }

                // Inventory slots
                Text(
                    text = "Trang bị hiện tại (${player?.inventory?.size ?: 0}/6):",
                    color = TextSecondary,
                    fontSize = 10.sp
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    for (i in 0 until 6) {
                        val itm = player?.inventory?.getOrNull(i)
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MobaSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (itm != null) MobaGold else MobaCardBorder),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (itm != null) {
                                    Text(text = itm.iconEmoji, fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }

                Divider(color = MobaCardBorder, modifier = Modifier.padding(vertical = 4.dp))

                // Category Tabs
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(ItemCategory.CONG, ItemCategory.PHEP, ItemCategory.THU, ItemCategory.TOC_CHAY).forEach { cat ->
                        val isSel = selectedCategory == cat
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSel) MobaGold else MobaSurfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedCategory = cat }
                        ) {
                            Text(
                                text = cat.displayName,
                                color = if (isSel) Color.Black else TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Items list
                val itemsInCat = ItemCatalog.items.filter { it.category == selectedCategory }
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(itemsInCat) { item ->
                        val canAfford = (player?.gold ?: 0) >= item.cost
                        val isOwned = player?.inventory?.any { it.id == item.id } == true

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MobaSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MobaCardBorder)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = item.iconEmoji, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.name,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = item.passiveDesc,
                                        color = TextSecondary,
                                        fontSize = 9.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Button(
                                    onClick = { onBuyItem(item) },
                                    enabled = canAfford && !isOwned && (player?.inventory?.size ?: 0) < 6,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MobaGold,
                                        contentColor = Color.Black,
                                        disabledContainerColor = MobaSurfaceVariant,
                                        disabledContentColor = TextSecondary
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text(
                                        text = if (isOwned) "Đã mua" else "${item.cost} V",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
