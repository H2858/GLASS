package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.BatteryManager
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomizerConfig
import com.example.data.model.GlassFont
import com.example.data.repository.WidgetRepository
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Helper for selected font family
fun getComposeFont(font: GlassFont): FontFamily {
    return when (font) {
        GlassFont.MODERN_SANS -> FontFamily.SansSerif
        GlassFont.TECH_MONO -> FontFamily.Monospace
        GlassFont.ELEGANT_SERIF -> FontFamily.Serif
        GlassFont.BOLD_DISPLAY -> FontFamily.SansSerif
    }
}

// 1. Digital Clock & Date Widget Preview
@Composable
fun GlassClockWidgetPreview(
    modifier: Modifier = Modifier,
    config: CustomizerConfig = CustomizerConfig(),
    onClick: (() -> Unit)? = null
) {
    var currentTime by remember { mutableStateOf(Date()) }
    val context = LocalContext.current
    var batteryLevel by remember { mutableIntStateOf(88) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Date()
            delay(1000)
        }
    }

    DisposableEffect(Unit) {
        val receiver = object : android.content.BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
                if (level >= 0 && scale > 0) {
                    batteryLevel = (level * 100) / scale
                }
            }
        }
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(receiver, filter)
        onDispose {
            try {
                context.unregisterReceiver(receiver)
            } catch (_: Exception) {}
        }
    }

    val timeFormatPattern = if (config.is24Hour) {
        if (config.showSeconds) "HH:mm:ss" else "HH:mm"
    } else {
        if (config.showSeconds) "hh:mm:ss a" else "hh:mm a"
    }
    val timeFormatter = remember(timeFormatPattern) { SimpleDateFormat(timeFormatPattern, Locale.getDefault()) }
    val dateFormatter = remember { SimpleDateFormat("EEEE, MMM d", Locale.getDefault()) }

    GlassContainer(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        config = config
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateFormatter.format(currentTime),
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    fontFamily = getComposeFont(config.font),
                    fontWeight = FontWeight.Medium
                )

                if (config.showBattery) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.BatteryChargingFull,
                            contentDescription = "Battery",
                            tint = config.tint.accentColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$batteryLevel%",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = timeFormatter.format(currentTime),
                    color = Color.White,
                    fontSize = if (config.showSeconds) 38.sp else 46.sp,
                    fontFamily = getComposeFont(config.font),
                    fontWeight = FontWeight.Light,
                    letterSpacing = (-1).sp
                )

                if (config.showWeather) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = "Sunny",
                                tint = Color(0xFFFFD54F),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (config.isCelsius) "24°C" else "75°F",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = "Sunny • Clear",
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

