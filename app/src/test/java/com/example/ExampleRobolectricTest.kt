package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.DiceType
import com.example.model.DieItem
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
  fun readAppNameFromContext() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("DiceMaster", appName)
  }

  @Test
  fun testDiceTypes() {
    assertEquals(6, DiceType.D6.sides)
    assertEquals(20, DiceType.D20.sides)
    assertEquals(4, DiceType.D4.sides)
    assertEquals(8, DiceType.D8.sides)
    assertEquals(10, DiceType.D10.sides)
    assertEquals(12, DiceType.D12.sides)
    assertEquals(100, DiceType.D100.sides)
  }

  @Test
  fun testDieItemCreation() {
    val die = DieItem(type = DiceType.D20, value = 15)
    assertEquals(DiceType.D20, die.type)
    assertEquals(15, die.value)
    assertTrue(die.id.isNotEmpty())
  }

  @Test
  fun testSoundPlayerDoesNotThrow() {
    val player = com.example.audio.SoundPlayer()
    // Should safely handle disabled and enabled triggers without crashing
    player.playDieRollSound(DiceType.D4, false)
    player.playDieRollSound(DiceType.D20, false)
    player.playRollAllSound(listOf(DiceType.D6, DiceType.D20), false)
    player.playPlacementSound(DiceType.D6, false)
  }

  @Test
  fun testShakeDetectorLifecycle() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    var shaken = false
    val detector = com.example.sensor.ShakeDetector(context) {
      shaken = true
    }
    detector.start()
    detector.stop()
    org.junit.Assert.assertFalse(shaken)
  }
}
