package com.example.firstapplication.learn_app_widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import com.example.firstapplication.R

class ExampleAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        Log.i("ExampleAppWidgetProvider", "onUpdate: Called by system.")
        // There may be multiple widgets active, so update all of them
        if (context == null || appWidgetManager == null || appWidgetIds == null) return

        for (widgetId in appWidgetIds)
            updateAppWidget(context, appWidgetManager, widgetId)
    }

    override fun onEnabled(context: Context?) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context?) {
        // Enter relevant functionality for when the last widget is disabled
    }

    internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        // Construct Remote Views Objects
        val views = RemoteViews(context.packageName, R.layout.single_text_view)
        views.setTextViewText(R.id.textView, "Hello Ladies and GentleMan")
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}