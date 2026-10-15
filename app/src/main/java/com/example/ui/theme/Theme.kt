package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

enum class AppThemeMode {
  LIGHT, DARK, SYSTEM, PINK, METAL
}

private val DarkColorScheme = darkColorScheme(
  primary = PrimaryFixedDim,
  onPrimary = PrimaryPurple,
  primaryContainer = PrimaryContainer,
  onPrimaryContainer = OnPrimaryContainer,
  secondary = SecondaryTeal,
  onSecondary = OnSecondary,
  secondaryContainer = SecondaryContainer,
  onSecondaryContainer = OnSecondaryContainer,
  background = BackgroundDark,
  onBackground = OnSurfaceDark,
  surface = SurfaceDark,
  onSurface = OnSurfaceDark,
  surfaceVariant = SurfaceContainerDark,
  onSurfaceVariant = OnSurfaceVariantDark,
  outline = OutlineColor,
  outlineVariant = SurfaceContainerHighestDark,
  error = ErrorRed,
  errorContainer = ErrorContainer
)

private val LightColorScheme = lightColorScheme(
  primary = PrimaryPurple,
  onPrimary = OnPrimary,
  primaryContainer = PrimaryContainer,
  onPrimaryContainer = OnPrimaryContainer,
  secondary = SecondaryTeal,
  onSecondary = OnSecondary,
  secondaryContainer = SecondaryContainer,
  onSecondaryContainer = OnSecondaryContainer,
  background = BackgroundLight,
  onBackground = OnBackground,
  surface = SurfaceLight,
  onSurface = OnSurface,
  surfaceVariant = SurfaceVariant,
  onSurfaceVariant = OnSurfaceVariant,
  outline = OutlineColor,
  outlineVariant = OutlineVariant,
  error = ErrorRed,
  errorContainer = ErrorContainer
)

private val PinkColorScheme = lightColorScheme(
  primary = PinkPrimary,
  onPrimary = PinkOnPrimary,
  primaryContainer = PinkPrimaryContainer,
  onPrimaryContainer = PinkOnPrimaryContainer,
  secondary = PinkSecondary,
  onSecondary = PinkOnSecondary,
  secondaryContainer = PinkSecondaryContainer,
  onSecondaryContainer = PinkOnSecondaryContainer,
  background = PinkBackground,
  onBackground = PinkOnSurface,
  surface = PinkSurface,
  onSurface = PinkOnSurface,
  surfaceVariant = PinkSurfaceVariant,
  onSurfaceVariant = PinkOnSurfaceVariant,
  outline = PinkOutlineVariant,
  outlineVariant = PinkOutlineVariant,
  error = ErrorRed,
  errorContainer = ErrorContainer
)

private val MetalColorScheme = lightColorScheme(
  primary = MetalPrimary,
  onPrimary = MetalOnPrimary,
  primaryContainer = MetalPrimaryContainer,
  onPrimaryContainer = MetalOnPrimaryContainer,
  secondary = MetalSecondary,
  onSecondary = MetalOnSecondary,
  secondaryContainer = MetalSecondaryContainer,
  onSecondaryContainer = MetalOnSecondaryContainer,
  background = MetalBackground,
  onBackground = MetalOnSurface,
  surface = MetalSurface,
  onSurface = MetalOnSurface,
  surfaceVariant = MetalSurfaceVariant,
  onSurfaceVariant = MetalOnSurfaceVariant,
  outline = MetalOutlineVariant,
  outlineVariant = MetalOutlineVariant,
  error = ErrorRed,
  errorContainer = ErrorContainer
)

@Composable
fun DiceMasterTheme(
  themeMode: AppThemeMode = AppThemeMode.SYSTEM,
  content: @Composable () -> Unit
) {
  val isDark = when (themeMode) {
    AppThemeMode.LIGHT -> false
    AppThemeMode.DARK -> true
    AppThemeMode.PINK -> false
    AppThemeMode.METAL -> false
    AppThemeMode.SYSTEM -> isSystemInDarkTheme()
  }

  val colorScheme = when (themeMode) {
    AppThemeMode.PINK -> PinkColorScheme
    AppThemeMode.METAL -> MetalColorScheme
    AppThemeMode.DARK -> DarkColorScheme
    AppThemeMode.LIGHT -> LightColorScheme
    AppThemeMode.SYSTEM -> if (isDark) DarkColorScheme else LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,
    content = content
  )
}
