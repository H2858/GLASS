package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomizerConfig
import com.example.data.model.WallpaperItem
import com.example.data.repository.WidgetRepository
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.CustomizerScreen
import com.example.ui.screens.GuideScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.WallpapersScreen
import com.example.ui.theme.MyApplicationTheme

enum class AppNavTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    STUDIO("Studio", Icons.Filled.Home, Icons.Outlined.Home, "nav_studio"),
    CATALOG("Widgets", Icons.Filled.Widgets, Icons.Outlined.Widgets, "nav_catalog"),
    CUSTOMIZER("Glass Lab", Icons.Filled.Tune, Icons.Outlined.Tune, "nav_customizer"),
    WALLPAPERS("Wallpapers", Icons.Filled.Wallpaper, Icons.Outlined.Wallpaper, "nav_wallpapers"),
    GUIDE("Guide", Icons.Filled.HelpOutline, Icons.Outlined.HelpOutline, "nav_guide")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppRoot()
            }
        }
    }
}

@Composable
fun MainAppRoot() {
    var currentTab by remember { mutableStateOf(AppNavTab.STUDIO) }
    var activeWidgetId by remember { mutableStateOf("clock_digital_minimal") }
    var activeWallpaper by remember { mutableStateOf(WidgetRepository.wallpapers[0]) }
    var customizerConfig by remember { mutableStateOf(CustomizerConfig()) }

    // Hardware/Gesture back handler: return to STUDIO if in another tab
    BackHandler(enabled = currentTab != AppNavTab.STUDIO) {
        currentTab = AppNavTab.STUDIO
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_nav"),
                containerColor = Color(0xFF090D18),
                tonalElevation = 8.dp
            ) {
                AppNavTab.entries.forEach { tab ->
                    val isSelected = (currentTab == tab)
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF070A12),
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color.White.copy(alpha = 0.65f),
                            unselectedTextColor = Color.White.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.testTag(tab.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF070A12))
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppNavTab.STUDIO -> HomeScreen(
                    config = customizerConfig,
                    activeWallpaper = activeWallpaper,
                    activeWidgetId = activeWidgetId,
                    onSelectWallpaper = { activeWallpaper = it },
                    onSelectWidget = { activeWidgetId = it },
                    onSelectTint = { customizerConfig = customizerConfig.copy(tint = it) },
                    onNavigateToCustomizer = { currentTab = AppNavTab.CUSTOMIZER },
                    onNavigateToWallpapers = { currentTab = AppNavTab.WALLPAPERS },
                    onNavigateToCatalog = { currentTab = AppNavTab.CATALOG }
                )

                AppNavTab.CATALOG -> CatalogScreen(
                    config = customizerConfig,
                    onSelectAndPreviewWidget = { widgetId ->
                        activeWidgetId = widgetId
                        currentTab = AppNavTab.STUDIO
                    }
                )

                AppNavTab.CUSTOMIZER -> CustomizerScreen(
                    config = customizerConfig,
                    onConfigChange = { customizerConfig = it },
                    onResetDefaults = { customizerConfig = CustomizerConfig() }
                )

                AppNavTab.WALLPAPERS -> WallpapersScreen(
                    activeWallpaper = activeWallpaper,
                    onSelectWallpaper = { activeWallpaper = it },
                    onPreviewInStudio = { wp ->
                        activeWallpaper = wp
                        currentTab = AppNavTab.STUDIO
                    }
                )

                AppNavTab.GUIDE -> GuideScreen()
            }
        }
    }
}
