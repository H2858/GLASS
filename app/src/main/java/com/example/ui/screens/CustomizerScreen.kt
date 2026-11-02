package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomizerConfig
import com.example.data.model.GlassBorder
import com.example.data.model.GlassCornerRadius
import com.example.data.model.GlassFont
import com.example.data.model.GlassTint
import com.example.ui.components.GlassClockWidgetPreview

@Composable
fun CustomizerScreen(
    config: CustomizerConfig,
    onConfigChange: (CustomizerConfig) -> Unit,
    onResetDefaults: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("customizer_screen_column")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Glass Lab Studio",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Craft and tweak your personalized glass aesthetics",
                    color = Color.White.copy(alpha = 0.65f),
                    fontSize = 12.sp
                )
            }

            OutlinedButton(
                onClick = {
                    onResetDefaults()
                    Toast.makeText(context, "Reset to default glass style", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reset", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sticky Live Preview Banner
        Text(
            text = "Live Interactive Preview",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp))
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        listOf(Color(0xFF0D1424), Color(0xFF04060C))
                    )
                )
                .border(1.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(26.dp))
                .padding(12.dp)
        ) {
            GlassClockWidgetPreview(
                config = config,
                modifier = Modifier.testTag("customizer_live_preview")
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 1. Glass Tint Palette
        CustomizerSection(title = "1. Glass Tint & Glow") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GlassTint.entries.forEach { tint ->
                    val isSelected = (config.tint == tint)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onConfigChange(config.copy(tint = tint)) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(tint.accentColor.copy(alpha = 0.35f))
                                .border(
                                    width = if (isSelected) 2.5.dp else 1.dp,
                                    color = if (isSelected) Color.White else tint.accentColor.copy(alpha = 0.5f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(tint.accentColor)
                            )
                            if (isSelected) {
                                Icon(Icons.Default.Check, null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tint.displayName,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Glass Translucency & Opacity Slider
        CustomizerSection(
            title = "2. Translucency (${(config.opacity * 100).toInt()}%)"
        ) {
            Slider(
                value = config.opacity,
                onValueChange = { onConfigChange(config.copy(opacity = it)) },
                valueRange = 0.15f..0.90f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                ),
                modifier = Modifier.testTag("opacity_slider")
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Frosted Clear (15%)", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                Text("Smoked Deep (90%)", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Corner Radius
        CustomizerSection(title = "3. Corner Geometry") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GlassCornerRadius.entries.forEach { radius ->
                    val isSelected = (config.cornerRadius == radius)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onConfigChange(config.copy(cornerRadius = radius)) },
                        label = {
                            Text(
                                text = radius.displayName,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                            containerColor = Color.White.copy(alpha = 0.08f)
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Border Style
        CustomizerSection(title = "4. Specular Border Refraction") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GlassBorder.entries.forEach { border ->
                    val isSelected = (config.border == border)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onConfigChange(config.copy(border = border)) },
                        label = {
                            Text(
                                text = border.displayName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                            containerColor = Color.White.copy(alpha = 0.08f)
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Typography Font
        CustomizerSection(title = "5. Font Styling") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GlassFont.entries.forEach { font ->
                    val isSelected = (config.font == font)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onConfigChange(config.copy(font = font)) },
                        label = {
                            Text(
                                text = font.displayName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                            containerColor = Color.White.copy(alpha = 0.08f)
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 6. Content Elements Toggles
        CustomizerSection(title = "6. Content Elements") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ToggleRow(
                    title = "Show Battery Level Chip",
                    subtitle = "Dynamic live battery percentage on widgets",
                    checked = config.showBattery,
                    onCheckedChange = { onConfigChange(config.copy(showBattery = it)) }
                )

                ToggleRow(
                    title = "Show Weather Condition Badge",
                    subtitle = "Real-time temperature and condition icon",
                    checked = config.showWeather,
                    onCheckedChange = { onConfigChange(config.copy(showWeather = it)) }
                )

                ToggleRow(
                    title = "Show Clock Seconds",
                    subtitle = "Live ticking precision seconds indicator",
                    checked = config.showSeconds,
                    onCheckedChange = { onConfigChange(config.copy(showSeconds = it)) }
                )

                ToggleRow(
                    title = "24-Hour Time Format",
                    subtitle = "Switch between 12-hour AM/PM and 24-hour military",
                    checked = config.is24Hour,
                    onCheckedChange = { onConfigChange(config.copy(is24Hour = it)) }
                )

                ToggleRow(
                    title = "Celsius Temperature (°C)",
                    subtitle = "Toggle between Celsius (°C) and Fahrenheit (°F)",
                    checked = config.isCelsius,
                    onCheckedChange = { onConfigChange(config.copy(isCelsius = it)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Save Custom Preset Button
        Button(
            onClick = {
                Toast.makeText(context, "Custom glass preset saved successfully!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("save_preset_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color(0xFF070A12)
            )
        ) {
            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save Preset to All Widgets", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun CustomizerSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0E1322)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                listOf(Color.White.copy(alpha = 0.15f), Color.White.copy(alpha = 0.05f))
            )
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun ToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(text = subtitle, color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF070A12),
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = Color.White.copy(alpha = 0.7f),
                uncheckedTrackColor = Color.White.copy(alpha = 0.15f)
            )
        )
    }
}
