package com.example.ui.screens

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WallpaperItem
import com.example.data.repository.WidgetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun WallpapersScreen(
    activeWallpaper: WallpaperItem,
    onSelectWallpaper: (WallpaperItem) -> Unit,
    onPreviewInStudio: (WallpaperItem) -> Unit
) {
    val context = LocalContext.current
    var wallpaperToApply by remember { mutableStateOf<WallpaperItem?>(null) }
    var isApplying by remember { mutableStateOf(false) }

    fun applyWallpaper(wp: WallpaperItem, which: Int) {
        isApplying = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val wallpaperManager = WallpaperManager.getInstance(context)
                val bitmap: Bitmap = if (wp.drawableResId != null) {
                    BitmapFactory.decodeResource(context.resources, wp.drawableResId)
                } else {
                    // Render procedural gradient bitmap
                    val w = 1080
                    val h = 2400
                    val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bmp)
                    val colors = wp.gradientColors.map { it.toArgb() }.toIntArray()
                    val shader = android.graphics.LinearGradient(
                        0f, 0f, 0f, h.toFloat(),
                        colors, null, android.graphics.Shader.TileMode.CLAMP
                    )
                    val paint = Paint().apply { this.shader = shader }
                    canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), paint)
                    bmp
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap(bitmap, null, true, which)
                } else {
                    wallpaperManager.setBitmap(bitmap)
                }

                withContext(Dispatchers.Main) {
                    isApplying = false
                    wallpaperToApply = null
                    Toast.makeText(context, "Wallpaper successfully applied!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isApplying = false
                    wallpaperToApply = null
                    Toast.makeText(context, "Could not apply wallpaper: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("wallpapers_screen_column")
    ) {
        // Header
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp)) {
            Text(
                text = "Glass Wallpapers",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Curated high-refraction backgrounds designed for glassmorphism",
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 12.sp
            )
        }

        // Wallpaper Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(WidgetRepository.wallpapers, key = { it.id }) { wp ->
                val isSelected = (wp.id == activeWallpaper.id)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1523)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(
                            if (isSelected) listOf(MaterialTheme.colorScheme.primary, Color.White)
                            else listOf(Color.White.copy(alpha = 0.2f), Color.White.copy(alpha = 0.05f))
                        )
                    )
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Wallpaper visual
                        if (wp.drawableResId != null) {
                            Image(
                                painter = painterResource(id = wp.drawableResId),
                                contentDescription = wp.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else if (wp.gradientColors.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Brush.verticalGradient(wp.gradientColors))
                            )
                        }

                        // Gradient bottom scrim
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.2f),
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.75f)
                                        )
                                    )
                                )
                        )

                        // Top Active Badge
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Check, null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            }
                        }

                        // Bottom Actions
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = wp.title,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = wp.subtitle,
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 10.sp,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        onSelectWallpaper(wp)
                                        onPreviewInStudio(wp)
                                    },
                                    modifier = Modifier.weight(1f).height(36.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Preview", fontSize = 11.sp, color = Color.White)
                                }

                                Button(
                                    onClick = { wallpaperToApply = wp },
                                    modifier = Modifier.weight(1f).height(36.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = Color(0xFF070A12)
                                    )
                                ) {
                                    Text("Apply", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Apply Wallpaper Dialog
    if (wallpaperToApply != null) {
        val wp = wallpaperToApply!!
        AlertDialog(
            onDismissRequest = { if (!isApplying) wallpaperToApply = null },
            title = {
                Text("Set Wallpaper: ${wp.title}", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Where would you like to apply this glassmorphism wallpaper?")
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                applyWallpaper(wp, WallpaperManager.FLAG_SYSTEM)
                            } else {
                                applyWallpaper(wp, 0)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isApplying
                    ) {
                        Text("Home Screen")
                    }

                    Button(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                applyWallpaper(wp, WallpaperManager.FLAG_LOCK)
                            } else {
                                applyWallpaper(wp, 0)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isApplying
                    ) {
                        Text("Lock Screen")
                    }

                    Button(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                applyWallpaper(wp, WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)
                            } else {
                                applyWallpaper(wp, 0)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isApplying
                    ) {
                        Text("Home & Lock Screens")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { wallpaperToApply = null },
                    enabled = !isApplying
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
