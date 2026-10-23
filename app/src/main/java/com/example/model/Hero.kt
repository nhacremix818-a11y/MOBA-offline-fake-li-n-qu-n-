package com.example.model

enum class HeroRole(val displayName: String, val shortName: String) {
    DAU_SI("Đấu Sĩ", "Fighter"),
    PHAP_SU("Pháp Sư", "Mage"),
    SAT_THU("Sát Thủ", "Assassin"),
    XA_THU("Xạ Thủ", "Marksman"),
    TRO_THU("Trợ Thủ", "Support"),
    DO_DON("Đỡ Đòn", "Tank")
}

enum class DamageType(val displayName: String) {
    VAT_LY("Sát thương Vật lý"),
    PHEP("Sát thương Phép"),
    CHUAN("Sát thương Chuẩn")
}

data class HeroSkill(
    val id: String,
    val name: String,
    val description: String,
    val cooldown: Float,
    val manaCost: Int = 0,
    val isUltimate: Boolean = false,
    val isPassive: Boolean = false,
    val baseDamage: Float = 0f,
    val range: Float = 120f,
    val iconSymbol: String = "⚔️"
)

data class Hero(
    val id: String,
    val name: String,
    val title: String,
    val role: HeroRole,
    val damageType: DamageType,
    val difficulty: Int, // 1 to 5
    val themeColorHex: Long,
    val baseHp: Float = 3400f,
    val baseMp: Float = 480f,
    val baseAttack: Float = 175f,
    val baseDefense: Float = 120f,
    val attackRange: Float = 55f, // melee 50-60f, ranged 150-180f
    val moveSpeed: Float = 120f,
    val passive: HeroSkill,
    val skill1: HeroSkill,
    val skill2: HeroSkill,
    val skill3: HeroSkill, // Ultimate
    val recommendedItems: List<String> = emptyList(),
    val comboGuide: String = "",
    val quote: String = "",
    val lore: String = ""
)