// 2. Weather & Forecast Card Widget Preview
@Composable
fun GlassWeatherWidgetPreview(
    modifier: Modifier = Modifier,
    config: CustomizerConfig = CustomizerConfig(),
    onClick: (() -> Unit)? = null
) {
    GlassContainer(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        config = config
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "San Francisco, CA",
                    color = config.tint.accentColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (config.isCelsius) "H: 26°  L: 16°" else "H: 78°  L: 60°",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = "Weather",
                        tint = Color(0xFFFFD54F),
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = if (config.isCelsius) "24°" else "75°",
                        color = Color.White,
                        fontSize = 38.sp,
                        fontFamily = getComposeFont(config.font),
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        text = "Mostly Sunny",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.WaterDrop, "Humidity", tint = config.tint.accentColor, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("48%", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Air, "Wind", tint = config.tint.accentColor, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("12 km/h", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// 3. Combo All-in-One Bar
@Composable
fun GlassComboWidgetPreview(
    modifier: Modifier = Modifier,
    config: CustomizerConfig = CustomizerConfig(),
    onClick: (() -> Unit)? = null
) {
    var currentTime by remember { mutableStateOf(Date()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Date()
            delay(1000)
        }
    }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val dateFormatter = remember { SimpleDateFormat("EEE, MMM d", Locale.getDefault()) }

    GlassContainer(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        config = config
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = timeFormatter.format(currentTime),
                color = Color.White,
                fontSize = 26.sp,
                fontFamily = getComposeFont(config.font),
                fontWeight = FontWeight.SemiBold
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .width(1.dp)
                    .height(24.dp)
                    .background(Color.White.copy(alpha = 0.2f))
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dateFormatter.format(currentTime),
                    color = Color.White.copy(alpha = 0.95f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = if (config.isCelsius) "24°C • Sunny" else "75°F • Sunny",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 11.sp
                )
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.BatteryChargingFull,
                    contentDescription = null,
                    tint = config.tint.accentColor,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "88%",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// 4. Glass Media Player Widget Preview
@Composable
fun GlassMediaPlayerWidgetPreview(
    modifier: Modifier = Modifier,
    config: CustomizerConfig = CustomizerConfig(),
    onClick: (() -> Unit)? = null
) {
    var isPlaying by remember { mutableStateOf(true) }
    var trackProgress by remember { mutableFloatStateOf(0.42f) }

    // Waveform visualizer animation
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    val wave1 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(450, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "w1"
    )
    val wave2 by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "w2"
    )
    val wave3 by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(350, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "w3"
    )

    GlassContainer(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        config = config
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Album Art Frame
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(config.tint.accentColor, Color(0xFF6A00F4))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Music",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Midnight Echoes",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Aurora Waves • Neon Synth",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Waveform indicator
                Row(
                    modifier = Modifier.height(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    val heights = listOf(wave1, wave2, wave3, wave1 * 0.7f, wave2 * 0.85f)
                    heights.forEach { h ->
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height((20 * (if (isPlaying) h else 0.2f)).dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(config.tint.accentColor)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { trackProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = config.tint.accentColor,
                trackColor = Color.White.copy(alpha = 0.15f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "01:24",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 10.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { trackProgress = (trackProgress - 0.1f).coerceAtLeast(0f) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.FastRewind, "Previous", tint = Color.White.copy(alpha = 0.85f))
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable { isPlaying = !isPlaying },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = { trackProgress = (trackProgress + 0.1f).coerceAtMost(1f) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.FastForward, "Next", tint = Color.White.copy(alpha = 0.85f))
                    }
                }

                Text(
                    text = "03:45",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 10.sp
                )
            }
        }
    }
}

// 5. Quick Control Center / Toggles Preview
@Composable
fun GlassQuickTogglesWidgetPreview(
    modifier: Modifier = Modifier,
    config: CustomizerConfig = CustomizerConfig(),
    onClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var isFlashlightOn by remember { mutableStateOf(false) }
    var isWifiOn by remember { mutableStateOf(true) }
    var isBluetoothOn by remember { mutableStateOf(true) }
    var isDndOn by remember { mutableStateOf(false) }

    fun toggleFlashlight() {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
        if (cameraManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val cameraId = cameraManager.cameraIdList.firstOrNull()
                if (cameraId != null) {
                    isFlashlightOn = !isFlashlightOn
                    cameraManager.setTorchMode(cameraId, isFlashlightOn)
                    Toast.makeText(context, if (isFlashlightOn) "Flashlight ON" else "Flashlight OFF", Toast.LENGTH_SHORT).show()
                }
            } catch (e: CameraAccessException) {
                isFlashlightOn = !isFlashlightOn
                Toast.makeText(context, "Flashlight: ${if (isFlashlightOn) "ON" else "OFF"}", Toast.LENGTH_SHORT).show()
            }
        } else {
            isFlashlightOn = !isFlashlightOn
        }
    }

    GlassContainer(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        config = config
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Control Center",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Quick Glass Toggles",
                    color = config.tint.accentColor,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Flashlight
                ToggleItem(
                    icon = Icons.Default.FlashlightOn,
                    label = "Torch",
                    isActive = isFlashlightOn,
                    activeColor = Color(0xFFFFD54F),
                    onClick = { toggleFlashlight() }
                )

                // Wi-Fi
                ToggleItem(
                    icon = Icons.Default.Wifi,
                    label = "Wi-Fi",
                    isActive = isWifiOn,
                    activeColor = config.tint.accentColor,
                    onClick = { isWifiOn = !isWifiOn }
                )

                // Bluetooth
                ToggleItem(
                    icon = Icons.Default.Bluetooth,
                    label = "Bluetooth",
                    isActive = isBluetoothOn,
                    activeColor = Color(0xFF64B5F6),
                    onClick = { isBluetoothOn = !isBluetoothOn }
                )

                // DND
                ToggleItem(
                    icon = Icons.Default.Notifications,
                    label = "DND",
                    isActive = isDndOn,
                    activeColor = Color(0xFFFF5D8F),
                    onClick = { isDndOn = !isDndOn }
                )
            }
        }
    }
}

@Composable
private fun ToggleItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    if (isActive) activeColor.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.12f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) activeColor else Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// 6. Daily Quotes & Wisdom Widget Preview
@Composable
fun GlassQuotesWidgetPreview(
    modifier: Modifier = Modifier,
    config: CustomizerConfig = CustomizerConfig(),
    onClick: (() -> Unit)? = null
) {
    var quoteIndex by remember { mutableIntStateOf(0) }
    val quote = WidgetRepository.dailyQuotes[quoteIndex % WidgetRepository.dailyQuotes.size]

    GlassContainer(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        config = config
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable { quoteIndex++ }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.FormatQuote,
                    contentDescription = "Quote",
                    tint = config.tint.accentColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Daily Spark (tap to shuffle)",
                    color = config.tint.accentColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\"${quote.first}\"",
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = getComposeFont(config.font),
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "— ${quote.second}",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

// 7. Fitness Activity 3-Ring Gauge Widget Preview
@Composable
fun GlassFitnessWidgetPreview(
    modifier: Modifier = Modifier,
    config: CustomizerConfig = CustomizerConfig(),
    onClick: (() -> Unit)? = null
) {
    val ringColors = listOf(
        Color(0xFFFF2A85), // Move / Calories (Pink)
        Color(0xFF00F5D4), // Exercise / Steps (Mint)
        Color(0xFF00E5FF)  // Water / Stand (Cyan)
    )

    GlassContainer(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        config = config
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Triple rings canvas
            Box(
                modifier = Modifier.size(76.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(76.dp)) {
                    val strokeW = 6.dp.toPx()
                    val radii = listOf(32.dp.toPx(), 23.dp.toPx(), 14.dp.toPx())
                    val sweeps = listOf(280f, 220f, 310f)

                    radii.forEachIndexed { idx, r ->
                        // Background track
                        drawCircle(
                            color = ringColors[idx].copy(alpha = 0.2f),
                            radius = r,
                            style = Stroke(width = strokeW)
                        )
                        // Active sweep arc
                        drawArc(
                            color = ringColors[idx],
                            startAngle = -90f,
                            sweepAngle = sweeps[idx],
                            useCenter = false,
                            topLeft = Offset(center.x - r, center.y - r),
                            size = Size(r * 2, r * 2),
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Activity Goals",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF00F5D4)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("7,842 / 10,000 Steps", color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFFF2A85)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("520 / 650 Kcal", color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF00E5FF)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("1.8 / 2.5 L Water", color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp)
                }
            }
        }
    }
}
