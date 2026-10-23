package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Hero
import com.example.ui.theme.*

@Composable
fun VictoryDefeatScreen(
    isVictory: Boolean,
    hero: Hero,
    kills: Int,
    deaths: Int,
    assists: Int,
    goldEarned: Int,
    onPlayAgain: () -> Unit,
    onGoHome: () -> Unit
) {
    BackHandler { onGoHome() }

    val bannerColor = if (isVictory) MobaGold else MobaRedTeam
    val titleText = if (isVictory) "CHIẾN THẮNG" else "THẤT BẠI"
    val subtitleText = if (isVictory) "Thắng Bại Tại Kỹ Năng!" else "Đừng nản chí! Luyện tập để trở lại mạnh mẽ hơn."
    val isMvp = isVictory && kills >= 3

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MobaDarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Left Column: Result Trophy & Titles
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(bannerColor.copy(alpha = 0.8f), MobaDarkBackground)
                            )
                        )
                        .border(2.dp, bannerColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isVictory) "🏆" else "💀",
                        fontSize = 32.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = titleText,
                    color = bannerColor,
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                    letterSpacing = 1.sp
                )

                Text(
                    text = subtitleText,
                    color = TextSecondary,
                    fontSize = 11.sp
                )

                if (isMvp) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MobaGold,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = "MVP", tint = Color.Black, modifier = Modifier.size(14.dp))
                            Text(
                                text = "DANH HIỆU MVP TRẬN ĐẤU",
                                color = Color.Black,
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            // Right Column: Stats Card & Action Buttons
            Column(
                modifier = Modifier.weight(1.3f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MobaSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MobaCardBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(hero.themeColorHex)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = hero.name.take(2).uppercase(),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = hero.name,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = hero.title,
                                        color = TextSecondary,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MobaSurfaceVariant
                            ) {
                                Text(
                                    text = if (isVictory) "+1 ⭐ SAO" else "-1 ⭐ SAO",
                                    color = if (isVictory) MobaGold else MobaRedTeam,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Divider(color = MobaCardBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            StatItem(label = "HẠ GỤC", value = "$kills", color = MobaBlueTeam)
                            StatItem(label = "BỊ HẠ", value = "$deaths", color = MobaRedTeam)
                            StatItem(label = "HỖ TRỢ", value = "$assists", color = MobaHealGreen)
                            StatItem(label = "THƯỞNG", value = "+$goldEarned V", color = MobaGold)
                        }
                    }
                }

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onPlayAgain,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .testTag("play_again_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MobaGold, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("CHƠI LẠI", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onGoHome,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .testTag("go_home_button"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MobaCardBorder),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("VỀ SẢNH", fontWeight = FontWeight.Medium, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
