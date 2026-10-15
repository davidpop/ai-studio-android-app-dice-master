package com.example.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.DiceColorChoice
import com.example.model.DiceSettings
import com.example.model.DiceTextureChoice
import com.example.model.StringsLocalization
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.HankenGroteskFontFamily
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.PrimaryPurple

import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.SettingsSuggest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  settings: DiceSettings,
  onUpdateTheme: (AppThemeMode) -> Unit,
  onUpdateDiceColor: (DiceColorChoice) -> Unit,
  onUpdateDiceTexture: (DiceTextureChoice) -> Unit,
  onUpdateLanguage: (AppLanguage) -> Unit,
  onUpdateSoundEffects: (Boolean) -> Unit,
  onUpdateHapticFeedback: (Boolean) -> Unit,
  onUpdateShakeToRoll: (Boolean) -> Unit
) {
  val lang = settings.language

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // TopAppBar
    TopAppBar(
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Casino,
            contentDescription = "Casino Icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = StringsLocalization.appTitle(),
            fontFamily = HankenGroteskFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary
          )
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      modifier = Modifier.shadow(2.dp)
    )

    // Scrollable Settings Content
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 24.dp)
        .padding(bottom = 80.dp),
      verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
      // Header
      Column {
        Text(
          text = StringsLocalization.settingsTitle(lang),
          fontFamily = HankenGroteskFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 28.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = StringsLocalization.settingsSubtitle(lang),
          fontSize = 15.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      // SECTION 1: APPEARANCE
      SettingsCard(
        icon = Icons.Default.Palette,
        sectionTitle = StringsLocalization.appearanceSection(lang)
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = StringsLocalization.appTheme(lang),
            fontFamily = HankenGroteskFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = StringsLocalization.appThemeDesc(lang),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(14.dp))

          // Theme Selector (Light, Dark, System + Pink Girly, Cold Metal)
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              ThemeSegment(
                title = StringsLocalization.themeLight(lang),
                icon = Icons.Default.LightMode,
                isSelected = settings.themeMode == AppThemeMode.LIGHT,
                onClick = { onUpdateTheme(AppThemeMode.LIGHT) },
                modifier = Modifier.weight(1f)
              )
              ThemeSegment(
                title = StringsLocalization.themeDark(lang),
                icon = Icons.Default.DarkMode,
                isSelected = settings.themeMode == AppThemeMode.DARK,
                onClick = { onUpdateTheme(AppThemeMode.DARK) },
                modifier = Modifier.weight(1f)
              )
              ThemeSegment(
                title = StringsLocalization.themeSystem(lang),
                icon = Icons.Default.SettingsSuggest,
                isSelected = settings.themeMode == AppThemeMode.SYSTEM,
                onClick = { onUpdateTheme(AppThemeMode.SYSTEM) },
                modifier = Modifier.weight(1f)
              )
            }
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              ThemeSegment(
                title = StringsLocalization.themePink(lang),
                icon = Icons.Default.Favorite,
                isSelected = settings.themeMode == AppThemeMode.PINK,
                onClick = { onUpdateTheme(AppThemeMode.PINK) },
                modifier = Modifier.weight(1f)
              )
              ThemeSegment(
                title = StringsLocalization.themeMetal(lang),
                icon = Icons.Default.AcUnit,
                isSelected = settings.themeMode == AppThemeMode.METAL,
                onClick = { onUpdateTheme(AppThemeMode.METAL) },
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }

      // SECTION 2: DICE STYLING
      SettingsCard(
        icon = Icons.Default.Casino,
        sectionTitle = StringsLocalization.diceStylingSection(lang)
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          // Dice Color
          Text(
            text = StringsLocalization.primaryDiceColor(lang),
            fontFamily = HankenGroteskFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = StringsLocalization.primaryDiceColorDesc(lang),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(14.dp))

          // Color Swatches
          Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            DiceColorChoice.entries.forEach { choice ->
              val isSelected = settings.diceColor == choice
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(choice.color)
                  .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else OutlineVariant.copy(alpha = 0.5f),
                    shape = CircleShape
                  )
                  .clickable { onUpdateDiceColor(choice) }
                  .testTag("color_swatch_${choice.name.lowercase()}")
              ) {
                if (isSelected) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          // Dice Material Texture Dropdown
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = StringsLocalization.diceMaterialTexture(lang),
                fontFamily = HankenGroteskFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = StringsLocalization.diceMaterialTextureDesc(lang),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            var textureMenuExpanded by remember { mutableStateOf(false) }

            Box {
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                  .clip(RoundedCornerShape(10.dp))
                  .clickable { textureMenuExpanded = true }
                  .testTag("texture_dropdown_button")
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                  Text(
                    text = settings.diceTexture.getTitle(lang),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }

              DropdownMenu(
                expanded = textureMenuExpanded,
                onDismissRequest = { textureMenuExpanded = false }
              ) {
                DiceTextureChoice.entries.forEach { textureChoice ->
                  DropdownMenuItem(
                    text = { Text(textureChoice.getTitle(lang)) },
                    onClick = {
                      onUpdateDiceTexture(textureChoice)
                      textureMenuExpanded = false
                    }
                  )
                }
              }
            }
          }
        }
      }

      // SECTION 3: LANGUAGE SETTINGS
      SettingsCard(
        icon = Icons.Default.Language,
        sectionTitle = StringsLocalization.languageSettingsSection(lang)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = StringsLocalization.appLanguage(lang),
              fontFamily = HankenGroteskFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = StringsLocalization.appLanguageDesc(lang),
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          var langMenuExpanded by remember { mutableStateOf(false) }

          Box {
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable { langMenuExpanded = true }
                .testTag("language_dropdown_button")
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
              ) {
                Text(
                  text = settings.language.displayName,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Medium,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                  imageVector = Icons.Default.ExpandMore,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(18.dp)
                )
              }
            }

            DropdownMenu(
              expanded = langMenuExpanded,
              onDismissRequest = { langMenuExpanded = false }
            ) {
              AppLanguage.entries.forEach { appLang ->
                DropdownMenuItem(
                  text = { Text(appLang.displayName) },
                  onClick = {
                    onUpdateLanguage(appLang)
                    langMenuExpanded = false
                  }
                )
              }
            }
          }
        }
      }

      // SECTION 4: AUDIO & FEEDBACK
      SettingsCard(
        icon = Icons.Default.VolumeUp,
        sectionTitle = StringsLocalization.audioFeedbackSection(lang)
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          // Sound Effects
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = StringsLocalization.soundEffects(lang),
                  fontFamily = HankenGroteskFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                  imageVector = Icons.Default.VolumeUp,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(16.dp)
                )
              }
              Text(
                text = StringsLocalization.soundEffectsDesc(lang),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Switch(
              checked = settings.soundEffects,
              onCheckedChange = onUpdateSoundEffects,
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryPurple
              ),
              modifier = Modifier.testTag("sound_effects_switch")
            )
          }

          Spacer(modifier = Modifier.height(16.dp))
          HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
          Spacer(modifier = Modifier.height(16.dp))

          // Haptic Feedback
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = StringsLocalization.hapticFeedback(lang),
                  fontFamily = HankenGroteskFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                  imageVector = Icons.Default.Vibration,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(16.dp)
                )
              }
              Text(
                text = StringsLocalization.hapticFeedbackDesc(lang),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Switch(
              checked = settings.hapticFeedback,
              onCheckedChange = onUpdateHapticFeedback,
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryPurple
              ),
              modifier = Modifier.testTag("haptic_feedback_switch")
            )
          }

          Spacer(modifier = Modifier.height(16.dp))
          HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
          Spacer(modifier = Modifier.height(16.dp))

          // Shake to Roll Toggle
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = StringsLocalization.shakeToRoll(lang),
                  fontFamily = HankenGroteskFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                  imageVector = Icons.Default.ScreenRotation,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(16.dp)
                )
              }
              Text(
                text = StringsLocalization.shakeToRollDesc(lang),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Switch(
              checked = settings.shakeToRoll,
              onCheckedChange = onUpdateShakeToRoll,
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryPurple
              ),
              modifier = Modifier.testTag("shake_to_roll_switch")
            )
          }
        }
      }
    }
  }
}

@Composable
fun SettingsCard(
  icon: ImageVector,
  sectionTitle: String,
  content: @Composable () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 2.dp,
    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.6f)),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      // Header row
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 12.dp)
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = sectionTitle,
          fontFamily = JetBrainsMonoFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 12.sp,
          letterSpacing = 0.08.sp,
          color = MaterialTheme.colorScheme.primary
        )
      }
      HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
      Spacer(modifier = Modifier.height(14.dp))
      content()
    }
  }
}

@Composable
fun ThemeSegment(
  title: String,
  icon: ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val bgColor by animateColorAsState(
    if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
    label = "theme_seg_bg"
  )
  val textColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(bgColor)
      .clickable(onClick = onClick)
      .padding(vertical = 8.dp, horizontal = 6.dp)
      .testTag("theme_segment_${title.lowercase()}")
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = textColor,
        modifier = Modifier.size(15.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = title,
        fontFamily = JetBrainsMonoFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        color = textColor
      )
    }
  }
}
