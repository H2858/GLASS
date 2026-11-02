package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import com.example.MainActivity
import com.example.R
import com.example.data.repository.WidgetRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WidgetUpdater {

    fun getBatteryLevel(context: Context): Int {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, filter)
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        return if (level >= 0 && scale > 0) (level * 100) / scale else 85
    }

    fun getLaunchAppIntent(context: Context, requestCode: Int): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun pinWidgetToHomeScreen(context: Context, providerClass: Class<*>) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val myProvider = ComponentName(context, providerClass)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (appWidgetManager.isRequestPinAppWidgetSupported) {
                val successCallback = getLaunchAppIntent(context, 100)
                appWidgetManager.requestPinAppWidget(myProvider, null, successCallback)
                Toast.makeText(context, "Adding widget to home screen...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    "Your launcher does not support direct pinning. Long-press home screen -> Widgets -> Glass Widgets",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                "Long-press your home screen, select 'Widgets' and choose Glass Widgets",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun updateClockWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_glass_clock)
        val now = Date()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateFormat = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())

        views.setTextViewText(R.id.tv_clock, timeFormat.format(now))
        views.setTextViewText(R.id.tv_date, dateFormat.format(now))
        views.setTextViewText(R.id.tv_battery, "${getBatteryLevel(context)}%")
        views.setTextViewText(R.id.tv_weather_temp, "24°C")
        views.setTextViewText(R.id.tv_weather_cond, "Sunny")

        views.setOnClickPendingIntent(R.id.widget_root, getLaunchAppIntent(context, appWidgetId))
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    fun updateWeatherWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_glass_weather)
        views.setTextViewText(R.id.tv_location, "San Francisco")
        views.setTextViewText(R.id.tv_forecast_hi_lo, "H: 26°  L: 16°")
        views.setTextViewText(R.id.tv_weather_big_temp, "24°")
        views.setTextViewText(R.id.tv_weather_state, "Clear Sky")
        views.setTextViewText(R.id.tv_weather_details, "Humidity 48% • Wind 12km/h")

        views.setOnClickPendingIntent(R.id.widget_weather_root, getLaunchAppIntent(context, appWidgetId))
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    fun updateComboWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_glass_combo)
        val now = Date()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

        views.setTextViewText(R.id.tv_combo_clock, timeFormat.format(now))
        views.setTextViewText(R.id.tv_combo_date, dateFormat.format(now))
        views.setTextViewText(R.id.tv_combo_weather, "24°C • Mostly Sunny")
        views.setTextViewText(R.id.tv_combo_battery, "${getBatteryLevel(context)}%")

        views.setOnClickPendingIntent(R.id.widget_combo_root, getLaunchAppIntent(context, appWidgetId))
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    fun updateQuotesWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_glass_quotes)
        val quote = WidgetRepository.dailyQuotes.random()

        views.setTextViewText(R.id.tv_quote_text, "\"${quote.first}\"")
        views.setTextViewText(R.id.tv_quote_author, "— ${quote.second}")

        views.setOnClickPendingIntent(R.id.widget_quotes_root, getLaunchAppIntent(context, appWidgetId))
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
