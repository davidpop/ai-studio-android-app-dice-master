package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val HankenGroteskFontFamily = FontFamily(
  Font(R.font.hanken_grotesk, FontWeight.Normal),
  Font(R.font.hanken_grotesk, FontWeight.Bold),
  Font(R.font.hanken_grotesk, FontWeight.ExtraBold)
)

val JetBrainsMonoFontFamily = FontFamily(
  Font(R.font.jetbrains_mono, FontWeight.Normal),
  Font(R.font.jetbrains_mono, FontWeight.SemiBold),
  Font(R.font.jetbrains_mono, FontWeight.Bold)
)

val AppTypography = Typography(
  displayLarge = TextStyle(
    fontFamily = HankenGroteskFontFamily,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 72.sp,
    lineHeight = 80.sp,
    letterSpacing = (-0.04).sp
  ),
  headlineLarge = TextStyle(
    fontFamily = HankenGroteskFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    lineHeight = 40.sp
  ),
  headlineMedium = TextStyle(
    fontFamily = HankenGroteskFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp,
    lineHeight = 34.sp
  ),
  titleLarge = TextStyle(
    fontFamily = HankenGroteskFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 22.sp,
    lineHeight = 28.sp
  ),
  titleMedium = TextStyle(
    fontFamily = HankenGroteskFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 18.sp,
    lineHeight = 24.sp
  ),
  bodyLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp
  ),
  bodyMedium = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp
  ),
  labelLarge = TextStyle(
    fontFamily = JetBrainsMonoFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 13.sp,
    lineHeight = 18.sp,
    letterSpacing = 0.08.sp
  ),
  labelMedium = TextStyle(
    fontFamily = JetBrainsMonoFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.08.sp
  ),
  labelSmall = TextStyle(
    fontFamily = JetBrainsMonoFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 10.sp,
    lineHeight = 14.sp,
    letterSpacing = 0.08.sp
  )
)
