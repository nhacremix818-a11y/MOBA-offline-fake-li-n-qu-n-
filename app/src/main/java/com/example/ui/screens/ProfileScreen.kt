package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HeroCatalog
import com.example.data.PlayerProfileManager
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileManager: PlayerProfileManager,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var showEditNameDialog by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf(profileManager.playerName) }

    val currentRank = profileManager.currentRank
    val matchHistory = remember { profileManager.getMatchHistory() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MobaDarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "HỒ SƠ KIỆN TƯỚNG",
                        color = MobaGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("profile_back_button")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay Lại",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MobaSurface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Rank & Profile Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MobaSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MobaGold.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(
                                            Color(currentRank.badgeColor),
                                            MobaDarkBackground
                                        )
                                    )
                                )
                                .border(2.dp, MobaGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👑", fontSize = 36.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = profileManager.playerName,
                                color = TextPrimary,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                            IconButton(
                                onClick = {
                                    tempName = profileManager.playerName
                                    showEditNameDialog = true
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Đổi tên", tint = MobaGold, modifier = Modifier.size(16.dp))
                            }
                        }

                        Text(
                            text = currentRank.title,
                            color = Color(currentRank.badgeColor),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            val totalStars = profileManager.stars.coerceAtLeast(1)
                            for (i in 0 until totalStars) {
                                Text("⭐", fontSize = 14.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Divider(color = MobaCardBorder)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            StatItem(
                                label = "TỔNG TRẬN",
                                value = "${profileManager.totalMatches}",
                                color = TextPrimary
                            )
                            StatItem(
                                label = "TỈ LỆ THẮNG",
                                value = "${if (profileManager.totalMatches > 0) (profileManager.totalWins * 100 / profileManager.totalMatches) else 0}%",
                                color = MobaGold
                            )
                            StatItem(
                                label = "MVP",
                                value = "${profileManager.totalMvp}",
                                color = MobaBlueTeam
                            )
                            StatItem(
                                label = "MEGA KILL",
                                value = "${profileManager.totalMegaKills}",
                                color = MobaRedTeam
                            )
                        }
                    }
                }
            }

            // Match History Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = MobaGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LỊCH SỬ ĐẤU GẦN ĐÂY",
                        color = MobaGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            // History Records
            items(matchHistory) { record ->
                val hero = HeroCatalog.getHeroById(record.heroId)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MobaSurface),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (record.isVictory) MobaBlueTeam.copy(alpha = 0.5f) else MobaRedTeam.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Victory / Defeat tag
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (record.isVictory) MobaBlueTeam else MobaRedTeam,
                            modifier = Modifier.width(60.dp)
                        ) {
                            Text(
                                text = if (record.isVictory) "THẮNG" else "BẠI",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(hero.themeColorHex)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = hero.name.take(2).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = record.heroName,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                if (record.isMvp) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = MobaGold
                                    ) {
                                        Text(
                                            text = "MVP",
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = record.gameMode,
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${record.kills} / ${record.deaths} / ${record.assists}",
                                color = MobaGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "+${record.goldEarned} Vàng",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            containerColor = MobaSurface,
            title = { Text("Đổi Tên Kiện Tướng", color = MobaGold) },
            text = {
                OutlinedTextField(
                    value = tempName,
                    onValueChange = { tempName = it },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MobaGold,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (tempName.isNotBlank()) {
                            profileManager.playerName = tempName.trim()
                        }
                        showEditNameDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MobaGold, contentColor = Color.Black)
                ) {
                    Text("Lưu", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog = false }) {
                    Text("Hủy", color = TextSecondary)
                }
            }
        )
    }
}
