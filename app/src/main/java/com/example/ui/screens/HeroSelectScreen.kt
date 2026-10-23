package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
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
import com.example.model.GameMode
import com.example.model.Hero
import com.example.model.HeroRole
import com.example.ui.theme.*

@Composable
fun HeroSelectScreen(
    gameMode: GameMode,
    onBack: () -> Unit,
    onStartBattle: (Hero, String) -> Unit
) {
    BackHandler { onBack() }

    var selectedRole by remember { mutableStateOf<HeroRole?>(null) }
    var selectedHero by remember { mutableStateOf(HeroCatalog.heroes.first()) }
    var selectedTalent by remember { mutableStateOf("Tốc Biến") }

    val filteredHeroes = remember(selectedRole) {
        if (selectedRole == null) {
            HeroCatalog.heroes
        } else {
            HeroCatalog.heroes.filter { it.role == selectedRole }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MobaDarkBackground,
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                color = MobaSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MobaCardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Back button + Title + Mode
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("hero_select_back_button")
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Quay Lại",
                                tint = TextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "CHỌN TƯỚNG LIÊN QUÂN",
                            color = MobaGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MobaSurfaceVariant
                        ) {
                            Text(
                                text = gameMode.title,
                                color = TextSecondary,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Right: Primary Top Quick-Start Button (Always visible on top bar!)
                    Button(
                        onClick = { onStartBattle(selectedHero, selectedTalent) },
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("top_start_battle_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MobaGold,
                            contentColor = Color.Black
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "BẮT ĐẦU NGAY",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // Widescreen Split Layout: Left Panel & Right Roster Grid
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Left Card: Hero Spotlight & Pinned "BẮT ĐẦU" Button
            Card(
                modifier = Modifier
                    .weight(1.1f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = MobaSurface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, MobaGold.copy(alpha = 0.7f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    // Scrollable Hero details so it never overflows even on low-resolution screens
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Hero Header
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                Color(selectedHero.themeColorHex),
                                                Color.Black
                                            )
                                        )
                                    )
                                    .border(2.dp, MobaGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = selectedHero.name.take(2).uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = selectedHero.name,
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = MobaGold.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = selectedHero.role.displayName,
                                            color = MobaGold,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "${selectedHero.title} • ${selectedHero.damageType.displayName}",
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "Độ khó: ${"★".repeat(selectedHero.difficulty)}",
                                    color = MobaGoldBright,
                                    fontSize = 9.sp
                                )
                            }
                        }

                        Divider(color = MobaCardBorder, modifier = Modifier.padding(vertical = 2.dp))

                        // Skills Row
                        Text("KỸ NĂNG:", color = MobaGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SkillMiniChip("Nội Tại", selectedHero.passive.iconSymbol, selectedHero.passive.name)
                            SkillMiniChip("Chiêu 1", selectedHero.skill1.iconSymbol, selectedHero.skill1.name)
                            SkillMiniChip("Chiêu 2", selectedHero.skill2.iconSymbol, selectedHero.skill2.name)
                            SkillMiniChip("Chiêu Cuối", selectedHero.skill3.iconSymbol, selectedHero.skill3.name)
                        }

                        // Talent Selection Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("Bổ trợ:", color = TextSecondary, fontSize = 9.sp)
                            listOf("Tốc Biến", "Bộc Phá", "Trừng Trị", "Cấp Cứu").forEach { talent ->
                                val isSel = selectedTalent == talent
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (isSel) MobaGold else MobaSurfaceVariant,
                                    modifier = Modifier.clickable { selectedTalent = talent }
                                ) {
                                    Text(
                                        text = talent,
                                        color = if (isSel) Color.Black else TextPrimary,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Pinned Main Action Button: "BẮT ĐẦU TRẬN ĐẤU" (Always 100% visible at bottom of card!)
                    Button(
                        onClick = { onStartBattle(selectedHero, selectedTalent) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("start_battle_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MobaGold,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "BẮT ĐẦU (${selectedHero.name.uppercase()})",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Right Column: Role Filters & Champion Grid
            Column(
                modifier = Modifier
                    .weight(1.3f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Role Tabs
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = selectedRole == null,
                            onClick = { selectedRole = null },
                            label = { Text("Tất cả (${HeroCatalog.heroes.size})", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MobaGold,
                                selectedLabelColor = Color.Black,
                                containerColor = MobaSurfaceVariant,
                                labelColor = TextPrimary
                            )
                        )
                    }
                    items(HeroRole.values()) { role ->
                        FilterChip(
                            selected = selectedRole == role,
                            onClick = { selectedRole = role },
                            label = { Text(role.displayName, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MobaGold,
                                selectedLabelColor = Color.Black,
                                containerColor = MobaSurfaceVariant,
                                labelColor = TextPrimary
                            )
                        )
                    }
                }

                // Heroes Grid
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 65.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("hero_grid")
                ) {
                    items(filteredHeroes) { hero ->
                        val isSelected = hero.id == selectedHero.id
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) MobaSurfaceVariant else MobaSurface)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) MobaGold else MobaCardBorder,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedHero = hero }
                                .padding(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(hero.themeColorHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = hero.name.take(2).uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = hero.name,
                                color = if (isSelected) MobaGold else TextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                            Text(
                                text = hero.role.shortName,
                                color = TextSecondary,
                                fontSize = 8.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
