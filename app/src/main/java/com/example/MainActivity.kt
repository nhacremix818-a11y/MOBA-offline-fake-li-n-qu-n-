package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.data.HeroCatalog
import com.example.data.PlayerProfileManager
import com.example.game.GameEngine
import com.example.game.SoundAndHaptic
import com.example.model.GameMode
import com.example.model.Hero
import com.example.ui.screens.*
import com.example.ui.theme.MobaDarkBackground
import com.example.ui.theme.MyApplicationTheme

enum class ScreenState {
    HOME,
    HERO_SELECT,
    BATTLE,
    POST_GAME,
    HERO_CODEX,
    ITEM_SHOP,
    PROFILE
}

class MainActivity : ComponentActivity() {
    private var soundAndHaptic: SoundAndHaptic? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        soundAndHaptic = SoundAndHaptic(this)
        val profileManager = PlayerProfileManager(this)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MobaDarkBackground
                ) {
                    MobaApp(
                        profileManager = profileManager,
                        soundAndHaptic = soundAndHaptic
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundAndHaptic?.release()
    }
}

@Composable
fun MobaApp(
    profileManager: PlayerProfileManager,
    soundAndHaptic: SoundAndHaptic?
) {
    var currentScreen by remember { mutableStateOf(ScreenState.HOME) }
    var selectedMode by remember { mutableStateOf(GameMode.FIVE_VS_FIVE) }
    var selectedHero by remember { mutableStateOf(HeroCatalog.heroes.first()) }
    var activeEngine by remember { mutableStateOf<GameEngine?>(null) }

    // Post-game result state
    var postGameVictory by remember { mutableStateOf(false) }
    var postGameKills by remember { mutableStateOf(0) }
    var postGameDeaths by remember { mutableStateOf(0) }
    var postGameAssists by remember { mutableStateOf(0) }
    var postGameGold by remember { mutableStateOf(0) }

    when (currentScreen) {
        ScreenState.HOME -> {
            HomeScreen(
                profileManager = profileManager,
                onSelectMode = { mode ->
                    selectedMode = mode
                    currentScreen = ScreenState.HERO_SELECT
                },
                onOpenHeroCodex = { currentScreen = ScreenState.HERO_CODEX },
                onOpenItemShop = { currentScreen = ScreenState.ITEM_SHOP },
                onOpenProfile = { currentScreen = ScreenState.PROFILE }
            )
        }

        ScreenState.HERO_SELECT -> {
            HeroSelectScreen(
                gameMode = selectedMode,
                onBack = { currentScreen = ScreenState.HOME },
                onStartBattle = { hero, talent ->
                    selectedHero = hero
                    activeEngine = GameEngine(hero, selectedMode, soundAndHaptic)
                    currentScreen = ScreenState.BATTLE
                }
            )
        }

        ScreenState.BATTLE -> {
            activeEngine?.let { engine ->
                BattleScreen(
                    gameEngine = engine,
                    onMatchEnd = { isVictory, kills, deaths, assists, goldEarned ->
                        postGameVictory = isVictory
                        postGameKills = kills
                        postGameDeaths = deaths
                        postGameAssists = assists
                        postGameGold = goldEarned

                        // Save match record in profile
                        profileManager.recordMatch(
                            heroId = selectedHero.id,
                            heroName = selectedHero.name,
                            kills = kills,
                            deaths = deaths,
                            assists = assists,
                            isVictory = isVictory,
                            isMvp = isVictory && kills >= 3,
                            mode = selectedMode,
                            goldEarned = goldEarned
                        )

                        currentScreen = ScreenState.POST_GAME
                    },
                    onSurrender = {
                        currentScreen = ScreenState.HOME
                    }
                )
            } ?: run {
                currentScreen = ScreenState.HOME
            }
        }

        ScreenState.POST_GAME -> {
            VictoryDefeatScreen(
                isVictory = postGameVictory,
                hero = selectedHero,
                kills = postGameKills,
                deaths = postGameDeaths,
                assists = postGameAssists,
                goldEarned = postGameGold,
                onPlayAgain = {
                    activeEngine = GameEngine(selectedHero, selectedMode, soundAndHaptic)
                    currentScreen = ScreenState.BATTLE
                },
                onGoHome = {
                    currentScreen = ScreenState.HOME
                }
            )
        }

        ScreenState.HERO_CODEX -> {
            HeroCodexScreen(
                onBack = { currentScreen = ScreenState.HOME },
                onSelectHeroForBattle = { hero ->
                    selectedHero = hero
                    currentScreen = ScreenState.HERO_SELECT
                }
            )
        }

        ScreenState.ITEM_SHOP -> {
            ItemShopCodexScreen(
                onBack = { currentScreen = ScreenState.HOME }
            )
        }

        ScreenState.PROFILE -> {
            ProfileScreen(
                profileManager = profileManager,
                onBack = { currentScreen = ScreenState.HOME }
            )
        }
    }
}
