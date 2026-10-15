package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DiceSettings
import com.example.model.DiceType
import com.example.model.DieItem
import com.example.model.StringsLocalization
import com.example.ui.components.DieCard
import com.example.ui.theme.HankenGroteskFontFamily
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.SecondaryContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  dice: List<DieItem>,
  modifierValue: Int,
  isRolling: Boolean,
  settings: DiceSettings,
  onAddDie: (DiceType) -> Unit,
  onRemoveDie: (String) -> Unit,
  onRollDie: (String) -> Unit,
  onRollAll: () -> Unit,
  onClearDice: () -> Unit,
  onResetDice: () -> Unit,
  onSetModifier: (Int) -> Unit,
  onResetModifier: () -> Unit
) {
  val total = dice.sumOf { it.value } + modifierValue
  var selectedChipType by remember { mutableStateOf<DiceType?>(DiceType.D6) }
  var showModifierPicker by remember { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
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
        actions = {
          // Reset button
          IconButton(
            onClick = onResetDice,
            modifier = Modifier.testTag("reset_dice_button")
          ) {
            Icon(
              imageVector = Icons.Default.RestartAlt,
              contentDescription = StringsLocalization.resetDice(settings.language),
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          // Clear button if dice present
          if (dice.isNotEmpty()) {
            IconButton(
              onClick = onClearDice,
              modifier = Modifier.testTag("clear_dice_button")
            ) {
              Icon(
                imageVector = Icons.Default.DeleteSweep,
                contentDescription = StringsLocalization.clearDice(settings.language),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          // Total Badge Pill
          Surface(
            shape = RoundedCornerShape(9999.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
              .padding(end = 16.dp)
              .testTag("total_badge")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
              Text(
                text = StringsLocalization.totalBadge(settings.language),
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = total.toString(),
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.shadow(2.dp)
      )

      // Main Dice Container
      if (dice.isEmpty()) {
        Box(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(24.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Default.Casino,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.outlineVariant,
              modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = if (settings.language == com.example.model.AppLanguage.FR)
                "Aucun dé sur la table. Choisissez un dé ci-dessous !"
              else "No dice on the table. Tap a die chip below to add!",
              fontFamily = HankenGroteskFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 16.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      } else {
        LazyVerticalGrid(
          columns = GridCells.Fixed(2),
          contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 20.dp,
            bottom = 210.dp // ensure ample space above floating chips and FAB
          ),
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp),
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .testTag("dice_grid")
        ) {
          items(dice, key = { it.id }) { die ->
            DieCard(
              die = die,
              diceColor = settings.diceColor,
              diceTexture = settings.diceTexture,
              themeMode = settings.themeMode,
              onRoll = { onRollDie(die.id) },
              onRemove = { onRemoveDie(die.id) }
            )
          }
        }
      }
    }

    // Floating Controls Bar (Dice Chips & Roll FAB)
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 12.dp)
        .padding(horizontal = 16.dp)
    ) {
      // Modifier row if enabled or toggleable
      AnimatedVisibility(
        visible = showModifierPicker || modifierValue != 0,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = MaterialTheme.colorScheme.surface,
          shadowElevation = 4.dp,
          border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant),
          modifier = Modifier
            .padding(bottom = 8.dp)
            .testTag("modifier_pill")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
          ) {
            IconButton(
              onClick = { onSetModifier(-1) },
              modifier = Modifier.size(36.dp).testTag("modifier_minus")
            ) {
              Icon(Icons.Default.Remove, contentDescription = "Minus", modifier = Modifier.size(16.dp))
            }
            Text(
              text = if (modifierValue >= 0) "+$modifierValue" else "$modifierValue",
              fontFamily = JetBrainsMonoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(
              onClick = { onSetModifier(1) },
              modifier = Modifier.size(36.dp).testTag("modifier_plus")
            ) {
              Icon(Icons.Default.Add, contentDescription = "Plus", modifier = Modifier.size(16.dp))
            }
          }
        }
      }

      // Dice Selectors Chips
      Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
        shadowElevation = 4.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("dice_selector_chips_container")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          val diceTypes = listOf(
            DiceType.D4,
            DiceType.D6,
            DiceType.D8,
            DiceType.D10,
            DiceType.D12,
            DiceType.D20,
            DiceType.D100
          )

          diceTypes.forEach { type ->
            val isSelected = selectedChipType == type
            val chipBg = if (isSelected) SecondaryContainer else MaterialTheme.colorScheme.surfaceContainerHighest
            val chipContentColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface

            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .padding(horizontal = 4.dp)
                .size(46.dp)
                .clip(CircleShape)
                .background(chipBg)
                .clickable {
                  selectedChipType = type
                  onAddDie(type)
                }
                .testTag("add_die_${type.label.lowercase()}")
            ) {
              Text(
                text = type.label,
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = chipContentColor
              )
            }
          }

          // Modifier button chip
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
              .padding(horizontal = 4.dp)
              .size(46.dp)
              .clip(CircleShape)
              .background(if (modifierValue != 0) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHighest)
              .clickable {
                showModifierPicker = !showModifierPicker
              }
              .testTag("toggle_modifier_button")
          ) {
            Text(
              text = if (modifierValue == 0) "+/-" else if (modifierValue > 0) "+$modifierValue" else "$modifierValue",
              fontFamily = JetBrainsMonoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              color = if (modifierValue != 0) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Roll All FAB
      Button(
        onClick = onRollAll,
        enabled = dice.isNotEmpty() && !isRolling,
        shape = RoundedCornerShape(9999.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color.Transparent,
          contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
          .width(200.dp)
          .height(52.dp)
          .shadow(
            8.dp,
            RoundedCornerShape(9999.dp),
            ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
          )
          .background(
            brush = Brush.verticalGradient(
              colors = listOf(
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.primary
              )
            ),
            shape = RoundedCornerShape(9999.dp)
          )
          .testTag("roll_all_button")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center,
          modifier = Modifier.fillMaxSize()
        ) {
          Icon(
            imageVector = Icons.Default.Sync,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = StringsLocalization.rollAllButton(settings.language),
            fontFamily = JetBrainsMonoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            letterSpacing = 0.08.sp,
            color = Color.White
          )
        }
      }
    }
  }
}
