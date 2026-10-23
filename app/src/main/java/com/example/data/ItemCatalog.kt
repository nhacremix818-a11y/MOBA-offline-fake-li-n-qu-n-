package com.example.data

import com.example.model.ItemCategory
import com.example.model.MobaItem

object ItemCatalog {

    val items: List<MobaItem> = listOf(
        // Công (Physical)
        MobaItem(
            id = "thanh_kiem",
            name = "Thánh Kiếm",
            category = ItemCategory.CONG,
            cost = 2140,
            attackBonus = 100f,
            critChance = 0.25f,
            passiveDesc = "Tăng thêm 50% sát thương chí mạng.",
            iconEmoji = "🗡️"
        ),
        MobaItem(
            id = "muramasa",
            name = "Kiếm Muramasa",
            category = ItemCategory.CONG,
            cost = 1980,
            attackBonus = 75f,
            cooldownReduction = 0.10f,
            passiveDesc = "+40% Xuyên giáp vật lý mục tiêu.",
            iconEmoji = "⚔️"
        ),
        MobaItem(
            id = "song_dao",
            name = "Song Đao Bão Táp",
            category = ItemCategory.CONG,
            cost = 2050,
            attackSpeed = 0.35f,
            critChance = 0.20f,
            moveSpeedBonus = 0.05f,
            passiveDesc = "Đòn đánh chí mạng tăng 35% kháng hiệu ứng và 10% tốc chạy.",
            iconEmoji = "🌪️"
        ),
        MobaItem(
            id = "guom_sam_set",
            name = "Gươm Sấm Sét",
            category = ItemCategory.CONG,
            cost = 1780,
            attackBonus = 60f,
            attackSpeed = 0.30f,
            moveSpeedBonus = 0.08f,
            passiveDesc = "Đòn đánh thường có 20% phóng tia sét lan 400 sát thương phép.",
            iconEmoji = "⚡"
        ),
        MobaItem(
            id = "phuc_hop_kiem",
            name = "Phức Hợp Kiếm",
            category = ItemCategory.CONG,
            cost = 2150,
            attackBonus = 70f,
            attackSpeed = 0.15f,
            hpBonus = 400f,
            cooldownReduction = 0.10f,
            passiveDesc = "Sau khi dùng chiêu, đòn đánh kế tiếp gây thêm 100% sát thương vật lý.",
            iconEmoji = "🔱"
        ),
        MobaItem(
            id = "thuong_longinus",
            name = "Thương Longinus",
            category = ItemCategory.CONG,
            cost = 2060,
            attackBonus = 80f,
            cooldownReduction = 0.15f,
            hpBonus = 500f,
            passiveDesc = "Đòn đánh phá giáp đối phương tối đa 250 giáp trong 5 giây.",
            iconEmoji = "📍"
        ),
        MobaItem(
            id = "nanh_fenrir",
            name = "Nanh Fenrir",
            category = ItemCategory.CONG,
            cost = 2950,
            attackBonus = 200f,
            passiveDesc = "Gây thêm 30% sát thương lên kẻ địch dưới 50% máu.",
            iconEmoji = "🐺"
        ),
        MobaItem(
            id = "liem_doat_menh",
            name = "Liềm Đoạt Mệnh",
            category = ItemCategory.CONG,
            cost = 1880,
            attackBonus = 60f,
            cooldownReduction = 0.05f,
            passiveDesc = "Khi chịu đòn chí mạng, bất tử trong 1 giây và tăng 20% tốc chạy.",
            iconEmoji = "🌙"
        ),

        // Phép (Magic)
        MobaItem(
            id = "truong_bung_no",
            name = "Trượng Bùng Nổ",
            category = ItemCategory.PHEP,
            cost = 2000,
            magicBonus = 240f,
            moveSpeedBonus = 0.05f,
            passiveDesc = "Chiêu thức trúng đích tạo vụ nổ gây thêm 150 (+0.4 Công phép) sát thương diện rộng.",
            iconEmoji = "💥"
        ),
        MobaItem(
            id = "vuong_mien_hecate",
            name = "Vương Miện Hecate",
            category = ItemCategory.PHEP,
            cost = 2410,
            magicBonus = 200f,
            passiveDesc = "Tăng thêm 35% tổng công phép và 75 xuyên giáp phép.",
            iconEmoji = "👑"
        ),
        MobaItem(
            id = "quyen_truong_rhea",
            name = "Quyền Trượng Rhea",
            category = ItemCategory.PHEP,
            cost = 2220,
            magicBonus = 140f,
            cooldownReduction = 0.10f,
            passiveDesc = "+25% Hút máu phép. Khi máu dưới 40% nhận lớp giáp ảo 1200 điểm.",
            iconEmoji = "🔮"
        ),
        MobaItem(
            id = "truong_bang",
            name = "Trượng Băng",
            category = ItemCategory.PHEP,
            cost = 1780,
            magicBonus = 140f,
            hpBonus = 850f,
            moveSpeedBonus = 0.05f,
            passiveDesc = "Chiêu thức làm chậm 20% tốc chạy của kẻ địch trong 2 giây.",
            iconEmoji = "❄️"
        ),
        MobaItem(
            id = "sach_truy_hon",
            name = "Sách Truy Hồn",
            category = ItemCategory.PHEP,
            cost = 2050,
            magicBonus = 200f,
            hpBonus = 500f,
            cooldownReduction = 0.10f,
            passiveDesc = "Giảm 40% khả năng hồi máu và hút máu của mục tiêu trong 1.5 giây.",
            iconEmoji = "📖"
        ),
        MobaItem(
            id = "qua_cau_bang_suong",
            name = "Quả Cầu Băng Sương",
            category = ItemCategory.PHEP,
            cost = 2000,
            magicBonus = 200f,
            cooldownReduction = 0.10f,
            passiveDesc = "Kích hoạt: Đóng băng bất tử trong 1.5 giây không thể bị chọn làm mục tiêu.",
            iconEmoji = "🧊"
        ),

        // Thủ (Defense)
        MobaItem(
            id = "khien_that_truyen",
            name = "Khiên Thất Truyền",
            category = ItemCategory.THU,
            cost = 2180,
            armorBonus = 270f,
            hpBonus = 1200f,
            passiveDesc = "Giảm 30% tốc đánh của kẻ địch đánh vào bản thân.",
            iconEmoji = "🛡️"
        ),
        MobaItem(
            id = "giap_thong_kho",
            name = "Giáp Thống Khổ",
            category = ItemCategory.THU,
            cost = 1940,
            armorBonus = 300f,
            hpBonus = 1200f,
            passiveDesc = "Phản lại 15% sát thương vật lý gánh chịu thành sát thương phép lên kẻ tấn công.",
            iconEmoji = "⛓️"
        ),
        MobaItem(
            id = "huan_chuong_troy",
            name = "Huân Chương Troy",
            category = ItemCategory.THU,
            cost = 2220,
            magicResistBonus = 360f,
            hpBonus = 1000f,
            cooldownReduction = 0.10f,
            passiveDesc = "Nhận lá chắn hấp thụ tối đa 1500 sát thương phép mỗi 18 giây.",
            iconEmoji = "🏅"
        ),
        MobaItem(
            id = "ao_choang_than_ra",
            name = "Áo Choàng Thần Ra",
            category = ItemCategory.THU,
            cost = 1950,
            armorBonus = 200f,
            hpBonus = 1000f,
            passiveDesc = "Thiêu đốt tất cả kẻ địch xung quanh 100 (+2% máu tối đa) sát thương phép mỗi giây.",
            iconEmoji = "🔥"
        ),
        MobaItem(
            id = "giap_ho_menh",
            name = "Giáp Hộ Mệnh",
            category = ItemCategory.THU,
            cost = 2400,
            armorBonus = 120f,
            passiveDesc = "Hồi sinh tại chỗ sau khi tử trận với 2000 máu (hồi chiêu 150 giây, tối đa 2 lần).",
            iconEmoji = "✨"
        ),

        // Tốc Chạy (Boots)
        MobaItem(
            id = "giay_kien_cuong",
            name = "Giày Kiên Cường",
            category = ItemCategory.TOC_CHAY,
            cost = 700,
            magicResistBonus = 110f,
            moveSpeedBonus = 0.15f,
            passiveDesc = "+35% Kháng hiệu ứng khống chế.",
            iconEmoji = "👢"
        ),
        MobaItem(
            id = "giay_du_muc",
            name = "Giày Du Mục",
            category = ItemCategory.TOC_CHAY,
            cost = 660,
            attackSpeed = 0.25f,
            moveSpeedBonus = 0.15f,
            passiveDesc = "+60 Tốc chạy cơ bản.",
            iconEmoji = "👟"
        ),
        MobaItem(
            id = "giay_thuat_si",
            name = "Giày Thuật Sĩ",
            category = ItemCategory.TOC_CHAY,
            cost = 710,
            cooldownReduction = 0.15f,
            moveSpeedBonus = 0.15f,
            passiveDesc = "Giảm 15% thời gian hồi chiêu.",
            iconEmoji = "🥿"
        ),

        // Trợ Thủ & Rừng
        MobaItem(
            id = "dai_nguyen_to",
            name = "Đại Nguyên Tố",
            category = ItemCategory.TRO_THU,
            cost = 1850,
            hpBonus = 1400f,
            cooldownReduction = 0.10f,
            moveSpeedBonus = 0.05f,
            passiveDesc = "Mở rộng tầm nhìn và tăng tốc độ chạy cho đồng minh xung quanh.",
            iconEmoji = "💎"
        ),
        MobaItem(
            id = "kiem_truy_hon",
            name = "Kiếm Truy Hồn",
            category = ItemCategory.RUNG,
            cost = 1750,
            attackBonus = 90f,
            cooldownReduction = 0.10f,
            moveSpeedBonus = 0.05f,
            passiveDesc = "Tăng sát thương lên quái rừng và nhận thêm 50% kinh nghiệm khi đi rừng.",
            iconEmoji = "🗡️"
        )
    )

    fun getItemById(id: String): MobaItem? {
        return items.find { it.id == id }
    }
}
