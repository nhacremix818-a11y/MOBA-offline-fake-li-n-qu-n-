package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.PlayerProfileManager
import com.example.model.GameMode
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    profileManager: PlayerProfileManager,
    onSelectMode: (GameMode) -> Unit,
    onOpenHeroCodex: () -> Unit,
    onOpenItemShop: () -> Unit,
    onOpenProfile: () -> Unit
) {
    val currentRank = profileManager.currentRank

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MobaDarkBackground,
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                color = MobaSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MobaCardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left Brand: Logo + Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MobaSurfaceVariant)
                                .border(1.dp, MobaGold, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⚔️", fontSize = 18.sp)
                        }
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "LIÊN QUÂN OFFLINE",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MobaGold
                                ) {
                                    Text(
                                        text = "3D NGANG",
                                        color = Color.Black,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Bình Nguyên Vô Tận 3D • 20 Tướng Đầy Đủ",
                                color = TextSecondary,
                                fontSize = 9.sp
                            )
                        }
                    }

                    // Center: Player Profile Chip
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MobaSurfaceVariant)
                            .clickable { onOpenProfile() }
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("👑", fontSize = 14.sp)
                        Text(
                            text = profileManager.playerName,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(currentRank.badgeColor).copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "${currentRank.title} ★".repeat(profileManager.stars.coerceAtLeast(1)),
                                color = Color(currentRank.badgeColor),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Right: Gold + Nav Shortcuts
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Gold
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MobaSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MobaGoldDark)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("🪙", fontSize = 12.sp)
                                Text(
                                    text = "${profileManager.goldCoins}",
                                    color = MobaGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Nav Buttons
                        LandscapeNavButton(
                            icon = Icons.Default.Groups,
                            label = "Tướng",
                            onClick = onOpenHeroCodex,
                            testTag = "nav_hero_codex"
                        )
                        LandscapeNavButton(
                            icon = Icons.Default.Shield,
                            label = "Trang Bị",
                            onClick = onOpenItemShop,
                            testTag = "nav_item_shop"
                        )
                        LandscapeNavButton(
                            icon = Icons.Default.Person,
                            label = "Hồ Sơ",
                            onClick = onOpenProfile,
                            testTag = "nav_profile"
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // Widescreen 2-Column Layout
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left Column (Featured Banner + Stats)
            Column(
                modifier = Modifier
                    .weight(1.1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Banner Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MobaCardBorder)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_arena_hero),
                            contentDescription = "Bình Nguyên Vô Tận",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Transparent,
                                            Color(0xFF090D1A).copy(alpha = 0.9f)
                                        )
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MobaRedTeam
                            ) {
                                Text(
                                    text = "3D ISOMETRIC MOBA OFFLINE",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "BÌNH NGUYÊN VÔ TẬN 3D",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Góc nhìn 3D chuẩn Liên Quân • 20 Tướng • 3 Đường & Quái Rừng",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                // Stats Bar
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MobaSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MobaCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatItem(
                            label = "Tổng Trận",
                            value = "${profileManager.totalMatches}",
                            color = TextPrimary
                        )
                        StatItem(
                            label = "Tỉ Lệ Thắng",
                            value = "${if (profileManager.totalMatches > 0) (profileManager.totalWins * 100 / profileManager.totalMatches) else 0}%",
                            color = MobaGold
                        )
                        StatItem(
                            label = "MVP Trận",
                            value = "${profileManager.totalMvp}",
                            color = MobaBlueTeam
                        )
                        StatItem(
                            label = "Mega Kill",
                            value = "${profileManager.totalMegaKills}",
                            color = MobaRedTeam
                        )
                    }
                }
            }

            // Right Column (Game Mode Cards)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.SportsEsports,
                        contentDescription = null,
                        tint = MobaGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CHỌN CHẾ ĐỘ CHIẾN ĐẤU 3D",
                        color = MobaGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                // Mode 1: 5v5
                LandscapeGameModeCard(
                    title = "5v5 Bình Nguyên Vô Tận 3D",
                    subtitle = "3 đường, 9 Bot AI, rồng ánh sáng & bùa Caesar",
                    tag = "CHẾ ĐỘ CHÍNH",
                    tagColor = MobaBlueTeam,
                    iconEmoji = "⚔️",
                    testTag = "mode_5v5",
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectMode(GameMode.FIVE_VS_FIVE) }
                )

                // Mode 2: 1v1
                LandscapeGameModeCard(
                    title = "1v1 Cầu Độc Đạo 3D",
                    subtitle = "Đấu trường đơn solo kỹ năng tay đôi",
                    tag = "SOLO ĐỈNH CAO",
                    tagColor = MobaRedTeam,
                    iconEmoji = "🔥",
                    testTag = "mode_1v1",
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectMode(GameMode.ONE_VS_ONE) }
                )

                // Mode 3: Practice
                LandscapeGameModeCard(
                    title = "Phòng Tập Luyện Tướng 3D",
                    subtitle = "Bất tử, hồi chiêu 0s, thử combo mọi tướng",
                    tag = "LUYỆN TẬP",
                    tagColor = MobaHealGreen,
                    iconEmoji = "🧪",
                    testTag = "mode_practice",
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectMode(GameMode.PRACTICE) }
                )
            }
        }
    }
}

@Composable
fun LandscapeNavButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MobaSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, MobaCardBorder),
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = label, tint = MobaGold, modifier = Modifier.size(15.dp))
            Text(label, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun LandscapeGameModeCard(
    title: String,
    subtitle: String,
    tag: String,
    tagColor: Color,
    iconEmoji: String,
    testTag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MobaSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MobaCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MobaSurfaceVariant)
                    .border(1.dp, tagColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = iconEmoji, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = tagColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = tag,
                        color = tagColor,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 9.sp,
                    maxLines = 1
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MobaGold,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
