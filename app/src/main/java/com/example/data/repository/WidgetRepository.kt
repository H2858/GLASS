package com.example.data.repository

import androidx.compose.ui.graphics.Color
import com.example.R
import com.example.data.model.WallpaperItem
import com.example.data.model.WidgetCategory
import com.example.data.model.WidgetItem

object WidgetRepository {

    val allWidgets: List<WidgetItem> = listOf(
        // Clocks
        WidgetItem(
            id = "clock_digital_minimal",
            title = "Digital Glass Clock & Date",
            category = WidgetCategory.CLOCKS,
            description = "Sleek transparent clock with dynamic day, date, and live battery status.",
            sizeTag = "4x2",
            isSystemWidgetAvailable = true,
            widgetProviderClassName = "com.example.widget.GlassClockWidgetProvider"
        ),
        WidgetItem(
            id = "clock_dual_world",
            title = "Dual World Glass Clock",
            category = WidgetCategory.CLOCKS,
            description = "Simultaneous display of local time alongside London & Tokyo timezones.",
            sizeTag = "4x2"
        ),
        WidgetItem(
            id = "clock_pill_compact",
            title = "Glass Time Capsule Pill",
            category = WidgetCategory.CLOCKS,
            description = "Compact pill widget combining digital time and battery level.",
            sizeTag = "3x1"
        ),
        WidgetItem(
            id = "clock_flip_aesthetic",
            title = "Retro Frosted Flip Clock",
            category = WidgetCategory.CLOCKS,
            description = "Retro mechanical flip-card numbers in translucent smoked glass.",
            sizeTag = "4x2"
        ),

        // Weather
        WidgetItem(
            id = "weather_card_detailed",
            title = "Glass Weather & Radar Card",
            category = WidgetCategory.WEATHER,
            description = "Real-time temperature, condition status, humidity, wind, and hi/lo range.",
            sizeTag = "4x2",
            isSystemWidgetAvailable = true,
            widgetProviderClassName = "com.example.widget.GlassWeatherWidgetProvider"
        ),
        WidgetItem(
            id = "weather_forecast_5day",
            title = "5-Day Glass Forecast Pill",
            category = WidgetCategory.WEATHER,
            description = "Weekly temperature trends with translucent condition icons.",
            sizeTag = "4x2"
        ),
        WidgetItem(
            id = "weather_sun_moon",
            title = "Sun Phase & UV Glass Arc",
            category = WidgetCategory.WEATHER,
            description = "Golden hour sunrise, sunset time, and current UV Index meter.",
            sizeTag = "2x2"
        ),

        // Power & System
        WidgetItem(
            id = "battery_circular_gauge",
            title = "Glass Battery Radial Meter",
            category = WidgetCategory.BATTERY,
            description = "Circular glowing ring showing phone battery level and charging speed.",
            sizeTag = "2x2"
        ),
        WidgetItem(
            id = "system_hardware_monitor",
            title = "RAM & Storage Glass Monitor",
            category = WidgetCategory.BATTERY,
            description = "Real-time device memory consumption and internal storage space.",
            sizeTag = "4x2"
        ),

        // Media Player
        WidgetItem(
            id = "media_dynamic_player",
            title = "Frosted Glass Music Controller",
            category = WidgetCategory.MEDIA,
            description = "Album art glass blur, track controls, and animated audio equalizer waveform.",
            sizeTag = "4x2"
        ),
        WidgetItem(
            id = "media_mini_pill",
            title = "Compact Now Playing Pill",
            category = WidgetCategory.MEDIA,
            description = "Mini floating controller for one-touch playback and next track.",
            sizeTag = "4x1"
        ),

        // Quick Toggles
        WidgetItem(
            id = "combo_all_in_one",
            title = "Glass Combo Bar (Clock+Weather)",
            category = WidgetCategory.TOGGLES,
            description = "Streamlined horizontal bar combining clock, weather, and battery.",
            sizeTag = "4x1",
            isSystemWidgetAvailable = true,
            widgetProviderClassName = "com.example.widget.GlassComboWidgetProvider"
        ),
        WidgetItem(
            id = "quick_control_center",
            title = "Glass Quick Toggles Hub",
            category = WidgetCategory.TOGGLES,
            description = "One-tap Flashlight torch, Wi-Fi, Sound profile, and Bluetooth toggles.",
            sizeTag = "4x2"
        ),

        // Quotes & Notes
        WidgetItem(
            id = "quotes_daily_inspiration",
            title = "Daily Glass Spark & Wisdom",
            category = WidgetCategory.QUOTES,
            description = "Inspiring curated quotes with elegant typography in translucent glass.",
            sizeTag = "3x2",
            isSystemWidgetAvailable = true,
            widgetProviderClassName = "com.example.widget.GlassQuotesWidgetProvider"
        ),
        WidgetItem(
            id = "notes_glass_scratchpad",
            title = "Quick Glass Sticky Note",
            category = WidgetCategory.QUOTES,
            description = "Pin your important thoughts, reminders, or todo list to your screen.",
            sizeTag = "3x2"
        ),

        // Fitness & Activity
        WidgetItem(
            id = "fitness_activity_rings",
            title = "Glass Fitness Activity Rings",
            category = WidgetCategory.FITNESS,
            description = "3-Ring glass gauge tracking Steps, Active Calories, and Water intake.",
            sizeTag = "2x2"
        ),
        WidgetItem(
            id = "fitness_step_counter_card",
            title = "Daily Step Goal Glass Tracker",
            category = WidgetCategory.FITNESS,
            description = "Daily steps progress bar with distance walked and active minutes.",
            sizeTag = "4x1"
        )
    )

