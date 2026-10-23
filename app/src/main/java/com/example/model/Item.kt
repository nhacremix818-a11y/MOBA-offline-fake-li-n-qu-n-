package com.example.model

enum class ItemCategory(val displayName: String) {
    CONG("Công"),
    PHEP("Phép"),
    THU("Thủ"),
    TOC_CHAY("Tốc Chạy"),
    TRO_THU("Trợ Thủ"),
    RUNG("Đi Rừng")
}

data class MobaItem(
    val id: String,
    val name: String,
    val category: ItemCategory,
    val cost: Int,
    val attackBonus: Float = 0f,
    val magicBonus: Float = 0f,
    val hpBonus: Float = 0f,
    val armorBonus: Float = 0f,
    val magicResistBonus: Float = 0f,
    val cooldownReduction: Float = 0f, // 0.10 = 10%
    val critChance: Float = 0f,
    val attackSpeed: Float = 0f,
    val moveSpeedBonus: Float = 0f,
    val passiveDesc: String,
    val iconEmoji: String = "🗡️"
)
