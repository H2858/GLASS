package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SignalCellular4Bar
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomizerConfig
import com.example.data.model.WallpaperItem

@Composable
fun PhoneMockupPreview(
    modifier: Modifier = Modifier,
    wallpaper: WallpaperItem,
    config: CustomizerConfig,
    activeWidgetId: String,
    onWidgetClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(490.dp)
            .shadow(16.dp, RoundedCornerShape(36.dp))
            .clip(RoundedCornerShape(36.dp))
            .border(2.5.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(36.dp))
    ) {
        // Wallpaper background (Image asset or gradient)
        if (wallpaper.drawableResId != null) {
            Image(
                painter = painterResource(id = wallpaper.drawableResId),
                contentDescription = wallpaper.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else if (wallpaper.gradientColors.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(wallpaper.gradientColors))
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F172A))
            )
        }

        // Overlay vignette for depth
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.25f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.45f)
                        )
                    )
                )
        )

        // Phone interface content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Simulated Status Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "10:42",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Wifi, "Wifi", tint = Color.White, modifier = Modifier.size(13.dp))
                    Icon(Icons.Default.SignalCellular4Bar, "Signal", tint = Color.White, modifier = Modifier.size(13.dp))
                    Text("100%", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // The Active Glass Widget on Display
            Box(modifier = Modifier.fillMaxWidth()) {
                when (activeWidgetId) {
                    "clock_digital_minimal" -> GlassClockWidgetPreview(config = config, onClick = onWidgetClick)
                    "weather_card_detailed" -> GlassWeatherWidgetPreview(config = config, onClick = onWidgetClick)
                    "combo_all_in_one" -> GlassComboWidgetPreview(config = config, onClick = onWidgetClick)
                    "media_dynamic_player" -> GlassMediaPlayerWidgetPreview(config = config, onClick = onWidgetClick)
                    "quick_control_center" -> GlassQuickTogglesWidgetPreview(config = config, onClick = onWidgetClick)
                    "quotes_daily_inspiration" -> GlassQuotesWidgetPreview(config = config, onClick = onWidgetClick)
                    "fitness_activity_rings" -> GlassFitnessWidgetPreview(config = config, onClick = onWidgetClick)
                    else -> GlassClockWidgetPreview(config = config, onClick = onWidgetClick)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Simulated App Dock at bottom
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Dock Glass Bar
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(26.dp))
                        .background(Color.White.copy(alpha = 0.18f))
                        .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(26.dp))
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(22.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DockIcon(icon = Icons.Default.Phone, bgColor = Color(0xFF00E676))
                        DockIcon(icon = Icons.Default.ChatBubble, bgColor = Color(0xFF2979FF))
                        DockIcon(icon = Icons.Default.Language, bgColor = Color(0xFFFF9100))
                        DockIcon(icon = Icons.Default.CameraAlt, bgColor = Color(0xFFFF1744))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Gesture navigation pill
                Box(
                    modifier = Modifier
                        .width(72.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.7f))
                )
            }
        }
    }
}

@Composable
private fun DockIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bgColor: Color
) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}
