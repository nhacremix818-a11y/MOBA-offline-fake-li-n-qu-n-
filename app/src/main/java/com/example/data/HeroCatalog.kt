package com.example.data

import com.example.model.DamageType
import com.example.model.Hero
import com.example.model.HeroRole
import com.example.model.HeroSkill

object HeroCatalog {

    val heroes: List<Hero> = listOf(
        // 1. Valhein
        Hero(
            id = "valhein",
            name = "Valhein",
            title = "Thợ Săn Ác Quỷ",
            role = HeroRole.XA_THU,
            damageType = DamageType.VAT_LY,
            difficulty = 1,
            themeColorHex = 0xFFE53935,
            baseHp = 3200f,
            baseMp = 450f,
            baseAttack = 178f,
            baseDefense = 110f,
            attackRange = 165f,
            moveSpeed = 125f,
            passive = HeroSkill(
                id = "val_p",
                name = "Ám Khí",
                description = "Đòn đánh thứ 3 bắn ra phi tiêu ngẫu nhiên: Đỏ gây sát thương nổ, Vàng làm choáng mục tiêu 0.75s, Xanh hồi năng lượng.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "🎯"
            ),
            skill1 = HeroSkill(
                id = "val_s1",
                name = "Chuyến Săn Bão Táp",
                description = "Ném phi tiêu đỏ vào mục tiêu gây 350 (+0.65 Công vật lý) sát thương và tăng 25% tốc chạy trong 2 giây.",
                cooldown = 5f,
                manaCost = 50,
                baseDamage = 350f,
                range = 160f,
                iconSymbol = "🔺"
            ),
            skill2 = HeroSkill(
                id = "val_s2",
                name = "Trừ Tà",
                description = "Ném phi tiêu vàng làm choáng mục tiêu trong 1 giây và gây 280 (+0.55 Công phép) sát thương.",
                cooldown = 8f,
                manaCost = 65,
                baseDamage = 280f,
                range = 150f,
                iconSymbol = "🟡"
            ),
            skill3 = HeroSkill(
                id = "val_s3",
                name = "Bão Đạn",
                description = "Bắn ra 6 viên đạn bạc hình nón, mỗi viên gây 300 sát thương. Mục tiêu trúng nhiều đạn chịu lượng sát thương cực đại.",
                cooldown = 24f,
                manaCost = 100,
                isUltimate = true,
                baseDamage = 750f,
                range = 180f,
                iconSymbol = "💥"
            ),
            recommendedItems = listOf("thanh_kiem", "song_dao", "guom_sam_set", "giay_du_muc", "muramasa", "giap_ho_menh"),
            comboGuide = "Chiêu 2 (Choáng) -> Chiêu 1 (Tăng tốc) -> Đánh thường kích hoạt nội tại Ám Khí -> Chiêu cuối áp sát dồn sát thương.",
            quote = "Kẻ săn mồi tối thượng trong bóng đêm!",
            lore = "Valhein là thợ săn ma cà rồng huyền thoại của Lâu đài Khởi nguyên, luôn mang theo khẩu súng bạc trừ tà."
        ),

        // 2. Nakroth
        Hero(
            id = "nakroth",
            name = "Nakroth",
            title = "Lưỡi Đao Phán Xét",
            role = HeroRole.SAT_THU,
            damageType = DamageType.VAT_LY,
            difficulty = 5,
            themeColorHex = 0xFF7E57C2,
            baseHp = 3100f,
            baseMp = 420f,
            baseAttack = 192f,
            baseDefense = 105f,
            attackRange = 55f,
            moveSpeed = 140f,
            passive = HeroSkill(
                id = "nak_p",
                name = "Thẩm Phán Oai Nghiêm",
                description = "Mỗi đòn đánh thứ 4 hất tung mục tiêu trong 0.5s và tăng 50% tốc đánh.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "⚔️"
            ),
            skill1 = HeroSkill(
                id = "nak_s1",
                name = "Bồi Thẩm Đoàn",
                description = "Lao về phía trước hất tung kẻ địch trong 0.8s. Kỹ năng có thể tái kích hoạt trong vòng 5 giây.",
                cooldown = 8f,
                manaCost = 55,
                baseDamage = 280f,
                range = 140f,
                iconSymbol = "🌪️"
            ),
            skill2 = HeroSkill(
                id = "nak_s2",
                name = "Nguồn Cơn Rắc Rối",
                description = "Lướt lùi về sau, biến đòn đánh thường tiếp theo thành đòn quét chém diện rộng cực mạnh.",
                cooldown = 5f,
                manaCost = 45,
                baseDamage = 320f,
                range = 130f,
                iconSymbol = "⚡"
            ),
            skill3 = HeroSkill(
                id = "nak_s3",
                name = "Gươm Hành Quyết",
                description = "Chém liên hoàn trước mặt 4 nhát, miễn nhiễm mọi hiệu ứng khống chế, nhát cuối hất tung kẻ địch.",
                cooldown = 26f,
                manaCost = 100,
                isUltimate = true,
                baseDamage = 820f,
                range = 110f,
                iconSymbol = "🗡️"
            ),
            recommendedItems = listOf("kiem_truy_hon", "giay_kien_cuong", "phuc_hop_kiem", "thuong_longinus", "thanh_kiem", "giap_ho_menh"),
            comboGuide = "Chiêu 2 tiếp cận -> Đánh thường -> Chiêu 1 hất tung -> Đánh thường -> Chiêu 3 miễn khống chế kết liễu -> Chiêu 1 rút lui.",
            quote = "Ngươi không thể thoát khỏi bản án của ta!",
            lore = "Quan chấp pháp tối cao của Lực lượng Sa đọa, kẻ đoạt mạng chỉ bằng một cái chớp mắt."
        ),

        // 3. Florentino
        Hero(
            id = "florentino",
            name = "Florentino",
            title = "Tay Kiếm Hào Hoa",
            role = HeroRole.DAU_SI,
            damageType = DamageType.VAT_LY,
            difficulty = 5,
            themeColorHex = 0xFFFF4081,
            baseHp = 3500f,
            baseMp = 300f,
            baseAttack = 185f,
            baseDefense = 135f,
            attackRange = 55f,
            moveSpeed = 135f,
            passive = HeroSkill(
                id = "flo_p",
                name = "Xuất Kích",
                description = "Khi rời giao tranh hoặc sau khi nhặt hoa, đòn đánh kế tiếp đột kích áp sát, hồi 8% máu và tăng 50% tốc chạy.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "🌹"
            ),
            skill1 = HeroSkill(
                id = "flo_s1",
                name = "Hào Hoa",
                description = "Ném đóa hoa vào kẻ địch gây sát thương, làm choáng 0.5s và làm rơi ra 3 bông hoa nhặt để hồi phục nội năng.",
                cooldown = 7f,
                manaCost = 0,
                baseDamage = 300f,
                range = 150f,
                iconSymbol = "🌸"
            ),
            skill2 = HeroSkill(
                id = "flo_s2",
                name = "Thưởng Hoa",
                description = "Tung kiếm chém 3 nhịp: Nhịp 1 làm chậm 50%, Nhịp 2 hất tung 0.75s, Nhịp 3 gây sát thương chuẩn theo % máu tối đa.",
                cooldown = 1.5f,
                manaCost = 0,
                baseDamage = 260f,
                range = 80f,
                iconSymbol = "🤺"
            ),
            skill3 = HeroSkill(
                id = "flo_s3",
                name = "Tài Hoa",
                description = "Lướt tới thách đấu kẻ địch, tạo ra 3 bông hoa, miễn nhiễm 60% sát thương từ những kẻ không bị thách đấu.",
                cooldown = 32f,
                manaCost = 0,
                isUltimate = true,
                baseDamage = 450f,
                range = 130f,
                iconSymbol = "👑"
            ),
            recommendedItems = listOf("giay_kien_cuong", "ao_choang_bang_gia", "thuong_longinus", "nanh_fenrir", "giap_thong_kho", "giap_ho_menh"),
            comboGuide = "Chiêu 1 (Ném hoa) -> Đánh thường lướt tới nhặt hoa -> Chiêu 2 -> Đánh thường lướt hoa 2 -> Chiêu 2 -> Nhặt hoa 3 -> Chiêu cuối quyết đấu.",
            quote = "Hoa hồng có gai, kiếm của ta cũng vậy!",
            lore = "Quý tộc hào hoa xứ Okka, tay kiếm nghệ sĩ bất khả chiến bại trên mọi đấu trường."
        ),

        // 4. Tulen
        Hero(
            id = "tulen",
            name = "Tulen",
            title = "Hoàng Tử Lôi Quang",
            role = HeroRole.PHAP_SU,
            damageType = DamageType.PHEP,
            difficulty = 3,
            themeColorHex = 0xFF00E5FF,
            baseHp = 3250f,
            baseMp = 500f,
            baseAttack = 160f,
            baseDefense = 115f,
            attackRange = 145f,
            moveSpeed = 130f,
            passive = HeroSkill(
                id = "tul_p",
                name = "Lôi Điện",
                description = "Tích đủ 5 dấu ấn lôi điện, Tulen triệu hồi 5 tia sét tự động bắn vào mục tiêu gần nhất gây sát thương phép liên tục.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "⚡"
            ),
            skill1 = HeroSkill(
                id = "tul_s1",
                name = "Lôi Quang",
                description = "Bắn 3 tia sét theo hình nón, mỗi tia trúng kẻ địch gây 450 sát thương phép và cộng 1 dấu ấn Lôi Điện.",
                cooldown = 6f,
                manaCost = 60,
                baseDamage = 450f,
                range = 150f,
                iconSymbol = "🔱"
            ),
            skill2 = HeroSkill(
                id = "tul_s2",
                name = "Lôi Động",
                description = "Biến ảo lướt nhanh đến điểm chỉ định gây sát thương phép tại điểm đầu và cuối. Tích trữ tối đa 3 lần lướt.",
                cooldown = 2f,
                manaCost = 40,
                baseDamage = 320f,
                range = 120f,
                iconSymbol = "🌀"
            ),
            skill3 = HeroSkill(
                id = "tul_s3",
                name = "Lôi Điểu",
                description = "Khoá mục tiêu phóng ra chim sấm khổng lồ kết liễu, gây thêm 30% sát thương theo lượng máu mục tiêu đã mất. Hạ gục kích hoạt ngay nội tại!",
                cooldown = 28f,
                manaCost = 100,
                isUltimate = true,
                baseDamage = 780f,
                range = 190f,
                iconSymbol = "🦅"
            ),
            recommendedItems = listOf("giay_thuat_si", "truong_bung_no", "vuong_mien_hecate", "quyen_truong_rhea", "sach_truy_hon", "qua_cau_bang_suong"),
            comboGuide = "Chiêu 1 trúng cả 3 tia -> Chiêu 2 lướt vào tích đủ 5 nội tại sét bắn -> Chiêu 3 Lôi Điểu khóa mục tiêu kết liễu hoàn trả hồi chiêu.",
            quote = "Sấm sét là hiện thân của ý chí ta!",
            lore = "Đệ tử chân truyền của Tháp Quang Minh, người nắm giữ quyền năng lôi điện siêu việt."
        ),

        // 5. Krixi
        Hero(
            id = "krixi",
            name = "Krixi",
            title = "Tinh Linh Thiên Nhiên",
            role = HeroRole.PHAP_SU,
            damageType = DamageType.PHEP,
            difficulty = 1,
            themeColorHex = 0xFF76FF03,
            baseHp = 3150f,
            baseMp = 520f,
            baseAttack = 155f,
            baseDefense = 105f,
            attackRange = 155f,
            moveSpeed = 125f,
            passive = HeroSkill(
                id = "kri_p",
                name = "Tung Cánh",
                description = "Khi tung kỹ năng trúng mục tiêu, Krixi được tăng 30% tốc độ di chuyển trong 2 giây.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "🦋"
            ),
            skill1 = HeroSkill(
                id = "kri_s1",
                name = "Bướm Ảo",
                description = "Phóng một chú bướm ảo bay đi và bay về, gây 450 sát thương phép trên đường bay.",
                cooldown = 5f,
                manaCost = 60,
                baseDamage = 450f,
                range = 160f,
                iconSymbol = "✨"
            ),
            skill2 = HeroSkill(
                id = "kri_s2",
                name = "Bão Lá",
                description = "Triệu hồi một cơn bão lá hất tung tất cả kẻ địch trúng chiêu trong 1.5 giây và gây sát thương phép.",
                cooldown = 9f,
                manaCost = 70,
                baseDamage = 350f,
                range = 140f,
                iconSymbol = "🍃"
            ),
            skill3 = HeroSkill(
                id = "kri_s3",
                name = "Mưa Sao Băng",
                description = "Kêu gọi mưa sao băng liên tục đánh vào tất cả tướng địch xung quanh trong 5 giây, tối đa 4 lần mỗi mục tiêu.",
                cooldown = 32f,
                manaCost = 110,
                isUltimate = true,
                baseDamage = 850f,
                range = 150f,
                iconSymbol = "🌠"
            ),
            recommendedItems = listOf("giay_thuat_si", "truong_bung_no", "vuong_mien_hecate", "truong_bang", "sach_truy_hon", "giap_ho_menh"),
            comboGuide = "Chiêu 2 hất tung kẻ địch -> Chiêu 1 cấu rỉa -> Chiêu 3 bật mưa sao băng đuổi theo dồn sát thương.",
            quote = "Khu rừng này sẽ bảo vệ ta!",
            lore = "Nàng tinh linh xinh đẹp bảo vệ sự bình yên cho Khu Rừng Chạng Vạng."
        ),

        // 6. Arthur
        Hero(
            id = "arthur",
            name = "Arthur",
            title = "Thanh Kiếm Chính Nghĩa",
            role = HeroRole.DAU_SI,
            damageType = DamageType.VAT_LY,
            difficulty = 1,
            themeColorHex = 0xFFFFB300,
            baseHp = 3700f,
            baseMp = 0f,
            baseAttack = 170f,
            baseDefense = 160f,
            attackRange = 55f,
            moveSpeed = 130f,
            passive = HeroSkill(
                id = "art_p",
                name = "Uy Dũng",
                description = "Arthur nhận thêm 180 giáp vật lý và giáp phép cơ bản, tăng dần theo cấp độ.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "🛡️"
            ),
            skill1 = HeroSkill(
                id = "art_s1",
                name = "Gươm Chính Nghĩa",
                description = "Tăng 30% tốc chạy trong 3 giây. Đòn đánh tiếp theo nhảy chém mục tiêu gây câm lặng trong 1 giây.",
                cooldown = 7f,
                manaCost = 0,
                baseDamage = 320f,
                range = 120f,
                iconSymbol = "⚔️"
            ),
            skill2 = HeroSkill(
                id = "art_s2",
                name = "Thần Khí Hộ Thể",
                description = "Xoay lốc kiếm thánh quanh bản thân trong 5 giây, liên tục chém tất cả kẻ địch xung quanh.",
                cooldown = 8f,
                manaCost = 0,
                baseDamage = 400f,
                range = 90f,
                iconSymbol = "🌪️"
            ),
            skill3 = HeroSkill(
                id = "art_s3",
                name = "Công Lý Phán Quyết",
                description = "Bổ kiếm từ trên không xuống mục tiêu gây sát thương cực lớn và hất tung kẻ địch trong vùng rơi.",
                cooldown = 26f,
                manaCost = 0,
                isUltimate = true,
                baseDamage = 750f,
                range = 130f,
                iconSymbol = "⚡"
            ),
            recommendedItems = listOf("ao_choang_than_ra", "giay_kien_cuong", "khien_that_truyen", "phuc_hop_kiem", "huan_chuong_troy", "giap_ho_menh"),
            comboGuide = "Chiêu 1 tăng tốc áp sát -> Đánh thường nhảy chém câm lặng -> Bật Chiêu 2 xoay kiếm -> Chiêu 3 bổ kiếm dứt điểm.",
            quote = "Chính nghĩa sẽ chiến thắng!",
            lore = "Vị vua trẻ kiên cường lãnh đạo Lâu đài Khởi nguyên đẩy lùi bóng tối."
        ),

        // 7. Murad
        Hero(
            id = "murad",
            name = "Murad",
            title = "Lãng Khách Thời Không",
            role = HeroRole.SAT_THU,
            damageType = DamageType.VAT_LY,
            difficulty = 5,
            themeColorHex = 0xFFFF6D00,
            baseHp = 3000f,
            baseMp = 400f,
            baseAttack = 195f,
            baseDefense = 100f,
            attackRange = 55f,
            moveSpeed = 140f,
            passive = HeroSkill(
                id = "mur_p",
                name = "Hồn Phong",
                description = "Đánh thường liên tiếp 4 đòn trong 3 giây giúp mở khóa Chiêu 3 Ảo Ảnh Trảm và tăng 5% công vật lý.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "⌛"
            ),
            skill1 = HeroSkill(
                id = "mur_s1",
                name = "Tàn Ảnh Đao",
                description = "Lướt tới làm choáng mục tiêu, có thể lướt lần 2. Lần 3 biến trở về bóng xuất phát ban đầu.",
                cooldown = 8f,
                manaCost = 50,
                baseDamage = 300f,
                range = 130f,
                iconSymbol = "👣"
            ),
            skill2 = HeroSkill(
                id = "mur_s2",
                name = "Vô Ảnh Vực",
                description = "Tạo vùng thời không, không thể bị chọn làm mục tiêu. Kẻ địch chạm rìa vùng bị giảm tốc và giảm giáp.",
                cooldown = 10f,
                manaCost = 60,
                baseDamage = 250f,
                range = 100f,
                iconSymbol = "⭕"
            ),
            skill3 = HeroSkill(
                id = "mur_s3",
                name = "Ảo Ảnh Trảm",
                description = "Chém 5 nhát kiếm thời không liên hoàn siêu tốc. Murad hoàn toàn không thể bị chọn làm mục tiêu trong lúc múa kiếm.",
                cooldown = 20f,
                manaCost = 90,
                isUltimate = true,
                baseDamage = 950f,
                range = 130f,
                iconSymbol = "🗡️"
            ),
            recommendedItems = listOf("kiem_truy_hon", "giay_du_muc", "thuong_longinus", "phuc_hop_kiem", "nanh_fenrir", "giap_ho_menh"),
            comboGuide = "Đánh quái/lính 4 phát mở phong ấn -> Chiêu 1 lướt 2 nhịp vào đội hình địch -> Chiêu 2 né chiêu giảm giáp -> Chiêu 3 múa kiếm -> Chiêu 1 quay về vị trí cũ an toàn.",
            quote = "Thời gian và không gian đều phục tùng thanh kiếm của ta!",
            lore = "Hoàng tử sa mạc sở hữu quyền năng điều khiển thời gian từ vũ khí cổ xưa."
        ),

        // 8. Violet
        Hero(
            id = "violet",
            name = "Violet",
            title = "Tay Súng Quả Cảm",
            role = HeroRole.XA_THU,
            damageType = DamageType.VAT_LY,
            difficulty = 2,
            themeColorHex = 0xFFFF1744,
            baseHp = 3200f,
            baseMp = 430f,
            baseAttack = 182f,
            baseDefense = 108f,
            attackRange = 160f,
            moveSpeed = 130f,
            passive = HeroSkill(
                id = "vio_p",
                name = "Nạp Đạn",
                description = "Đòn đánh thường trúng tướng địch giúp giảm 0.5s thời gian hồi chiêu của Đạn Lộn Nhào.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "🔫"
            ),
            skill1 = HeroSkill(
                id = "vio_s1",
                name = "Đạn Lộn Nhào",
                description = "Lộn nhào về hướng chỉ định, cường hóa phát bắn kế tiếp tăng gấp đôi tầm bắn và gây sát thương chí mạng cực khủng.",
                cooldown = 4.5f,
                manaCost = 45,
                baseDamage = 420f,
                range = 190f,
                iconSymbol = "💥"
            ),
            skill2 = HeroSkill(
                id = "vio_s2",
                name = "Khai Hỏa",
                description = "Ném một quả lựu đạn gây sát thương và làm chậm 90% tốc độ di chuyển của kẻ địch trong 1 giây.",
                cooldown = 8f,
                manaCost = 50,
                baseDamage = 300f,
                range = 150f,
                iconSymbol = "💣"
            ),
            skill3 = HeroSkill(
                id = "vio_s3",
                name = "Pháo Đại",
                description = "Bắn một viên đạn pháo cực đại tầm siêu xa phát nổ diện rộng, gây lượng sát thương lớn cho kẻ địch.",
                cooldown = 25f,
                manaCost = 90,
                isUltimate = true,
                baseDamage = 750f,
                range = 210f,
                iconSymbol = "🚀"
            ),
            recommendedItems = listOf("thanh_kiem", "giay_du_muc", "song_dao", "phuc_hop_kiem", "muramasa", "nanh_fenrir"),
            comboGuide = "Chiêu 1 lộn nhào -> Bắn cường hóa tầm xa -> Chiêu 2 ném lựu đạn làm chậm -> Đánh thường hạ hồi chiêu 1 -> Chiêu 3 kết liễu từ xa.",
            quote = "Mỗi phát bắn là một mạng người!",
            lore = "Nữ xạ thủ hàng đầu của Vương quốc, người bạn đồng hành tin cậy của Valhein."
        ),

        // 9. Grakk
        Hero(
            id = "grakk",
            name = "Grakk",
            title = "Kẻ Nuốt Chửng",
            role = HeroRole.TRO_THU,
            damageType = DamageType.PHEP,
            difficulty = 3,
            themeColorHex = 0xFF00BFA5,
            baseHp = 4200f,
            baseMp = 480f,
            baseAttack = 150f,
            baseDefense = 180f,
            attackRange = 55f,
            moveSpeed = 125f,
            passive = HeroSkill(
                id = "gra_p",
                name = "Huyết Tế",
                description = "Mỗi khi nhận lượng sát thương lớn hoặc bị hạ gục, Grakk phát nổ sau 2s gây sát thương phép khủng khiếp cho kẻ xung quanh.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "☣️"
            ),
            skill1 = HeroSkill(
                id = "gra_s1",
                name = "Càn Quét",
                description = "Dậm mạnh xuống mặt đất gây sát thương phép và làm chậm 30% tốc độ di chuyển của kẻ địch xung quanh.",
                cooldown = 6f,
                manaCost = 50,
                baseDamage = 320f,
                range = 100f,
                iconSymbol = "💥"
            ),
            skill2 = HeroSkill(
                id = "gra_s2",
                name = "Xích Ma Quái",
                description = "Bắn bàn tay xích quỷ tầm xa kéo kẻ địch đầu tiên trúng đòn về ngay sát chân Grakk!",
                cooldown = 13f,
                manaCost = 75,
                baseDamage = 380f,
                range = 190f,
                iconSymbol = "⛓️"
            ),
            skill3 = HeroSkill(
                id = "gra_s3",
                name = "Nuốt Chửng",
                description = "Há miệng hút liên tục mọi kẻ địch trước mặt vào bụng, khống chế và tạo lá chắn ảo bảo vệ bản thân.",
                cooldown = 35f,
                manaCost = 120,
                isUltimate = true,
                baseDamage = 800f,
                range = 120f,
                iconSymbol = "🕳️"
            ),
            recommendedItems = listOf("dai_nguyen_to", "giay_kien_cuong", "khien_that_truyen", "giap_thong_kho", "huan_chuong_troy", "giap_ho_menh"),
            comboGuide = "Chiêu 2 kéo địch chuẩn xác từ trong bụi cỏ -> Chiêu 1 dậm làm chậm -> Chiêu 3 hút nuốt chửng giam cầm đối phương.",
            quote = "Ta đói quá... món tráng miệng đây rồi!",
            lore = "Quái thú háu ăn nơi vực sâu tăm tối, nỗi ác mộng kéo kẻ thù vào chỗ chết."
        ),

        // 10. Raz
        Hero(
            id = "raz",
            name = "Raz",
            title = "Quyền Vương",
            role = HeroRole.PHAP_SU,
            damageType = DamageType.PHEP,
            difficulty = 4,
            themeColorHex = 0xFFFF6F00,
            baseHp = 3250f,
            baseMp = 200f,
            baseAttack = 165f,
            baseDefense = 110f,
            attackRange = 55f,
            moveSpeed = 135f,
            passive = HeroSkill(
                id = "raz_p",
                name = "Đấm & Lách",
                description = "Sau khi tung chiêu, Raz có thể lướt nhanh theo một hướng và đòn đánh thứ 3 đẩy lùi kẻ địch.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "🥊"
            ),
            skill1 = HeroSkill(
                id = "raz_s1",
                name = "Cú Đấm Thăng Hoa",
                description = "Lao về phía trước tung cú đấm móc hất tung tất cả kẻ địch trong vùng trong 1 giây.",
                cooldown = 8f,
                manaCost = 60,
                baseDamage = 420f,
                range = 110f,
                iconSymbol = "🌪️"
            ),
            skill2 = HeroSkill(
                id = "raz_s2",
                name = "Cú Đấm Chân Không",
                description = "Phóng một quả cầu lửa tầm xa gây sát thương phép, làm chậm tới 90% và giảm mạnh giáp phép của mục tiêu.",
                cooldown = 3f,
                manaCost = 40,
                baseDamage = 480f,
                range = 180f,
                iconSymbol = "🔥"
            ),
            skill3 = HeroSkill(
                id = "raz_s3",
                name = "Tả Xung Hữu Đột",
                description = "Lao vút tới tung cú đấm trời giáng đẩy lùi toàn bộ kẻ địch theo hướng đấm và gây sát thương phép cực lớn.",
                cooldown = 24f,
                manaCost = 90,
                isUltimate = true,
                baseDamage = 850f,
                range = 130f,
                iconSymbol = "⚡"
            ),
            recommendedItems = listOf("giay_thuat_si", "truong_bung_no", "vuong_mien_hecate", "truong_bang", "sach_truy_hon", "qua_cau_bang_suong"),
            comboGuide = "Chiêu 2 câu rỉa làm chậm -> Chiêu 1 hất tung tiếp cận -> Lách ra sau lưng đối thủ -> Chiêu 3 đấm đẩy ngược về phía trụ mình.",
            quote = "Nắm đấm của ta rực cháy ngọn lửa ý chí!",
            lore = "Võ sĩ quyền Anh ngầm bảo vệ thành phố khỏi các thế lực hắc ám."
        ),

        // 11. Natalya
        Hero(
            id = "natalya",
            name = "Natalya",
            title = "Hiểm Họa Địa Ngục",
            role = HeroRole.PHAP_SU,
            damageType = DamageType.PHEP,
            difficulty = 2,
            themeColorHex = 0xFF00E676,
            baseHp = 3100f,
            baseMp = 500f,
            baseAttack = 150f,
            baseDefense = 100f,
            attackRange = 150f,
            moveSpeed = 125f,
            passive = HeroSkill(
                id = "nat_p",
                name = "Ám Ảnh",
                description = "Tung chiêu trúng kẻ địch gây thêm sát thương và cộng dồn hiệu ứng thiêu đốt.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "🧪"
            ),
            skill1 = HeroSkill(
                id = "nat_s1",
                name = "Ác Quỷ Hội Tụ",
                description = "Bắn ra 5 quả cầu ma thuật hội tụ tại một điểm, gây sát thương phép hủy diệt.",
                cooldown = 5f,
                manaCost = 55,
                baseDamage = 520f,
                range = 160f,
                iconSymbol = "🟢"
            ),
            skill2 = HeroSkill(
                id = "nat_s2",
                name = "Quả Cầu Hắc Ám",
                description = "Bắn ra quả cầu năng lượng bay chậm, khi chạm mục tiêu gây choáng 1s và phát tán khí độc làm chậm.",
                cooldown = 8f,
                manaCost = 70,
                baseDamage = 350f,
                range = 140f,
                iconSymbol = "💫"
            ),
            skill3 = HeroSkill(
                id = "nat_s3",
                name = "Tia Sáng Tử Vong",
                description = "Bắn ra luồng laser khổng lồ xoay theo hướng trỏ, nhận lớp khiên dày và miễn nhiễm toàn bộ khống chế.",
                cooldown = 28f,
                manaCost = 100,
                isUltimate = true,
                baseDamage = 1100f,
                range = 190f,
                iconSymbol = "⚡"
            ),
            recommendedItems = listOf("giay_thuat_si", "truong_bung_no", "vuong_mien_hecate", "truong_bang", "sach_truy_hon", "giap_ho_menh"),
            comboGuide = "Chiêu 2 gây choáng -> Chiêu 1 bắn trọn 5 viên -> Chiêu 3 quét tia laser hủy diệt lập tức hạ gục mục tiêu.",
            quote = "Hãy để ngọn lửa hắc ám thiêu rụi tất cả!",
            lore = "Nữ pháp sư độc dược mang trong mình sức mạnh nguyền rủa từ địa ngục."
        ),

        // 12. Elsu
        Hero(
            id = "elsu",
            name = "Elsu",
            title = "Xạ Thủ Tinh Anh",
            role = HeroRole.XA_THU,
            damageType = DamageType.VAT_LY,
            difficulty = 5,
            themeColorHex = 0xFF546E7A,
            baseHp = 3200f,
            baseMp = 440f,
            baseAttack = 210f,
            baseDefense = 110f,
            attackRange = 170f,
            moveSpeed = 130f,
            passive = HeroSkill(
                id = "els_p",
                name = "Bắn Tỉa Ngụy Trang",
                description = "Khi đứng gần vách tường địa hình, Elsu ngụy trang tàng hình và được tăng tốc chạy cực nhanh.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "🎯"
            ),
            skill1 = HeroSkill(
                id = "els_s1",
                name = "Ưng Trạm",
                description = "Đặt bẫy ưng soi sáng tầm nhìn vùng rộng và tăng xuyên giáp cho Elsu khi đứng trong vùng.",
                cooldown = 15f,
                manaCost = 40,
                baseDamage = 0f,
                range = 150f,
                iconSymbol = "👁️"
            ),
            skill2 = HeroSkill(
                id = "els_s2",
                name = "Viễn Trình Kích",
                description = "Ngắm bắn phát đạn tỉa xuyên thấu siêu xa, gây sát thương vật lý cực khủng theo % máu.",
                cooldown = 1.5f,
                manaCost = 50,
                baseDamage = 680f,
                range = 280f,
                iconSymbol = "🔭"
            ),
            skill3 = HeroSkill(
                id = "els_s3",
                name = "Giật Bắn",
                description = "Bắn đạn cực mạnh và giật lùi về sau để giữ khoảng cách an toàn, tăng tốc chạy.",
                cooldown = 22f,
                manaCost = 80,
                isUltimate = true,
                baseDamage = 600f,
                range = 140f,
                iconSymbol = "💨"
            ),
            recommendedItems = listOf("kiem_muramasa", "giay_du_muc", "nanh_fenrir", "thanh_kiem", "phuc_hop_kiem", "giap_ho_menh"),
            comboGuide = "Đặt Chiêu 1 kiểm soát bản đồ -> Ngắm Chiêu 2 tỉa máu đối phương từ xa -> Chiêu 3 giật bắn nếu bị kẻ địch áp sát.",
            quote = "Mỗi viên đạn đều mang theo số phận của kẻ địch.",
            lore = "Xạ thủ bách phát bách trúng của Biệt đội Vệ binh Thành phố."
        ),

        // 13. Hayate
        Hero(
            id = "hayate",
            name = "Hayate",
            title = "Ninja Tà Thần",
            role = HeroRole.XA_THU,
            damageType = DamageType.CHUAN,
            difficulty = 4,
            themeColorHex = 0xFFAB47BC,
            baseHp = 3150f,
            baseMp = 200f,
            baseAttack = 175f,
            baseDefense = 105f,
            attackRange = 160f,
            moveSpeed = 135f,
            passive = HeroSkill(
                id = "hay_p",
                name = "Ảo Ảnh Trảm",
                description = "Đòn đánh hoặc phi tiêu tích cộng dồn, đạt 6 lần sẽ kích hoạt sát thương chuẩn bỏ qua mọi giáp bảo vệ.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "🥷"
            ),
            skill1 = HeroSkill(
                id = "hay_s1",
                name = "Chuỗi Phi Tiêu",
                description = "Vừa di chuyển vừa phóng liên hoàn phi tiêu theo một hướng, kích hoạt hiệu ứng đòn đánh.",
                cooldown = 5f,
                manaCost = 40,
                baseDamage = 400f,
                range = 170f,
                iconSymbol = "🪃"
            ),
            skill2 = HeroSkill(
                id = "hay_s2",
                name = "Ảnh Biến",
                description = "Dịch chuyển tức thời đến điểm chỉ định. Khi gần tướng địch được tăng tốc chạy và sát thương.",
                cooldown = 6f,
                manaCost = 30,
                baseDamage = 0f,
                range = 120f,
                iconSymbol = "⚡"
            ),
            skill3 = HeroSkill(
                id = "hay_s3",
                name = "Bão Phi Tiêu",
                description = "Lướt vào trận địa xoay tròn bão phi tiêu tàn sát mọi kẻ địch xung quanh bằng mưa sát thương chuẩn.",
                cooldown = 30f,
                manaCost = 80,
                isUltimate = true,
                baseDamage = 950f,
                range = 150f,
                iconSymbol = "🌪️"
            ),
            recommendedItems = listOf("kiem_sam_set", "giay_du_muc", "song_dao", "ao_choang_bang", "nanh_fenrir", "giap_ho_menh"),
            comboGuide = "Chiêu 1 cấu rỉa tích dấu ấn sát thương chuẩn -> Chiêu 2 lướt chọn góc thuận lợi -> Bật Chiêu 3 xoay bão phi tiêu kết liễu toàn bộ đội hình địch.",
            quote = "Bóng tối chính là đồng minh trung thành nhất!",
            lore = "Ninja phản đồ mang sức mạnh rồng bóng tối của tộc Ninja cổ."
        ),

        // 14. Allain
        Hero(
            id = "allain",
            name = "Allain",
            title = "Cuồng Kiếm Sĩ",
            role = HeroRole.DAU_SI,
            damageType = DamageType.VAT_LY,
            difficulty = 3,
            themeColorHex = 0xFF29B6F6,
            baseHp = 3500f,
            baseMp = 0f,
            baseAttack = 180f,
            baseDefense = 130f,
            attackRange = 55f,
            moveSpeed = 135f,
            passive = HeroSkill(
                id = "all_p",
                name = "Kiếm Gầm",
                description = "Đòn đánh thường gồm 4 nhịp kết hợp cả sát thương vật lý, phép và chuẩn, hồi máu khi đủ điểm.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "⚔️"
            ),
            skill1 = HeroSkill(
                id = "all_s1",
                name = "Kiếm Lưu Ngân",
                description = "Chém kiếm liên tục nhiều nhịp, nhịp cuối tung kiếm hất tung kẻ địch trong vùng.",
                cooldown = 6f,
                manaCost = 0,
                baseDamage = 450f,
                range = 90f,
                iconSymbol = "⚡"
            ),
            skill2 = HeroSkill(
                id = "all_s2",
                name = "Tỏa Kiếm",
                description = "Lướt tới trói chân mục tiêu đầu tiên trúng chiêu trong 1.5 giây. Tích trữ tối đa 2 lần lướt.",
                cooldown = 2f,
                manaCost = 0,
                baseDamage = 260f,
                range = 120f,
                iconSymbol = "🔗"
            ),
            skill3 = HeroSkill(
                id = "all_s3",
                name = "Đoạt Mệnh Kích",
                description = "Bay lên trời khóa chặt mục tiêu không thể chọn, sau đó giáng kiếm xuống hủy diệt diện rộng.",
                cooldown = 30f,
                manaCost = 0,
                isUltimate = true,
                baseDamage = 850f,
                range = 160f,
                iconSymbol = "☄️"
            ),
            recommendedItems = listOf("thanh_kiem", "giay_kien_cuong", "song_dao", "nanh_fenrir", "giap_thong_kho", "giap_ho_menh"),
            comboGuide = "Chiêu 2 trói chân -> Đánh thường 4 phát tích nội tại -> Chiêu 1 chém liên hoàn hất tung -> Chiêu 3 bay lên trời kết liễu.",
            quote = "Thanh song kiếm này sẽ định đoạt thắng bại!",
            lore = "Chàng kiếm sĩ mất trí nhớ với kỹ năng song kiếm trác tuyệt."
        ),

        // 15. Aya
        Hero(
            id = "aya",
            name = "Aya",
            title = "Ca Sĩ Tí Hon",
            role = HeroRole.TRO_THU,
            damageType = DamageType.PHEP,
            difficulty = 1,
            themeColorHex = 0xFFFF80AB,
            baseHp = 2950f,
            baseMp = 480f,
            baseAttack = 140f,
            baseDefense = 95f,
            attackRange = 140f,
            moveSpeed = 125f,
            passive = HeroSkill(
                id = "aya_p",
                name = "Rừng Sâu Tí Hon",
                description = "Khi tụt dưới 40% máu, Aya hóa thành sóc con bất tử không thể bị chọn trong 3 giây.",
                cooldown = 60f,
                isPassive = true,
                iconSymbol = "🐿️"
            ),
            skill1 = HeroSkill(
                id = "aya_s1",
                name = "Nốt Nhạc Ánh Sáng",
                description = "Bắn ra sóng âm mở rộng tầm nhìn, gây sát thương phép và làm chậm kẻ địch.",
                cooldown = 5f,
                manaCost = 45,
                baseDamage = 280f,
                range = 150f,
                iconSymbol = "🎵"
            ),
            skill2 = HeroSkill(
                id = "aya_s2",
                name = "Rừng Sâu Che Chở",
                description = "Nhảy lên đầu đồng đội tạo lớp giáp dày bảo vệ cực lớn và tăng tốc độ di chuyển cho đồng đội.",
                cooldown = 7f,
                manaCost = 60,
                baseDamage = 0f,
                range = 130f,
                iconSymbol = "💖"
            ),
            skill3 = HeroSkill(
                id = "aya_s3",
                name = "Khúc Nhạc Rừng Sâu",
                description = "Phát ra hào quang ma thuật, sau 1.5s biến tất cả tướng địch xung quanh thành thú bông vô hại!",
                cooldown = 32f,
                manaCost = 100,
                isUltimate = true,
                baseDamage = 520f,
                range = 140f,
                iconSymbol = "🧸"
            ),
            recommendedItems = listOf("dai_nguyen_to", "giay_thuat_si", "truong_bang", "mat_na_berith", "sach_truy_hon", "giap_ho_menh"),
            comboGuide = "Nhập Chiêu 2 lên đầu sát thủ/đấu sĩ -> Chiêu 1 câu rỉa làm chậm -> Bật Chiêu 3 hóa thú toàn bộ team bạn.",
            quote = "Hãy để âm nhạc chữa lành cho mọi người!",
            lore = "Ca sĩ sóc con thần tượng của Rừng Chạng Vạng."
        ),

        // 16. Maloch
        Hero(
            id = "maloch",
            name = "Maloch",
            title = "Ma Vương Quản Ngục",
            role = HeroRole.DAU_SI,
            damageType = DamageType.CHUAN,
            difficulty = 2,
            themeColorHex = 0xFFD32F2F,
            baseHp = 3900f,
            baseMp = 380f,
            baseAttack = 185f,
            baseDefense = 150f,
            attackRange = 60f,
            moveSpeed = 125f,
            passive = HeroSkill(
                id = "mal_p",
                name = "Ma Vương",
                description = "Sau khi Quỷ Kiếm trúng tướng, Maloch tiến vào trạng thái Ma Vương biến mọi đòn đánh thành sát thương chuẩn và hồi máu.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "👹"
            ),
            skill1 = HeroSkill(
                id = "mal_s1",
                name = "Quỷ Kiếm",
                description = "Vung kiếm chém quét hình nón cực mạnh, gây sát thương chuẩn và hồi lượng máu cực khủng.",
                cooldown = 5f,
                manaCost = 0,
                baseDamage = 550f,
                range = 100f,
                iconSymbol = "🗡️"
            ),
            skill2 = HeroSkill(
                id = "mal_s2",
                name = "Đoạt Hồn",
                description = "Tước đoạt linh hồn kẻ địch xung quanh làm chậm đối thủ và tạo một lớp lá chắn khổng lồ.",
                cooldown = 8f,
                manaCost = 50,
                baseDamage = 200f,
                range = 110f,
                iconSymbol = "🛡️"
            ),
            skill3 = HeroSkill(
                id = "mal_s3",
                name = "Luyện Ngục",
                description = "Bay vút lên trời rồi giáng xuống tâm điểm hất tung diện rộng mọi kẻ địch và tạo đấu trường địa ngục.",
                cooldown = 35f,
                manaCost = 110,
                isUltimate = true,
                baseDamage = 850f,
                range = 180f,
                iconSymbol = "🌋"
            ),
            recommendedItems = listOf("ao_choang_than_ra", "giay_kien_cuong", "khien_that_truyen", "nanh_fenrir", "giap_thong_kho", "giap_ho_menh"),
            comboGuide = "Chiêu 2 hút giáp làm chậm -> Chiêu 1 chém Quỷ Kiếm kích hoạt Ma Vương -> Chiêu 3 nhảy Luyện Ngục mở giao tranh.",
            quote = "Cả thế giới sẽ quỳ gối dưới chân Ma Vương!",
            lore = "Kẻ cai quản tối cao của Ngục Vực, chúa tể của bóng tối và sự thống khổ."
        ),

        // 17. Liliana
        Hero(
            id = "liliana",
            name = "Liliana",
            title = "Cửu Vĩ Yêu Hồ",
            role = HeroRole.PHAP_SU,
            damageType = DamageType.PHEP,
            difficulty = 5,
            themeColorHex = 0xFFFF4081,
            baseHp = 3200f,
            baseMp = 510f,
            baseAttack = 162f,
            baseDefense = 108f,
            attackRange = 150f,
            moveSpeed = 135f,
            passive = HeroSkill(
                id = "lil_p",
                name = "Yêu Hồ",
                description = "Dạng Người tăng tầm đánh xa; Dạng Cáo tăng mạnh giáp, tốc chạy và cường hóa đòn đánh cào xé.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "🦊"
            ),
            skill1 = HeroSkill(
                id = "lil_s1",
                name = "Kế Sách Quang Minh",
                description = "Dạng người: Gọi linh đạn phát nổ. Dạng cáo: Vồ cào liên tiếp nhiều đòn gây sát thương phép liên hoàn.",
                cooldown = 5f,
                manaCost = 50,
                baseDamage = 450f,
                range = 150f,
                iconSymbol = "✨"
            ),
            skill2 = HeroSkill(
                id = "lil_s2",
                name = "Ảo Ảnh Hồ Ly",
                description = "Dạng người: Phóng đạn gây choáng 1s. Dạng cáo: Lướt tới và triệu hồi quả cầu linh hồn khổng lồ bay theo mục tiêu.",
                cooldown = 8f,
                manaCost = 65,
                baseDamage = 380f,
                range = 140f,
                iconSymbol = "💫"
            ),
            skill3 = HeroSkill(
                id = "lil_s3",
                name = "Biến Chuyển Hình Dạng",
                description = "Chuyển đổi giữa Dạng Người và Dạng Cáo, lướt nhanh và không thể bị chọn làm mục tiêu khi biến hình.",
                cooldown = 6f,
                manaCost = 0,
                isUltimate = true,
                baseDamage = 400f,
                range = 120f,
                iconSymbol = "🌀"
            ),
            recommendedItems = listOf("giay_thuat_si", "truong_bung_no", "vuong_mien_hecate", "quyen_truong_rhea", "sach_truy_hon", "qua_cau_bang_suong"),
            comboGuide = "Dạng người: Chiêu 2 gây choáng -> Chiêu 1 nổ phép -> Chiêu 3 hóa Cáo lướt vào -> Chiêu 2 vồ triệu hồi cầu linh hồn -> Chiêu 1 cào xé dứt điểm.",
            quote = "Tri thức ngàn năm nằm trong một cái phẩy đuôi!",
            lore = "Nàng hồ ly chín đuôi nắm giữ tri thức vô tận của vũ trụ."
        ),

        // 18. Butterfly
        Hero(
            id = "butterfly",
            name = "Butterfly",
            title = "Siêu Cấp Sát Thủ",
            role = HeroRole.SAT_THU,
            damageType = DamageType.VAT_LY,
            difficulty = 2,
            themeColorHex = 0xFFFFD600,
            baseHp = 3300f,
            baseMp = 0f,
            baseAttack = 188f,
            baseDefense = 115f,
            attackRange = 55f,
            moveSpeed = 135f,
            passive = HeroSkill(
                id = "but_p",
                name = "Khát Máu",
                description = "Hạ gục hoặc phụ hạ kích hoạt dấu ấn làm mới toàn bộ thời gian hồi chiêu của tất cả kỹ năng!",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "🩸"
            ),
            skill1 = HeroSkill(
                id = "but_s1",
                name = "Phóng Kiếm",
                description = "Quét kiếm xung quanh tạo lá chắn bảo vệ và tăng 50% tốc độ di chuyển.",
                cooldown = 6f,
                manaCost = 0,
                baseDamage = 320f,
                range = 80f,
                iconSymbol = "🛡️"
            ),
            skill2 = HeroSkill(
                id = "but_s2",
                name = "Quét Kiếm",
                description = "Phóng kiếm về phía trước theo đường thẳng gây sát thương và tăng mạnh tốc đánh.",
                cooldown = 6f,
                manaCost = 0,
                baseDamage = 360f,
                range = 110f,
                iconSymbol = "⚡"
            ),
            skill3 = HeroSkill(
                id = "but_s3",
                name = "Ám Sát",
                description = "Tức thì nhảy ra sau lưng kẻ địch ít máu nhất, chém nhát kiếm chí mạng và giảm sát thương nhận vào.",
                cooldown = 13f,
                manaCost = 0,
                isUltimate = true,
                baseDamage = 780f,
                range = 140f,
                iconSymbol = "🗡️"
            ),
            recommendedItems = listOf("kiem_truy_hon", "giay_kien_cuong", "thuong_longinus", "phuc_hop_kiem", "nanh_fenrir", "giap_ho_menh"),
            comboGuide = "Chiêu 3 nhảy áp sát kẻ máu thấp -> Chiêu 2 tăng tốc đánh -> Chiêu 1 xoay kiếm tạo giáp -> Kết liễu hồi lại chiêu tiếp tục chém mục tiêu kế tiếp.",
            quote = "Nhiệm vụ nhận... Mục tiêu đã bị hạ gục!",
            lore = "Nữ lính đánh thuê thiện chiến, không bao giờ để con mồi trốn thoát."
        ),

        // 19. Richter
        Hero(
            id = "richter",
            name = "Richter",
            title = "Bá Tước Kiếm Cổ",
            role = HeroRole.DAU_SI,
            damageType = DamageType.VAT_LY,
            difficulty = 4,
            themeColorHex = 0xFF795548,
            baseHp = 3550f,
            baseMp = 350f,
            baseAttack = 182f,
            baseDefense = 140f,
            attackRange = 55f,
            moveSpeed = 135f,
            passive = HeroSkill(
                id = "ric_p",
                name = "Thanh Gươm Biến Huyễn",
                description = "Thích ứng địa hình: Trong Bụi Cỏ tăng tốc đánh và hất tung; Dưới Sông tăng tốc chạy và hồi máu; Trên Đường tăng miễn thương.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "🌿"
            ),
            skill1 = HeroSkill(
                id = "ric_s1",
                name = "Chặt & Chém",
                description = "Lướt tới chém kiếm. Trong bụi cỏ sẽ hất tung kẻ địch; nếu trúng tướng hồi chiêu ngay lập tức.",
                cooldown = 7f,
                manaCost = 0,
                baseDamage = 380f,
                range = 120f,
                iconSymbol = "⚔️"
            ),
            skill2 = HeroSkill(
                id = "ric_s2",
                name = "Công Thủ Toàn Diện",
                description = "Đưa kiếm phòng ngự, miễn nhiễm toàn bộ sát thương và khống chế trong 0.75 giây rồi phản đòn.",
                cooldown = 4f,
                manaCost = 0,
                baseDamage = 300f,
                range = 80f,
                iconSymbol = "🛡️"
            ),
            skill3 = HeroSkill(
                id = "ric_s3",
                name = "Vùng Diệt Dzoong",
                description = "Tạo vùng ma trận khắc dấu ấn hủy diệt, gây sát thương chuẩn liên tục xung quanh bản thân.",
                cooldown = 32f,
                manaCost = 0,
                isUltimate = true,
                baseDamage = 820f,
                range = 140f,
                iconSymbol = "🌋"
            ),
            recommendedItems = listOf("giay_kien_cuong", "thuong_longinus", "ao_choang_bang", "nanh_fenrir", "giap_thong_kho", "giap_ho_menh"),
            comboGuide = "Nấp bụi cỏ nhận kiếm đỏ -> Chiêu 1 lướt hất tung -> Bật Chiêu 3 ma trận sát thương chuẩn -> Chiêu 2 né đòn phản công -> Chiêu 1 lướt chém tiếp.",
            quote = "Thanh kiếm cổ này đã uống máu bao kẻ thù!",
            lore = "Thợ săn quỷ huyền bí lang bạt khắp đại lục Athanor."
        ),

        // 20. Alice
        Hero(
            id = "alice",
            name = "Alice",
            title = "Tiểu Thần Thiện Lành",
            role = HeroRole.TRO_THU,
            damageType = DamageType.PHEP,
            difficulty = 1,
            themeColorHex = 0xFFFF8A80,
            baseHp = 3000f,
            baseMp = 550f,
            baseAttack = 135f,
            baseDefense = 100f,
            attackRange = 140f,
            moveSpeed = 130f,
            passive = HeroSkill(
                id = "ali_p",
                name = "Bước Chân Thần Tốc",
                description = "Mỗi khi tung kỹ năng, Alice được tăng 20% tốc độ di chuyển trong 1 giây.",
                cooldown = 0f,
                isPassive = true,
                iconSymbol = "👟"
            ),
            skill1 = HeroSkill(
                id = "ali_s1",
                name = "Vụ Nổ Thời Không",
                description = "Bắn quả cầu ma pháp tới điểm chỉ định làm choáng tất cả mục tiêu trúng đòn trong 1 giây.",
                cooldown = 6f,
                manaCost = 60,
                baseDamage = 320f,
                range = 150f,
                iconSymbol = "💫"
            ),
            skill2 = HeroSkill(
                id = "ali_s2",
                name = "Lá Chắn Thời Không",
                description = "Tạo lá chắn bảo vệ cho bản thân và toàn bộ đồng đội xung quanh, đồng thời tăng 30% tốc độ di chuyển.",
                cooldown = 8f,
                manaCost = 65,
                baseDamage = 0f,
                range = 130f,
                iconSymbol = "🛡️"
            ),
            skill3 = HeroSkill(
                id = "ali_s3",
                name = "Dòng Chảy Thời Không",
                description = "Tạo một vùng ma trận khổng lồ làm chậm cực đại, câm lặng 1 giây và liên tục bào mòn giáp phép đối phương.",
                cooldown = 28f,
                manaCost = 100,
                isUltimate = true,
                baseDamage = 650f,
                range = 160f,
                iconSymbol = "⏳"
            ),
            recommendedItems = listOf("dai_nguyen_to", "giay_thuat_si", "truong_bang", "mat_na_berith", "sach_truy_hon", "giap_ho_menh"),
            comboGuide = "Chiêu 2 bật khiên tăng tốc đội hình -> Chiêu 3 thả vùng làm chậm và câm lặng chặn đường địch -> Chiêu 1 làm choáng kẻ tháo chạy.",
            quote = "Alice đáng yêu nhất trần đời!",
            lore = "Nàng thần hộ mệnh tí hon sở hữu pháp thuật thời gian kỳ diệu."
        )
    )

    fun getHeroById(id: String): Hero {
        return heroes.find { it.id == id } ?: heroes.first()
    }
}