    val wallpapers: List<WallpaperItem> = listOf(
        WallpaperItem(
            id = "wp_cosmic",
            title = "Cosmic Aurora",
            subtitle = "Deep space indigo & glowing cyan",
            drawableResId = R.drawable.img_wallpaper_cosmic_1793621675160
        ),
        WallpaperItem(
            id = "wp_fluid",
            title = "Iridescent Silk",
            subtitle = "Holographic pastel ribbon waves",
            drawableResId = R.drawable.img_wallpaper_fluid_1793621676479
        ),
        WallpaperItem(
            id = "wp_midnight_matrix",
            title = "Midnight Cyan",
            subtitle = "Sleek AMOLED dark gradient with cyan glow",
            gradientColors = listOf(
                Color(0xFF04060B),
                Color(0xFF0A1325),
                Color(0xFF003847),
                Color(0xFF020914)
            )
        ),
        WallpaperItem(
            id = "wp_amethyst_dream",
            title = "Violet Nebula",
            subtitle = "Rich purple aura with deep obsidian base",
            gradientColors = listOf(
                Color(0xFF0D0221),
                Color(0xFF240046),
                Color(0xFF3C096C),
                Color(0xFF10002B)
            )
        ),
        WallpaperItem(
            id = "wp_sunset_blaze",
            title = "Sunset Radiance",
            subtitle = "Warm coral, magenta and peach horizon",
            gradientColors = listOf(
                Color(0xFF1A0B2E),
                Color(0xFF7B1E7A),
                Color(0xFFD81B60),
                Color(0xFFFF85A1)
            )
        ),
        WallpaperItem(
            id = "wp_emerald_forest",
            title = "Emerald Aurora",
            subtitle = "Translucent mint and dark forest velvet",
            gradientColors = listOf(
                Color(0xFF021B1A),
                Color(0xFF004D40),
                Color(0xFF00796B),
                Color(0xFF004D40)
            )
        )
    )

    val dailyQuotes: List<Pair<String, String>> = listOf(
        "Simplicity is the keynote of all true elegance." to "Coco Chanel",
        "Make it simple, but significant." to "Don Draper",
        "Design is not just what it looks like. Design is how it works." to "Steve Jobs",
        "Creativity is intelligence having fun." to "Albert Einstein",
        "The details are not the details. They make the design." to "Charles Eames",
        "Less is more, but clarity is everything." to "Dieter Rams"
    )
}
