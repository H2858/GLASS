package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.R

enum class WidgetCategory(val title: String) {
    ALL("All Widgets"),
    CLOCKS("Clocks & Date"),
    WEATHER("Weather"),
    BATTERY("Battery & Specs"),
    MEDIA("Media Player"),
    TOGGLES("Quick Toggles"),
    QUOTES("Quotes & Notes"),
    FITNESS("Fitness & Activity")
}

data class WidgetItem(
    val id: String,
    val title: String,
    val category: WidgetCategory,
    val description: String,
    val sizeTag: String,
    val isSystemWidgetAvailable: Boolean = false,
    val widgetProviderClassName: String? = null
)

enum class GlassTint(val displayName: String, val surfaceColor: Color, val accentColor: Color) {
    FROST_WHITE("Frosted Ice", Color(0x28FFFFFF), Color(0xFFFFFFFF)),
    OBSIDIAN_DARK("Obsidian Dark", Color(0xD90C121F), Color(0xFF00E5FF)),
    CYBER_CYAN("Cyber Cyan", Color(0x3300E5FF), Color(0xFF00E5FF)),
    AMETHYST_VIOLET("Amethyst Violet", Color(0x389D4EDD), Color(0xFFC77DFF)),
    EMERALD_MINT("Emerald Mint", Color(0x3300F5D4), Color(0xFF00F5D4)),
    SUNSET_ROSE("Sunset Rose", Color(0x38FF5D8F), Color(0xFFFF85A1)),
    LIQUID_TITANIUM("Liquid Titanium", Color(0x38CED4DA), Color(0xFFE9ECEF))
}

enum class GlassCornerRadius(val displayName: String, val dpValue: Int) {
    ROUNDED_24("Balanced 24dp", 24),
    SQUIRCLE_16("Squircle 16dp", 16),
    PILL_999("Pill Curve", 999),
    SUBTLE_8("Subtle 8dp", 8)
}

enum class GlassBorder(val displayName: String, val strokeWidthDp: Float, val alpha: Float) {
    REFRACTIVE("Refractive Glass", 1.5f, 0.5f),
    NEON_GLOW("Neon Edge", 2.0f, 0.85f),
    MINIMAL("Minimal 1dp", 1.0f, 0.25f),
    NONE("No Border", 0f, 0f)
}

enum class GlassFont(val displayName: String) {
    MODERN_SANS("Modern Sans"),
    TECH_MONO("Tech Digital"),
    ELEGANT_SERIF("Elegant Classic"),
    BOLD_DISPLAY("Bold Futurist")
}

data class CustomizerConfig(
    val opacity: Float = 0.35f,
    val tint: GlassTint = GlassTint.FROST_WHITE,
    val cornerRadius: GlassCornerRadius = GlassCornerRadius.ROUNDED_24,
    val border: GlassBorder = GlassBorder.REFRACTIVE,
    val font: GlassFont = GlassFont.MODERN_SANS,
    val showSeconds: Boolean = false,
    val showBattery: Boolean = true,
    val showWeather: Boolean = true,
    val isCelsius: Boolean = true,
    val is24Hour: Boolean = true
)

data class WallpaperItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val drawableResId: Int? = null,
    val gradientColors: List<Color> = emptyList()
)
