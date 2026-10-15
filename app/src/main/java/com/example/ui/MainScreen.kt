package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.StringsLocalization
import com.example.sensor.ShakeDetector
import com.example.ui.theme.DiceMasterTheme
import com.example.ui.theme.HankenGroteskFontFamily
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.SecondaryContainer

@Composable
fun MainScreen(viewModel: DiceViewModel) {
  val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
  val settings by viewModel.settings.collectAsStateWithLifecycle()
  val dice by viewModel.dice.collectAsStateWithLifecycle()
  val modifierValue by viewModel.modifier.collectAsStateWithLifecycle()
  val isRolling by viewModel.isRollingAll.collectAsStateWithLifecycle()
  val historyList by viewModel.historyList.collectAsStateWithLifecycle()

  val context = LocalContext.current
  DisposableEffect(settings.shakeToRoll, dice, isRolling) {
    val shakeDetector = ShakeDetector(context) {
      if (settings.shakeToRoll && dice.isNotEmpty() && !isRolling) {
        viewModel.rollAll()
      }
    }
    if (settings.shakeToRoll) {
      shakeDetector.start()
    }
    onDispose {
      shakeDetector.stop()
    }
  }

  // Back handling: If on History or Settings, pressing back navigates to Home
  BackHandler(enabled = activeTab != NavTab.HOME) {
    viewModel.selectTab(NavTab.HOME)
  }

  DiceMasterTheme(themeMode = settings.themeMode) {
    val configuration = LocalConfiguration.current
    val isExpandedScreen = configuration.screenWidthDp >= 720

    if (isExpandedScreen) {
      // Tablet / Landscape Canonical Layout: NavigationRail on Left
      Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail(
          header = {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.padding(vertical = 16.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Casino,
                contentDescription = "DiceMaster",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = StringsLocalization.appTitle(),
                fontFamily = HankenGroteskFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
              )
            }
          },
          containerColor = MaterialTheme.colorScheme.surface,
          contentColor = MaterialTheme.colorScheme.onSurface
        ) {
          NavigationRailItem(
            selected = activeTab == NavTab.HOME,
            onClick = { viewModel.selectTab(NavTab.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = StringsLocalization.navHome(settings.language)) },
            label = { Text(StringsLocalization.navHome(settings.language), fontFamily = JetBrainsMonoFontFamily) },
            colors = NavigationRailItemDefaults.colors(
              selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
              selectedTextColor = MaterialTheme.colorScheme.primary,
              indicatorColor = SecondaryContainer
            ),
            modifier = Modifier.testTag("rail_tab_home")
          )
          NavigationRailItem(
            selected = activeTab == NavTab.HISTORY,
            onClick = { viewModel.selectTab(NavTab.HISTORY) },
            icon = { Icon(Icons.Default.History, contentDescription = StringsLocalization.navHistory(settings.language)) },
            label = { Text(StringsLocalization.navHistory(settings.language), fontFamily = JetBrainsMonoFontFamily) },
            colors = NavigationRailItemDefaults.colors(
              selectedIconColor = Color.White,
              selectedTextColor = MaterialTheme.colorScheme.primary,
              indicatorColor = PrimaryContainer
            ),
            modifier = Modifier.testTag("rail_tab_history")
          )
          NavigationRailItem(
            selected = activeTab == NavTab.SETTINGS,
            onClick = { viewModel.selectTab(NavTab.SETTINGS) },
            icon = { Icon(Icons.Default.Settings, contentDescription = StringsLocalization.navSettings(settings.language)) },
            label = { Text(StringsLocalization.navSettings(settings.language), fontFamily = JetBrainsMonoFontFamily) },
            colors = NavigationRailItemDefaults.colors(
              selectedIconColor = Color.White,
              selectedTextColor = MaterialTheme.colorScheme.primary,
              indicatorColor = PrimaryContainer
            ),
            modifier = Modifier.testTag("rail_tab_settings")
          )
        }

        Box(modifier = Modifier.fillMaxSize()) {
          Crossfade(targetState = activeTab, label = "tab_crossfade_rail") { tab ->
            when (tab) {
              NavTab.HOME -> HomeScreen(
                dice = dice,
                modifierValue = modifierValue,
                isRolling = isRolling,
                settings = settings,
                onAddDie = { viewModel.addDie(it) },
                onRemoveDie = { viewModel.removeDie(it) },
                onRollDie = { viewModel.rollSingleDie(it) },
                onRollAll = { viewModel.rollAll() },
                onClearDice = { viewModel.clearDice() },
                onResetDice = { viewModel.resetDice() },
                onSetModifier = { viewModel.setModifier(it) },
                onResetModifier = { viewModel.resetModifier() }
              )
              NavTab.HISTORY -> HistoryScreen(
                historyList = historyList,
                settings = settings,
                onClearHistory = { viewModel.clearHistory() },
                onDeleteItem = { viewModel.deleteHistoryItem(it) }
              )
              NavTab.SETTINGS -> SettingsScreen(
                settings = settings,
                onUpdateTheme = { viewModel.updateThemeMode(it) },
                onUpdateDiceColor = { viewModel.updateDiceColor(it) },
                onUpdateDiceTexture = { viewModel.updateDiceTexture(it) },
                onUpdateLanguage = { viewModel.updateLanguage(it) },
                onUpdateSoundEffects = { viewModel.updateSoundEffects(it) },
                onUpdateHapticFeedback = { viewModel.updateHapticFeedback(it) },
                onUpdateShakeToRoll = { viewModel.updateShakeToRoll(it) }
              )
            }
          }
        }
      }
    } else {
      // Standard Phone Layout: Full bleed with BottomNavBar
      Scaffold(
        bottomBar = {
          DiceMasterBottomNavBar(
            activeTab = activeTab,
            language = settings.language,
            onTabSelected = { viewModel.selectTab(it) }
          )
        }
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
          Crossfade(targetState = activeTab, label = "tab_crossfade") { tab ->
            when (tab) {
              NavTab.HOME -> HomeScreen(
                dice = dice,
                modifierValue = modifierValue,
                isRolling = isRolling,
                settings = settings,
                onAddDie = { viewModel.addDie(it) },
                onRemoveDie = { viewModel.removeDie(it) },
                onRollDie = { viewModel.rollSingleDie(it) },
                onRollAll = { viewModel.rollAll() },
                onClearDice = { viewModel.clearDice() },
                onResetDice = { viewModel.resetDice() },
                onSetModifier = { viewModel.setModifier(it) },
                onResetModifier = { viewModel.resetModifier() }
              )
              NavTab.HISTORY -> HistoryScreen(
                historyList = historyList,
                settings = settings,
                onClearHistory = { viewModel.clearHistory() },
                onDeleteItem = { viewModel.deleteHistoryItem(it) }
              )
              NavTab.SETTINGS -> SettingsScreen(
                settings = settings,
                onUpdateTheme = { viewModel.updateThemeMode(it) },
                onUpdateDiceColor = { viewModel.updateDiceColor(it) },
                onUpdateDiceTexture = { viewModel.updateDiceTexture(it) },
                onUpdateLanguage = { viewModel.updateLanguage(it) },
                onUpdateSoundEffects = { viewModel.updateSoundEffects(it) },
                onUpdateHapticFeedback = { viewModel.updateHapticFeedback(it) },
                onUpdateShakeToRoll = { viewModel.updateShakeToRoll(it) }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun DiceMasterBottomNavBar(
  activeTab: NavTab,
  language: com.example.model.AppLanguage,
  onTabSelected: (NavTab) -> Unit
) {
  Surface(
    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 8.dp,
    modifier = Modifier
      .fillMaxWidth()
      .navigationBarsPadding()
      .testTag("bottom_nav_bar")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Home Tab
      val isHome = activeTab == NavTab.HOME
      BottomNavItem(
        label = StringsLocalization.navHome(language),
        icon = Icons.Default.Home,
        isActive = isHome,
        activeBg = SecondaryContainer,
        activeColor = MaterialTheme.colorScheme.onSecondaryContainer,
        onClick = { onTabSelected(NavTab.HOME) },
        testTag = "nav_home"
      )

      // History Tab
      val isHistory = activeTab == NavTab.HISTORY
      BottomNavItem(
        label = StringsLocalization.navHistory(language),
        icon = Icons.Default.History,
        isActive = isHistory,
        activeBg = PrimaryContainer,
        activeColor = Color.White,
        onClick = { onTabSelected(NavTab.HISTORY) },
        testTag = "nav_history"
      )

      // Settings Tab
      val isSettings = activeTab == NavTab.SETTINGS
      BottomNavItem(
        label = StringsLocalization.navSettings(language),
        icon = Icons.Default.Settings,
        isActive = isSettings,
        activeBg = PrimaryContainer,
        activeColor = Color.White,
        onClick = { onTabSelected(NavTab.SETTINGS) },
        testTag = "nav_settings"
      )
    }
  }
}

@Composable
fun BottomNavItem(
  label: String,
  icon: ImageVector,
  isActive: Boolean,
  activeBg: Color,
  activeColor: Color,
  onClick: () -> Unit,
  testTag: String
) {
  val contentColor = if (isActive) activeColor else MaterialTheme.colorScheme.onSurfaceVariant

  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .clip(CircleShape)
      .background(if (isActive) activeBg else Color.Transparent)
      .clickable(onClick = onClick)
      .padding(horizontal = 20.dp, vertical = 6.dp)
      .testTag(testTag)
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = contentColor,
        modifier = Modifier.size(22.dp)
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = label,
        fontFamily = JetBrainsMonoFontFamily,
        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 0.05.sp,
        color = contentColor
      )
    }
  }
}
