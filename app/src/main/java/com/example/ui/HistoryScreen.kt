package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RollHistoryEntity
import com.example.model.AppLanguage
import com.example.model.DiceSettings
import com.example.model.StringsLocalization
import com.example.ui.theme.HankenGroteskFontFamily
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.OutlineVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
  historyList: List<RollHistoryEntity>,
  settings: DiceSettings,
  onClearHistory: () -> Unit,
  onDeleteItem: (Long) -> Unit
) {
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
      actions = {
        if (historyList.isNotEmpty()) {
          IconButton(
            onClick = onClearHistory,
            modifier = Modifier.testTag("clear_history_button")
          ) {
            Icon(
              imageVector = Icons.Default.DeleteSweep,
              contentDescription = StringsLocalization.clearHistory(settings.language),
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      modifier = Modifier.shadow(2.dp)
    )

    // Main Content
    if (historyList.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(24.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.size(64.dp)
          )
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = StringsLocalization.historyEmpty(settings.language),
            fontFamily = HankenGroteskFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 20.dp)
          .testTag("history_list"),
        contentPadding = PaddingValues(top = 20.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        item {
          Text(
            text = StringsLocalization.historyTitle(settings.language),
            fontFamily = HankenGroteskFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp)
          )
        }

        items(historyList, key = { it.id }) { item ->
          HistoryCard(
            item = item,
            language = settings.language,
            onDelete = { onDeleteItem(item.id) }
          )
        }
      }
    }
  }
}

@Composable
fun HistoryCard(
  item: RollHistoryEntity,
  language: AppLanguage,
  onDelete: () -> Unit
) {
  val pillColor = Color(item.colorHex)
  val timeText = formatRelativeTime(item.timestamp, language)
  val individualRolls = item.rollsCommaSeparated.split(",").map { it.trim() }.filter { it.isNotEmpty() }

  Surface(
    shape = RoundedCornerShape(16.dp),
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 2.dp,
    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.6f)),
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .testTag("history_card_${item.id}")
  ) {
    Box(modifier = Modifier.fillMaxWidth()) {
      // Color Indicator Pill on Left
      Box(
        modifier = Modifier
          .align(Alignment.CenterStart)
          .width(8.dp)
          .fillMaxHeight()
          .background(pillColor)
      )

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 18.dp, end = 16.dp, top = 14.dp, bottom = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Left Column: Formula, timestamp, roll chips
        Column(
          modifier = Modifier.weight(1f)
        ) {
          // Top Row: Formula and relative timestamp
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = item.formula,
              fontFamily = JetBrainsMonoFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = timeText,
              fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.outline
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Second Row: Individual roll pills + modifier
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            individualRolls.take(6).forEach { rollStr ->
              Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.surfaceVariant)
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text(
                  text = rollStr,
                  fontFamily = JetBrainsMonoFontFamily,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }

            if (individualRolls.size > 6) {
              Text(
                text = "+${individualRolls.size - 6}",
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            if (item.modifier != 0) {
              Text(
                text = if (item.modifier > 0) "+${item.modifier}" else "${item.modifier}",
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Right Column: TOTAL label and large value
        Column(
          horizontalAlignment = Alignment.End,
          verticalArrangement = Arrangement.Center
        ) {
          Text(
            text = StringsLocalization.totalLabel(language),
            fontFamily = JetBrainsMonoFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            letterSpacing = 0.08.sp,
            color = MaterialTheme.colorScheme.outline
          )
          Text(
            text = item.total.toString(),
            fontFamily = HankenGroteskFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp,
            lineHeight = 36.sp,
            color = MaterialTheme.colorScheme.primary
          )
        }
      }
    }
  }
}

fun formatRelativeTime(timestamp: Long, language: AppLanguage): String {
  val diff = System.currentTimeMillis() - timestamp
  val seconds = diff / 1000
  val minutes = seconds / 60
  val hours = minutes / 60
  val days = hours / 24

  return when {
    minutes < 1 -> StringsLocalization.timeJustNow(language)
    minutes < 60 -> StringsLocalization.timeMinutesAgo(minutes, language)
    hours < 24 -> StringsLocalization.timeHoursAgo(hours, language)
    days <= 2 -> StringsLocalization.timeYesterday(language)
    else -> "${days}d ago"
  }
}
