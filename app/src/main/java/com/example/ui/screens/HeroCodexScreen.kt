package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HeroCatalog
import com.example.data.ItemCatalog
import com.example.model.Hero
import com.example.model.HeroRole
import com.example.model.HeroSkill
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroCodexScreen(
    onBack: () -> Unit,
    onSelectHeroForBattle: (Hero) -> Unit
) {
    BackHandler { onBack() }

    var searchQuery by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<HeroRole?>(null) }
    var inspectedHero by remember { mutableStateOf<Hero?>(null) }

    val filteredHeroes = remember(searchQuery, selectedRole) {
        HeroCatalog.heroes.filter { hero ->
            val matchesRole = selectedRole == null || hero.role == selectedRole
            val matchesSearch = searchQuery.isBlank() ||
                    hero.name.contains(searchQuery, ignoreCase = true) ||
                    hero.title.contains(searchQuery, ignoreCase = true)
            matchesRole && matchesSearch
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MobaDarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CẨM NANG TƯỚNG LIÊN QUÂN (${HeroCatalog.heroes.size})",
                        color = MobaGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("hero_codex_back_button")
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Tìm kiếm tên tướng hoặc danh hiệu...", color = TextSecondary, fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MobaGold) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_search_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MobaGold,
                    unfocusedBorderColor = MobaCardBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = MobaSurface,
                    unfocusedContainerColor = MobaSurface
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Role Filters
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedRole == null,
                        onClick = { selectedRole = null },
                        label = { Text("Tất cả") },
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
                        label = { Text(role.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MobaGold,
                            selectedLabelColor = Color.Black,
                            containerColor = MobaSurfaceVariant,
                            labelColor = TextPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Heroes List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredHeroes) { hero ->
                    HeroCodexCard(
                        hero = hero,
                        onClick = { inspectedHero = hero }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // Hero Detail Modal BottomSheet / Dialog
    inspectedHero?.let { hero ->
        HeroDetailDialog(
            hero = hero,
            onDismiss = { inspectedHero = null },
            onSelectHero = {
                inspectedHero = null
                onSelectHeroForBattle(hero)
            }
        )
    }
}

@Composable
fun HeroCodexCard(
    hero: Hero,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("hero_card_${hero.id}"),
        colors = CardDefaults.cardColors(containerColor = MobaSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MobaCardBorder),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(hero.themeColorHex)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = hero.name.take(2).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = hero.name,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MobaSurfaceVariant
                    ) {
                        Text(
                            text = hero.role.displayName,
                            color = MobaGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = hero.title,
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Text(
                    text = "Độ khó: ${"★".repeat(hero.difficulty)} • ${hero.damageType.displayName}",
                    color = TextTertiary,
                    fontSize = 10.sp
                )
            }

            Text(
                text = "Xem chi tiết →",
                color = MobaGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun HeroDetailDialog(
    hero: Hero,
    onDismiss: () -> Unit,
    onSelectHero: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onSelectHero,
                colors = ButtonDefaults.buttonColors(containerColor = MobaGold, contentColor = Color.Black),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("choose_hero_confirm_button")
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("CHỌN ĐẤU NGAY", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng", color = TextSecondary)
            }
        },
        containerColor = MobaSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "${hero.name} - ${hero.title}",
                        color = MobaGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${hero.role.displayName} • ${hero.damageType.displayName}",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Quote & Lore
                if (hero.quote.isNotBlank()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MobaSurfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "💬 \"${hero.quote}\"",
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

                // Skills Breakdown
                item {
                    Text("BỘ KỸ NĂNG CHIẾN ĐẤU", color = MobaGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                item { SkillDetailRow("Nội tại: ${hero.passive.name}", hero.passive.iconSymbol, hero.passive.description, "0s") }
                item { SkillDetailRow("Chiêu 1: ${hero.skill1.name}", hero.skill1.iconSymbol, hero.skill1.description, "${hero.skill1.cooldown}s") }
                item { SkillDetailRow("Chiêu 2: ${hero.skill2.name}", hero.skill2.iconSymbol, hero.skill2.description, "${hero.skill2.cooldown}s") }
                item { SkillDetailRow("Chiêu Cuối: ${hero.skill3.name}", hero.skill3.iconSymbol, hero.skill3.description, "${hero.skill3.cooldown}s") }

                // Combo Guide
                if (hero.comboGuide.isNotBlank()) {
                    item {
                        Text("MẸO COMBO CHUẨN", color = MobaBlueTeam, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(hero.comboGuide, color = TextPrimary, fontSize = 11.sp)
                    }
                }

                // Recommended Items
                if (hero.recommendedItems.isNotEmpty()) {
                    item {
                        Text("TRANG BỊ KHUYÊN DÙNG", color = MobaGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            hero.recommendedItems.mapNotNull { ItemCatalog.getItemById(it) }.forEach { itm ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MobaSurfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, MobaCardBorder)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(4.dp)
                                    ) {
                                        Text(text = itm.iconEmoji, fontSize = 16.sp)
                                        Text(text = itm.name.take(6), color = TextSecondary, fontSize = 8.sp, maxLines = 1)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SkillDetailRow(title: String, icon: String, desc: String, cd: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MobaDarkBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, MobaCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = icon, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = title, color = MobaGold, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Text(text = "Hồi chiêu: $cd", color = TextSecondary, fontSize = 9.sp)
                }
                Text(text = desc, color = TextPrimary, fontSize = 10.sp)
            }
        }
    }
}
