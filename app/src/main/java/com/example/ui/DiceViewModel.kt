package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.HapticController
import com.example.audio.SoundPlayer
import com.example.data.DiceDatabase
import com.example.data.DiceRepository
import com.example.data.RollHistoryEntity
import com.example.model.AppLanguage
import com.example.model.DiceColorChoice
import com.example.model.DiceSettings
import com.example.model.DiceTextureChoice
import com.example.model.DiceType
import com.example.model.DieItem
import com.example.ui.theme.AppThemeMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class NavTab {
  HOME, HISTORY, SETTINGS
}

class DiceViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: DiceRepository
  private val soundPlayer = SoundPlayer()
  private val hapticController = HapticController(application)

  private val _activeTab = MutableStateFlow(NavTab.HOME)
  val activeTab: StateFlow<NavTab> = _activeTab.asStateFlow()

  // Initial dice matching the exact screenshot: D6 (4) and D20 (15)
  private val _dice = MutableStateFlow<List<DieItem>>(
    listOf(
      DieItem(type = DiceType.D6, value = 4),
      DieItem(type = DiceType.D20, value = 15)
    )
  )
  val dice: StateFlow<List<DieItem>> = _dice.asStateFlow()

  private val _modifier = MutableStateFlow(0)
  val modifier: StateFlow<Int> = _modifier.asStateFlow()

  private val _isRollingAll = MutableStateFlow(false)
  val isRollingAll: StateFlow<Boolean> = _isRollingAll.asStateFlow()

  private val _settings = MutableStateFlow(
    DiceSettings(
      themeMode = AppThemeMode.LIGHT,
      diceColor = DiceColorChoice.PURPLE,
      diceTexture = DiceTextureChoice.SOLID_PLASTIC,
      language = AppLanguage.EN,
      soundEffects = true,
      hapticFeedback = false
    )
  )
  val settings: StateFlow<DiceSettings> = _settings.asStateFlow()

  init {
    val database = DiceDatabase.getDatabase(application)
    repository = DiceRepository(database.rollHistoryDao())
    viewModelScope.launch {
      repository.seedInitialDataIfEmpty()
    }
  }

  val historyList: StateFlow<List<RollHistoryEntity>> = repository.allHistory
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  fun selectTab(tab: NavTab) {
    _activeTab.value = tab
    hapticController.performClickHaptic(_settings.value.hapticFeedback)
  }

  fun addDie(type: DiceType) {
    val newDie = DieItem(
      type = type,
      value = Random.nextInt(1, type.sides + 1)
    )
    _dice.value = _dice.value + newDie
    soundPlayer.playPlacementSound(type, _settings.value.soundEffects)
    hapticController.performClickHaptic(_settings.value.hapticFeedback)
  }

  fun removeDie(id: String) {
    _dice.value = _dice.value.filter { it.id != id }
    hapticController.performClickHaptic(_settings.value.hapticFeedback)
  }

  fun clearDice() {
    _dice.value = emptyList()
    hapticController.performClickHaptic(_settings.value.hapticFeedback)
  }

  fun resetDice() {
    _dice.value = listOf(
      DieItem(type = DiceType.D6, value = 4),
      DieItem(type = DiceType.D20, value = 15)
    )
    _modifier.value = 0
    hapticController.performClickHaptic(_settings.value.hapticFeedback)
  }

  fun setModifier(delta: Int) {
    _modifier.value = (_modifier.value + delta).coerceIn(-99, 99)
    hapticController.performClickHaptic(_settings.value.hapticFeedback)
  }

  fun resetModifier() {
    _modifier.value = 0
  }

  fun rollSingleDie(dieId: String) {
    val currentList = _dice.value
    val targetIndex = currentList.indexOfFirst { it.id == dieId }
    if (targetIndex == -1) return

    viewModelScope.launch {
      val target = currentList[targetIndex]
      soundPlayer.playDieRollSound(target.type, _settings.value.soundEffects)
      hapticController.performRollHaptic(_settings.value.hapticFeedback)

      // Animate die
      for (step in 0..5) {
        val tempVal = Random.nextInt(1, target.type.sides + 1)
        _dice.value = _dice.value.toMutableList().also {
          it[targetIndex] = target.copy(value = tempVal, isRolling = true)
        }
        delay(40L)
      }
      val finalVal = Random.nextInt(1, target.type.sides + 1)
      _dice.value = _dice.value.toMutableList().also {
        it[targetIndex] = target.copy(value = finalVal, isRolling = false)
      }
    }
  }

  fun rollAll() {
    if (_dice.value.isEmpty() || _isRollingAll.value) return
    viewModelScope.launch {
      _isRollingAll.value = true
      soundPlayer.playRollAllSound(_dice.value.map { it.type }, _settings.value.soundEffects)
      hapticController.performRollHaptic(_settings.value.hapticFeedback)

      val initialList = _dice.value
      // Quick tumble animation steps
      for (frame in 0 until 8) {
        _dice.value = initialList.map { die ->
          die.copy(
            value = Random.nextInt(1, die.type.sides + 1),
            isRolling = true
          )
        }
        delay(45L)
      }

      // Final roll values
      val finalDice = initialList.map { die ->
        die.copy(
          value = Random.nextInt(1, die.type.sides + 1),
          isRolling = false
        )
      }
      _dice.value = finalDice
      _isRollingAll.value = false

      // Generate formula and record history
      saveRollToHistory(finalDice, _modifier.value)
    }
  }

  private suspend fun saveRollToHistory(diceList: List<DieItem>, mod: Int) {
    if (diceList.isEmpty()) return

    // Group dice by type to build formula e.g. "2d20 + 5" or "1d6 + 1d20"
    val countsByType = diceList.groupBy { it.type }.toSortedMap(compareBy { it.sides })
    val formulaParts = countsByType.map { (type, list) ->
      "${list.size}${type.label.lowercase()}"
    }.joinToString(" + ")

    val fullFormula = when {
      mod > 0 -> "$formulaParts + $mod"
      mod < 0 -> "$formulaParts - ${kotlin.math.abs(mod)}"
      else -> formulaParts
    }

    val rollValues = diceList.map { it.value }
    val total = rollValues.sum() + mod

    repository.insert(
      RollHistoryEntity(
        formula = fullFormula,
        rollsCommaSeparated = rollValues.joinToString(", "),
        modifier = mod,
        total = total,
        colorHex = _settings.value.diceColor.hex,
        timestamp = System.currentTimeMillis()
      )
    )
  }

  fun clearHistory() {
    viewModelScope.launch {
      repository.clear()
      hapticController.performClickHaptic(_settings.value.hapticFeedback)
    }
  }

  fun deleteHistoryItem(id: Long) {
    viewModelScope.launch {
      repository.delete(id)
      hapticController.performClickHaptic(_settings.value.hapticFeedback)
    }
  }

  fun updateThemeMode(themeMode: AppThemeMode) {
    val autoColor = when (themeMode) {
      AppThemeMode.PINK -> DiceColorChoice.PINK
      AppThemeMode.METAL -> DiceColorChoice.COLD_STEEL
      AppThemeMode.DARK -> DiceColorChoice.PURPLE
      AppThemeMode.LIGHT, AppThemeMode.SYSTEM -> DiceColorChoice.PURPLE
    }
    _settings.value = _settings.value.copy(
      themeMode = themeMode,
      diceColor = autoColor
    )
  }

  fun updateDiceColor(color: DiceColorChoice) {
    _settings.value = _settings.value.copy(diceColor = color)
  }

  fun updateDiceTexture(texture: DiceTextureChoice) {
    _settings.value = _settings.value.copy(diceTexture = texture)
  }

  fun updateLanguage(lang: AppLanguage) {
    _settings.value = _settings.value.copy(language = lang)
  }

  fun updateSoundEffects(enabled: Boolean) {
    _settings.value = _settings.value.copy(soundEffects = enabled)
  }

  fun updateHapticFeedback(enabled: Boolean) {
    _settings.value = _settings.value.copy(hapticFeedback = enabled)
  }

  fun updateShakeToRoll(enabled: Boolean) {
    _settings.value = _settings.value.copy(shakeToRoll = enabled)
  }
}
