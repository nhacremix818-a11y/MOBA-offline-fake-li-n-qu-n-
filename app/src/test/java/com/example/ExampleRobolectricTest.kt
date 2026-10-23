package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.HeroCatalog
import com.example.data.ItemCatalog
import com.example.data.PlayerProfileManager
import com.example.game.GameEngine
import com.example.model.GameMode
import com.example.model.HeroRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun testAppName() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Liên Quân Offline", appName)
    }

    @Test
    fun testHeroCatalogRoster() {
        assertTrue("Roster should have 20 heroes", HeroCatalog.heroes.size >= 20)
        val valhein = HeroCatalog.getHeroById("valhein")
        assertEquals("Valhein", valhein.name)
        assertEquals(HeroRole.XA_THU, valhein.role)

        val florentino = HeroCatalog.getHeroById("florentino")
        assertEquals("Florentino", florentino.name)
        assertEquals(HeroRole.DAU_SI, florentino.role)
    }

    @Test
    fun testItemCatalog() {
        val thanhKiem = ItemCatalog.getItemById("thanh_kiem")
        assertTrue(thanhKiem != null)
        assertEquals("Thánh Kiếm", thanhKiem?.name)
    }

    @Test
    fun testGameEngineInitialization() {
        val hero = HeroCatalog.getHeroById("valhein")
        val engine = GameEngine(hero, GameMode.FIVE_VS_FIVE)
        assertEquals(10, engine.entities.value.size) // 1 player + 4 allies + 5 enemies
        val player = engine.getPlayer()
        assertTrue(player != null)
        assertEquals("Valhein", player?.name)
    }

    @Test
    fun testJoystickDirection() {
        val hero = HeroCatalog.getHeroById("valhein")
        val engine = GameEngine(hero, GameMode.FIVE_VS_FIVE)
        val player = engine.getPlayer()!!
        val startX = player.x
        val startY = player.y

        // Push joystick UP on screen (screenVy = -1f) -> Forward towards Red Nexus
        engine.updateJoystick(0f, -1f)
        engine.tick(0.1f)
        assertTrue("Pushing UP should increase X forward", player.x > startX)
        assertTrue("Pushing UP should increase Y forward", player.y > startY)

        // Pull joystick DOWN on screen (screenVy = +1f) -> Backward towards Blue Base
        val forwardX = player.x
        val forwardY = player.y
        engine.updateJoystick(0f, 1f)
        engine.tick(0.1f)
        assertTrue("Pulling DOWN should decrease X backward", player.x < forwardX)
        assertTrue("Pulling DOWN should decrease Y backward", player.y < forwardY)
    }
}
