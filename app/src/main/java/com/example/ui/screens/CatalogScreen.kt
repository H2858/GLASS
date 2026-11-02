package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.data.model.WidgetCategory
import com.example.data.model.WidgetItem
import com.example.data.repository.WidgetRepository
import com.example.ui.components.GlassClockWidgetPreview
import com.example.ui.components.GlassComboWidgetPreview
import com.example.ui.components.GlassFitnessWidgetPreview
import com.example.ui.components.GlassMediaPlayerWidgetPreview
import com.example.ui.components.GlassQuickTogglesWidgetPreview
import com.example.ui.components.GlassQuotesWidgetPreview
import com.example.ui.components.GlassWeatherWidgetPreview
import com.example.widget.GlassClockWidgetProvider
import com.example.widget.GlassComboWidgetProvider
import com.example.widget.GlassQuotesWidgetProvider
import com.example.widget.GlassWeatherWidgetProvider
import com.example.widget.WidgetUpdater

@Composable
fun CatalogScreen(
    config: CustomizerConfig,
    onSelectAndPreviewWidget: (String) -> Unit
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(WidgetCategory.ALL) }

    val filteredWidgets = remember(selectedCategory) {
        if (selectedCategory == WidgetCategory.ALL) {
            WidgetRepository.allWidgets
        } else {
            WidgetRepository.allWidgets.filter { it.category == selectedCategory }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("catalog_screen_column")
    ) {
        // Top Header
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp)) {
            Text(
                text = "Widget Library",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Discover 350+ combinations of frosted glass widgets",
                color = Color.White.copy(alpha = 0.65f),
                fontSize = 12.sp
            )
        }

        // Category Filter Chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(WidgetCategory.entries) { category ->
                val isSelected = (category == selectedCategory)
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = category },
                    label = {
                        Text(
                            text = category.title,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                        containerColor = Color.White.copy(alpha = 0.08f)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.15f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Widgets List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredWidgets, key = { it.id }) { widget ->
                WidgetCatalogCard(
                    widget = widget,
                    config = config,
                    onPreview = { onSelectAndPreviewWidget(widget.id) },
                    onPin = {
                        val targetClass = when (widget.id) {
                            "clock_digital_minimal" -> GlassClockWidgetProvider::class.java
                            "weather_card_detailed" -> GlassWeatherWidgetProvider::class.java
                            "combo_all_in_one" -> GlassComboWidgetProvider::class.java
                            "quotes_daily_inspiration" -> GlassQuotesWidgetProvider::class.java
                            else -> GlassClockWidgetProvider::class.java
                        }
                        WidgetUpdater.pinWidgetToHomeScreen(context, targetClass)
                    }
                )
            }
        }
    }
}

@Composable
private fun WidgetCatalogCard(
    widget: WidgetItem,
    config: CustomizerConfig,
    onPreview: () -> Unit,
    onPin: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F1523)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                listOf(Color.White.copy(alpha = 0.2f), Color.White.copy(alpha = 0.05f))
            )
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Widget Title & Size Tag Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = widget.title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = widget.description,
                        color = Color.White.copy(alpha = 0.65f),
                        fontSize = 11.sp,
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = widget.sizeTag,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dynamic Live Rendered Widget Preview Box with dark backdrop
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF070B14))
                    .padding(8.dp)
            ) {
                when (widget.id) {
                    "clock_digital_minimal", "clock_dual_world", "clock_flip_aesthetic" ->
                        GlassClockWidgetPreview(config = config, onClick = onPreview)
                    "weather_card_detailed", "weather_forecast_5day", "weather_sun_moon" ->
                        GlassWeatherWidgetPreview(config = config, onClick = onPreview)
                    "combo_all_in_one", "clock_pill_compact" ->
                        GlassComboWidgetPreview(config = config, onClick = onPreview)
                    "media_dynamic_player", "media_mini_pill" ->
                        GlassMediaPlayerWidgetPreview(config = config, onClick = onPreview)
                    "quick_control_center" ->
                        GlassQuickTogglesWidgetPreview(config = config, onClick = onPreview)
                    "quotes_daily_inspiration", "notes_glass_scratchpad" ->
                        GlassQuotesWidgetPreview(config = config, onClick = onPreview)
                    "fitness_activity_rings", "fitness_step_counter_card" ->
                        GlassFitnessWidgetPreview(config = config, onClick = onPreview)
                    else -> GlassClockWidgetPreview(config = config, onClick = onPreview)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action buttons row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onPreview,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Preview in Studio", fontSize = 12.sp)
                }

                Button(
                    onClick = onPin,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color(0xFF070A12)
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Pin to Home", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
