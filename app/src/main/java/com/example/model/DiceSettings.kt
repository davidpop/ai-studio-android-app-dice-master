package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AppThemeMode

enum class DiceColorChoice(val hex: Long, val displayNameEn: String, val displayNameFr: String) {
  PURPLE(0xFF451EBB, "Purple", "Violet"),
  TEAL(0xFF00677F, "Teal", "Sarcelle"),
  RED(0xFFBA1A1A, "Crimson Red", "Rouge"),
  PINK(0xFFD81B60, "Girly Pink", "Rose Girly"),
  COLD_STEEL(0xFF0284C7, "Cold Steel", "Métal Glacé"),
  BROWN(0xFF713300, "Bronze Brown", "Brun"),
  BLACK(0xFF191C1D, "Obsidian Black", "Noir");

  val color: Color get() = Color(hex)
}

enum class DiceTextureChoice(val id: String, val titleEn: String, val titleFr: String, val titleDe: String) {
  SOLID_PLASTIC("solid", "3D Glossy Plastic", "Plastique 3D Glossy", "Glänzend 3D Kunststoff"),
  SWIRLED_MARBLE("marble", "Swirled Marble", "Marbre Tourbillon", "Marmor"),
  NEON_GLOW("neon", "Neon Glow", "Lueur Néon", "Neon Leuchten"),
  BRUSHED_METAL("metal", "Brushed Metal", "Métal Brossé", "Gebürstetes Metall");

  fun getTitle(lang: AppLanguage): String = when (lang) {
    AppLanguage.EN -> titleEn
    AppLanguage.FR -> titleFr
    AppLanguage.DE -> titleDe
  }
}

enum class AppLanguage(val code: String, val displayName: String) {
  EN("en", "English"),
  FR("fr", "Français"),
  DE("de", "Deutsch")
}

data class DiceSettings(
  val themeMode: AppThemeMode = AppThemeMode.LIGHT,
  val diceColor: DiceColorChoice = DiceColorChoice.PURPLE,
  val diceTexture: DiceTextureChoice = DiceTextureChoice.SOLID_PLASTIC,
  val language: AppLanguage = AppLanguage.FR, // Defaulting or keeping user choice
  val soundEffects: Boolean = true,
  val hapticFeedback: Boolean = false,
  val shakeToRoll: Boolean = true
)
