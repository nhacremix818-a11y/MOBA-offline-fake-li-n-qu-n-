package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ItemCatalog
import com.example.model.ItemCategory
import com.example.model.MobaItem
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemShopCodexScreen(
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var selectedCategory by remember { mutableStateOf<ItemCategory?>(null) }

    val filteredItems = remember(selectedCategory) {
        if (selectedCategory == null) {
            ItemCatalog.items
        } else {
            ItemCatalog.items.filter { it.category == selectedCategory }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MobaDarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "KHO TRANG BỊ LIÊN QUÂN (${ItemCatalog.items.size})",
                        color = MobaGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("item_shop_back_button")
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

            // Category Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("Tất cả") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MobaGold,
                            selectedLabelColor = Color.Black,
                            containerColor = MobaSurfaceVariant,
                            labelColor = TextPrimary
                        )
                    )
                }
                items(ItemCategory.values()) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat.displayName) },
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

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
                                    .size(46.dp)
                                    .background(MobaSurfaceVariant, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = item.iconEmoji, fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.name,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MobaSurfaceVariant
                                    ) {
                                        Text(
                                            text = "${item.cost} Vàng",
                                            color = MobaGold,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = item.passiveDesc,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                                // Stat bonuses
                                val bonuses = mutableListOf<String>()
                                if (item.attackBonus > 0f) bonuses.add("+${item.attackBonus.toInt()} Công")
                                if (item.magicBonus > 0f) bonuses.add("+${item.magicBonus.toInt()} Công Phép")
                                if (item.hpBonus > 0f) bonuses.add("+${item.hpBonus.toInt()} Máu")
                                if (item.armorBonus > 0f) bonuses.add("+${item.armorBonus.toInt()} Giáp")
                                if (item.magicResistBonus > 0f) bonuses.add("+${item.magicResistBonus.toInt()} Giáp Phép")
                                if (item.cooldownReduction > 0f) bonuses.add("+${(item.cooldownReduction * 100).toInt()}% Giảm Hồi Chiêu")
                                if (item.critChance > 0f) bonuses.add("+${(item.critChance * 100).toInt()}% Chí Mạng")

                                if (bonuses.isNotEmpty()) {
                                    Text(
                                        text = bonuses.joinToString(" • "),
                                        color = MobaBlueTeam,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
