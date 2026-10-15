package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DiceColorChoice
import com.example.model.DiceTextureChoice
import com.example.model.DieItem
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.HankenGroteskFontFamily
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.MetalOutlineVariant
import com.example.ui.theme.MetalPrimary
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.PinkOutlineVariant
import com.example.ui.theme.PinkPrimary
import com.example.ui.theme.PrimaryPurple

@Composable
fun DieCard(
  die: DieItem,
  diceColor: DiceColorChoice,
  diceTexture: DiceTextureChoice,
  themeMode: AppThemeMode,
  onRoll: () -> Unit,
  onRemove: () -> Unit,
  modifier: Modifier = Modifier
) {
  val rotation = remember { Animatable(0f) }
  val scale = remember { Animatable(1f) }

  LaunchedEffect(die.isRolling) {
    if (die.isRolling) {
      rotation.animateTo(
        targetValue = 360f,
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
      )
      rotation.snapTo(0f)
    }
  }

  LaunchedEffect(die.value) {
    scale.animateTo(1.1f, animationSpec = tween(80))
    scale.animateTo(1.0f, animationSpec = tween(120))
  }

  // Determine active die accent color based on theme and settings
  val activeColor = when (themeMode) {
    AppThemeMode.PINK -> if (diceColor == DiceColorChoice.PURPLE) PinkPrimary else diceColor.color
    AppThemeMode.METAL -> if (diceColor == DiceColorChoice.PURPLE) MetalPrimary else diceColor.color
    AppThemeMode.DARK -> if (diceColor == DiceColorChoice.PURPLE) Color(0xFFCABEFF) else diceColor.color
    AppThemeMode.LIGHT, AppThemeMode.SYSTEM -> diceColor.color
  }

  val baseCardColor = when (themeMode) {
    AppThemeMode.PINK -> Color(0xFFFFF8FB)
    AppThemeMode.METAL -> Color(0xFFF8FAFC)
    AppThemeMode.DARK -> Color(0xFF1E222B)
    AppThemeMode.LIGHT, AppThemeMode.SYSTEM -> MaterialTheme.colorScheme.surface
  }

  val borderColor = when (themeMode) {
    AppThemeMode.PINK -> PinkOutlineVariant
    AppThemeMode.METAL -> MetalOutlineVariant.copy(alpha = 0.8f)
    AppThemeMode.DARK -> Color(0xFF451EBB).copy(alpha = 0.7f)
    AppThemeMode.LIGHT, AppThemeMode.SYSTEM -> OutlineVariant.copy(alpha = 0.7f)
  }

  val shadowAmbientColor = when (themeMode) {
    AppThemeMode.PINK -> PinkPrimary.copy(alpha = 0.28f)
    AppThemeMode.METAL -> MetalPrimary.copy(alpha = 0.28f)
    AppThemeMode.DARK -> Color(0xFFCABEFF).copy(alpha = 0.25f)
    AppThemeMode.LIGHT, AppThemeMode.SYSTEM -> PrimaryPurple.copy(alpha = 0.20f)
  }

  Surface(
    modifier = modifier
      .aspectRatio(1f)
      .shadow(
        elevation = if (diceTexture == DiceTextureChoice.NEON_GLOW) 14.dp else 8.dp,
        shape = RoundedCornerShape(26.dp),
        ambientColor = if (diceTexture == DiceTextureChoice.NEON_GLOW) activeColor.copy(alpha = 0.6f) else shadowAmbientColor,
        spotColor = if (diceTexture == DiceTextureChoice.NEON_GLOW) activeColor.copy(alpha = 0.8f) else shadowAmbientColor.copy(alpha = 0.45f)
      )
      .clip(RoundedCornerShape(26.dp))
      .border(
        width = if (diceTexture == DiceTextureChoice.NEON_GLOW) 2.dp else 1.2.dp,
        color = if (diceTexture == DiceTextureChoice.NEON_GLOW) activeColor else borderColor,
        shape = RoundedCornerShape(26.dp)
      )
      .clickable(onClick = onRoll)
      .testTag("die_card_${die.id}"),
    color = baseCardColor
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .drawBehind {
          val cornerRadiusPx = 26.dp.toPx()

          // 1. Theme-Specific Base 3D Shading
          val ambientColors = when (themeMode) {
            AppThemeMode.PINK -> listOf(
              Color(0xFFFFFFFF),
              Color(0xFFFFF0F5),
              Color(0xFFFFDFEA)
            )
            AppThemeMode.METAL -> listOf(
              Color(0xFFFFFFFF),
              Color(0xFFF1F6FA),
              Color(0xFFD6E4F0)
            )
            AppThemeMode.DARK -> listOf(
              Color(0xFF2E3440),
              Color(0xFF1E222B),
              Color(0xFF13161C)
            )
            AppThemeMode.LIGHT, AppThemeMode.SYSTEM -> listOf(
              Color(0xFFFFFFFF),
              Color(0xFFF9FAFB),
              Color(0xFFE8EBEE)
            )
          }

          val ambient3dBrush = Brush.linearGradient(
            colors = ambientColors,
            start = Offset(0f, 0f),
            end = Offset(size.width, size.height)
          )
          drawRoundRect(
            brush = ambient3dBrush,
            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
          )

          // 2. Texture Layer (Marble, Metal, Neon, or Glossy Solid Resin)
          when (diceTexture) {
            DiceTextureChoice.SWIRLED_MARBLE -> {
              val brush = Brush.radialGradient(
                colors = listOf(
                  activeColor.copy(alpha = 0.16f),
                  Color.Transparent,
                  activeColor.copy(alpha = 0.08f)
                ),
                center = Offset(size.width * 0.32f, size.height * 0.28f),
                radius = size.width * 0.85f
              )
              drawRect(brush)
            }
            DiceTextureChoice.BRUSHED_METAL -> {
              val brush = Brush.linearGradient(
                colors = listOf(
                  Color(0xFFE5E7EB).copy(alpha = 0.45f),
                  Color(0xFFD1D5DB).copy(alpha = 0.18f),
                  Color(0xFFF3F4F6).copy(alpha = 0.55f)
                ),
                start = Offset.Zero,
                end = Offset(size.width, size.height),
                tileMode = TileMode.Repeated
              )
              drawRect(brush)
            }
            DiceTextureChoice.NEON_GLOW -> {
              val brush = Brush.radialGradient(
                colors = listOf(
                  activeColor.copy(alpha = 0.28f),
                  Color.Transparent
                ),
                center = Offset(size.width * 0.5f, size.height * 0.5f),
                radius = size.width * 0.65f
              )
              drawRect(brush)
            }
            DiceTextureChoice.SOLID_PLASTIC -> {
              val tintBrush = Brush.radialGradient(
                colors = listOf(
                  activeColor.copy(alpha = 0.06f),
                  Color.Transparent
                ),
                center = Offset(size.width * 0.5f, size.height * 0.6f),
                radius = size.width * 0.7f
              )
              drawRect(tintBrush)
            }
          }

          // 3. 3D Beveled Chamfer & Inner Rim Light
          val rimHighlightColors = when (themeMode) {
            AppThemeMode.PINK -> listOf(Color.White.copy(alpha = 0.95f), Color(0xFFFF80AB).copy(alpha = 0.35f), Color.Transparent)
            AppThemeMode.METAL -> listOf(Color.White.copy(alpha = 0.95f), Color(0xFF38BDF8).copy(alpha = 0.40f), Color.Transparent)
            AppThemeMode.DARK -> listOf(Color.White.copy(alpha = 0.50f), Color(0xFFCABEFF).copy(alpha = 0.30f), Color.Transparent)
            AppThemeMode.LIGHT, AppThemeMode.SYSTEM -> listOf(Color.White.copy(alpha = 0.95f), Color.White.copy(alpha = 0.40f), Color.Transparent)
          }

          val rimHighlightBrush = Brush.linearGradient(
            colors = rimHighlightColors,
            start = Offset(0f, 0f),
            end = Offset(size.width * 0.7f, size.height * 0.7f)
          )
          drawRoundRect(
            brush = rimHighlightBrush,
            size = Size(size.width - 2.5.dp.toPx(), size.height - 2.5.dp.toPx()),
            topLeft = Offset(1.25.dp.toPx(), 1.25.dp.toPx()),
            cornerRadius = CornerRadius(cornerRadiusPx - 1.25.dp.toPx(), cornerRadiusPx - 1.25.dp.toPx()),
            style = Stroke(width = 2.2.dp.toPx())
          )

          // Bottom-right inner ambient shadow (die thickness)
          val innerShadowBrush = Brush.linearGradient(
            colors = listOf(
              Color.Transparent,
              if (themeMode == AppThemeMode.DARK) Color.Black.copy(alpha = 0.30f) else Color.Black.copy(alpha = 0.08f)
            ),
            start = Offset(size.width * 0.4f, size.height * 0.4f),
            end = Offset(size.width, size.height)
          )
          drawRoundRect(
            brush = innerShadowBrush,
            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
          )

          // 4. Glossy Dome Arc Reflection
          val glossPath = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height * 0.42f)
            cubicTo(
              size.width * 0.72f, size.height * 0.56f,
              size.width * 0.28f, size.height * 0.56f,
              0f, size.height * 0.42f
            )
            close()
          }
          val glossDomeBrush = Brush.verticalGradient(
            colors = listOf(
              Color.White.copy(alpha = if (themeMode == AppThemeMode.DARK) 0.40f else 0.75f),
              Color.White.copy(alpha = if (themeMode == AppThemeMode.DARK) 0.15f else 0.35f),
              Color.White.copy(alpha = 0.02f)
            ),
            startY = 0f,
            endY = size.height * 0.56f
          )
          drawPath(path = glossPath, brush = glossDomeBrush)

          // 5. Specular Glint (Glossy reflection hotspot near upper-left)
          val specularGlintBrush = Brush.radialGradient(
            colors = listOf(
              Color.White.copy(alpha = 0.90f),
              Color.White.copy(alpha = 0.45f),
              Color.Transparent
            ),
            center = Offset(size.width * 0.24f, size.height * 0.20f),
            radius = size.width * 0.22f
          )
          drawCircle(
            brush = specularGlintBrush,
            radius = size.width * 0.22f,
            center = Offset(size.width * 0.24f, size.height * 0.20f)
          )

          // Secondary subtle specular gleam at bottom-right edge for 3D bounce
          val bounceGleamBrush = Brush.radialGradient(
            colors = listOf(
              Color.White.copy(alpha = if (themeMode == AppThemeMode.DARK) 0.15f else 0.25f),
              Color.Transparent
            ),
            center = Offset(size.width * 0.82f, size.height * 0.84f),
            radius = size.width * 0.18f
          )
          drawCircle(
            brush = bounceGleamBrush,
            radius = size.width * 0.18f,
            center = Offset(size.width * 0.82f, size.height * 0.84f)
          )
        }
    ) {
      // Die Type Label at top center
      Text(
        text = die.type.label,
        fontFamily = JetBrainsMonoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        letterSpacing = 0.08.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = TextStyle(
          shadow = Shadow(
            color = if (themeMode == AppThemeMode.DARK) Color.Black.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.6f),
            offset = Offset(0f, 1.5f),
            blurRadius = 1f
          )
        ),
        modifier = Modifier
          .align(Alignment.TopCenter)
          .padding(top = 12.dp)
      )

      // Remove button at top right
      IconButton(
        onClick = onRemove,
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(4.dp)
          .size(32.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
          .testTag("remove_die_${die.id}")
      ) {
        Icon(
          imageVector = Icons.Default.Close,
          contentDescription = "Remove die",
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(16.dp)
        )
      }

      // Center number with 3D engraved depth shadow, tumbling rotation & bounce scale
      Text(
        text = die.value.toString(),
        fontFamily = HankenGroteskFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = if (die.value >= 100) 48.sp else 64.sp,
        letterSpacing = (-0.04).sp,
        color = activeColor,
        style = TextStyle(
          shadow = Shadow(
            color = if (themeMode == AppThemeMode.DARK) Color.Black.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.22f),
            offset = Offset(0f, 5f),
            blurRadius = 8f
          )
        ),
        modifier = Modifier
          .align(Alignment.Center)
          .rotate(rotation.value)
          .scale(scale.value)
      )
    }
  }
}
