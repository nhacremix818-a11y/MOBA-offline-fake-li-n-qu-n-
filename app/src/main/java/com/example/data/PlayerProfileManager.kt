package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.GameMode
import org.json.JSONArray
import org.json.JSONObject

enum class RankTier(val title: String, val badgeColor: Long, val starsToPromote: Int) {
    DONG("Đồng I", 0xFF8D6E63, 3),
    BAC("Bạc I", 0xFFB0BEC5, 3),
    VANG("Vàng I", 0xFFFFD54F, 4),
    BACH_KIM("Bạch Kim I", 0xFF80DEEA, 5),
    KIM_CUONG("Kim Cương I", 0xFF42A5F5, 5),
    TINH_ANH("Tinh Anh I", 0xFFAB47BC, 5),
    CAO_THU("Cao Thủ", 0xFFFF7043, 25),
    CHIEN_TUONG("Chiến Tướng", 0xFFFFD700, 999)
}

data class MatchRecord(
    val id: String,
    val heroId: String,
    val heroName: String,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val isVictory: Boolean,
    val isMvp: Boolean,
    val gameMode: String,
    val timestamp: Long,
    val goldEarned: Int
)

class PlayerProfileManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("moba_player_profile", Context.MODE_PRIVATE)

    var playerName: String
        get() = prefs.getString("player_name", "Kiện Tướng Liên Quân") ?: "Kiện Tướng Liên Quân"
        set(value) = prefs.edit().putString("player_name", value).apply()

    var rankTierOrdinal: Int
        get() = prefs.getInt("rank_tier", 2) // Default Vàng I
        set(value) = prefs.edit().putInt("rank_tier", value).apply()

    var stars: Int
        get() = prefs.getInt("stars", 2)
        set(value) = prefs.edit().putInt("stars", value).apply()

    var goldCoins: Int
        get() = prefs.getInt("gold_coins", 8888)
        set(value) = prefs.edit().putInt("gold_coins", value).apply()

    var totalMatches: Int
        get() = prefs.getInt("total_matches", 42)
        set(value) = prefs.edit().putInt("total_matches", value).apply()

    var totalWins: Int
        get() = prefs.getInt("total_wins", 31)
        set(value) = prefs.edit().putInt("total_wins", value).apply()

    var totalMvp: Int
        get() = prefs.getInt("total_mvp", 18)
        set(value) = prefs.edit().putInt("total_mvp", value).apply()

    var totalMegaKills: Int
        get() = prefs.getInt("total_mega_kills", 4)
        set(value) = prefs.edit().putInt("total_mega_kills", value).apply()

    val currentRank: RankTier
        get() {
            val tiers = RankTier.values()
            return if (rankTierOrdinal in tiers.indices) tiers[rankTierOrdinal] else RankTier.VANG
        }

    fun recordMatch(
        heroId: String,
        heroName: String,
        kills: Int,
        deaths: Int,
        assists: Int,
        isVictory: Boolean,
        isMvp: Boolean,
        mode: GameMode,
        goldEarned: Int
    ) {
        totalMatches += 1
        if (isVictory) {
            totalWins += 1
            addStar()
        } else {
            removeStar()
        }
        if (isMvp) totalMvp += 1
        if (kills >= 5) totalMegaKills += 1
        goldCoins += goldEarned

        val records = getMatchHistory().toMutableList()
        records.add(
            0,
            MatchRecord(
                id = System.currentTimeMillis().toString(),
                heroId = heroId,
                heroName = heroName,
                kills = kills,
                deaths = deaths,
                assists = assists,
                isVictory = isVictory,
                isMvp = isMvp,
                gameMode = mode.title,
                timestamp = System.currentTimeMillis(),
                goldEarned = goldEarned
            )
        )
        // Keep last 20
        saveMatchHistory(records.take(20))
    }

    private fun addStar() {
        val current = currentRank
        if (stars < current.starsToPromote) {
            stars += 1
        } else {
            val tiers = RankTier.values()
            if (rankTierOrdinal < tiers.size - 1) {
                rankTierOrdinal += 1
                stars = 1
            } else {
                stars += 1
            }
        }
    }

    private fun removeStar() {
        if (stars > 0) {
            stars -= 1
        } else if (rankTierOrdinal > 0) {
            rankTierOrdinal -= 1
            stars = currentRank.starsToPromote - 1
        }
    }

    fun getMatchHistory(): List<MatchRecord> {
        val json = prefs.getString("match_history", null) ?: return defaultHistory()
        return try {
            val arr = JSONArray(json)
            val list = mutableListOf<MatchRecord>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    MatchRecord(
                        id = obj.getString("id"),
                        heroId = obj.getString("heroId"),
                        heroName = obj.getString("heroName"),
                        kills = obj.getInt("kills"),
                        deaths = obj.getInt("deaths"),
                        assists = obj.getInt("assists"),
                        isVictory = obj.getBoolean("isVictory"),
                        isMvp = obj.getBoolean("isMvp"),
                        gameMode = obj.getString("gameMode"),
                        timestamp = obj.getLong("timestamp"),
                        goldEarned = obj.getInt("goldEarned")
                    )
                )
            }
            list
        } catch (e: Exception) {
            defaultHistory()
        }
    }

    private fun saveMatchHistory(records: List<MatchRecord>) {
        val arr = JSONArray()
        for (r in records) {
            val obj = JSONObject().apply {
                put("id", r.id)
                put("heroId", r.heroId)
                put("heroName", r.heroName)
                put("kills", r.kills)
                put("deaths", r.deaths)
                put("assists", r.assists)
                put("isVictory", r.isVictory)
                put("isMvp", r.isMvp)
                put("gameMode", r.gameMode)
                put("timestamp", r.timestamp)
                put("goldEarned", r.goldEarned)
            }
            arr.put(obj)
        }
        prefs.edit().putString("match_history", arr.toString()).apply()
    }

    private fun defaultHistory(): List<MatchRecord> {
        val now = System.currentTimeMillis()
        return listOf(
            MatchRecord("1", "valhein", "Valhein", 14, 2, 8, true, true, "5v5 Bình Nguyên Vô Tận", now - 3600000, 350),
            MatchRecord("2", "nakroth", "Nakroth", 11, 3, 5, true, true, "5v5 Bình Nguyên Vô Tận", now - 7200000, 320),
            MatchRecord("3", "florentino", "Florentino", 9, 4, 7, true, false, "1v1 Cầu Độc Đạo", now - 18000000, 180),
            MatchRecord("4", "tulen", "Tulen", 8, 5, 12, false, false, "5v5 Bình Nguyên Vô Tận", now - 36000000, 150)
        )
    }
}
