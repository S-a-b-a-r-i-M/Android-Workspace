package com.example.firstapplication.learn_app_widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.example.firstapplication.R
import cutomutils.logDebug
import cutomutils.logInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyWidgetProvider1 : AppWidgetProvider() {

    /**
     * Called when widget is added or periodically as per updatePeriodMillis
     */
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        // Update all widget instances
        if (context == null || appWidgetManager == null || appWidgetIds == null) return

        for (widgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId)
        }
    }

    /**
     * Called when first widget instance is created
     */
    override fun onEnabled(context: Context?) {
        // Start any background work if needed
    }

    /**
     * Called when last widget instance is removed
     */
    override fun onDisabled(context: Context) {
        // Clean up background work
    }

    /**
     * Called when widget is deleted
     */
    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        // Clean up for specific widget instances
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {
        // Create Remote Views
        val views = RemoteViews(context.packageName, R.layout.widget_layout_1)

        // Update UI - note you can't directly access views!
        views.setTextViewText(R.id.widget_title, "Widget Using RemoteViews")
        views.setTextViewText(R.id.widget_content, "This widget is updated at : ${getCurrentTime()}")

        // Set up click listener
        val intent = Intent(context, MyWidgetProvider1::class.java).apply {
            action = ACTION_WIDGET_CLICK
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            widgetId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_btn, pendingIntent)

        // Tell the AppWidgetManager to update the widget
        appWidgetManager.updateAppWidget(widgetId, views)
    }

    /**
     * Handle custom actions
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent == null || context == null) return

        if (intent.action == ACTION_WIDGET_CLICK) {
            val widgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
            logDebug("------------------ MyWidgetProvider1 received widgetId: $widgetId ------------------")
            if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                logInfo("------------------ MyWidgetProvider1 received an click event. ------------------")
                updateWidget(context, AppWidgetManager.getInstance(context), widgetId)
            }
        }
    }

    private fun getCurrentTime() : String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    companion object {
        private const val ACTION_WIDGET_CLICK = "com.example.firstapplication.ACTION_WIDGET_CLICK"
    }

}