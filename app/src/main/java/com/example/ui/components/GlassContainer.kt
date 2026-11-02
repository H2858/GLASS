package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.model.CustomizerConfig
import com.example.data.model.GlassBorder
import com.example.data.model.GlassCornerRadius

@Composable
fun GlassContainer(
    modifier: Modifier = Modifier,
    config: CustomizerConfig = CustomizerConfig(),
    customRadius: Dp? = null,
    customOpacity: Float? = null,
    customBorder: GlassBorder? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val cornerRadius = customRadius ?: config.cornerRadius.dpValue.dp
    val shape: Shape = RoundedCornerShape(cornerRadius)
    val opacity = customOpacity ?: config.opacity
    val borderOption = customBorder ?: config.border

    // Surface color blended with tint and opacity
    val baseSurface = config.tint.surfaceColor.copy(alpha = opacity)
    val surfaceGradient = Brush.linearGradient(
        colors = listOf(
            baseSurface.copy(alpha = (opacity * 1.25f).coerceAtMost(0.95f)),
            baseSurface.copy(alpha = (opacity * 0.85f).coerceAtLeast(0.10f)),
            baseSurface.copy(alpha = (opacity * 0.65f).coerceAtLeast(0.05f))
        ),
        start = Offset(0f, 0f),
        end = Offset(400f, 600f)
    )

    // Border stroke with specular gleam
    val borderBrush = when (borderOption) {
        GlassBorder.REFRACTIVE -> Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.75f),
                config.tint.accentColor.copy(alpha = 0.45f),
                Color.White.copy(alpha = 0.15f),
                Color.White.copy(alpha = 0.35f)
            ),
            start = Offset(0f, 0f),
            end = Offset(300f, 500f)
        )
        GlassBorder.NEON_GLOW -> Brush.linearGradient(
            colors = listOf(
                config.tint.accentColor.copy(alpha = 0.95f),
                config.tint.accentColor.copy(alpha = 0.60f),
                Color.White.copy(alpha = 0.85f)
            )
        )
        GlassBorder.MINIMAL -> Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.30f),
                Color.White.copy(alpha = 0.10f)
            )
        )
        GlassBorder.NONE -> Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent)
        )
    }

    val strokeWidth = borderOption.strokeWidthDp.dp

    Box(
        modifier = modifier
            .drawBehind {
                // Subtle ambient glow behind the glass
                if (borderOption == GlassBorder.NEON_GLOW) {
                    drawCircle(
                        color = config.tint.accentColor.copy(alpha = 0.18f),
                        radius = size.maxDimension * 0.55f,
                        center = center
                    )
                }
            }
            .clip(shape)
            .background(surfaceGradient)
            .then(
                if (borderOption != GlassBorder.NONE && strokeWidth > 0.dp) {
                    Modifier.border(strokeWidth, borderBrush, shape)
                } else {
                    Modifier
                }
            ),
        content = content
    )
}
