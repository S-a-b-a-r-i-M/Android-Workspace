package com.example.firstapplication.learn_app_widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.Toast
import com.example.firstapplication.R
import cutomutils.logWarning
import kotlinx.coroutines.delay

data class TodoData(val id: Int, val title: String, var isCompleted: Boolean = false)

val todoList = listOf<TodoData>(
    TodoData(1, "Learn Jetpack Compose", false),
    TodoData(2, "Learn Jetpack Glance", false),
    TodoData(3, "Learn RemoteViews", false),
    TodoData(4, "Learn Data Binding", false),
    TodoData(5, "Practice English Fluency", false),
    TodoData(6, "Improve Volleyball", false),
    TodoData(7, "Improve Cricket", false),
    TodoData(8, "Improve Shuttlecock", false),
)

class TodoWidgetProvider : AppWidgetProvider() {

/* Phase 1: Widget Creation (Your App Process)
     1.1. onUpdate() is called
   Phase 2: System Processes RemoteViews
     AppWidgetManager receives RemoteViews:
     ├─ "Okay, I need to render this widget"
     ├─ "I see there's a ListView with id 'widget_list'"
     ├─ "It has a setRemoteAdapter instruction"
     ├─ "The Intent points to WidgetListService"
     └─ "I need to bind to that service to get the data"
   Phase 3: Service Binding (Launcher Process)
 */
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        // Update all widget instances
        if (context == null || appWidgetManager == null || appWidgetIds == null) return

        for (widgetId in appWidgetIds)
            updateWidget(context, appWidgetManager, widgetId)
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_todos_layout)

        // Tell the ListView "hey, get your data from TodoWidgetService"
        val intent = Intent(context, TodoWidgetService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)

        views.setRemoteAdapter(R.id.widget_listView, intent)

        // Setup click handling for list items
        val clickIntent = Intent(context, TodoWidgetProvider::class.java).apply {
            action = ACTION_ITEM_CLICK
            // Put common data here (applies to ALL list items)
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }
        val clickPendingIntent = PendingIntent.getBroadcast(
            context,
            widgetId,
            clickIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        views.setPendingIntentTemplate(R.id.widget_listView, clickPendingIntent)

        appWidgetManager.updateAppWidget(widgetId, views)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (context == null || intent == null) return

        if (intent.action == ACTION_ITEM_CLICK) {
            val completedTodoId = intent.getIntExtra(TODO_ID, -1)
            val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
            if (completedTodoId == -1 || widgetId == -1) {
                logWarning("Completed TodoId or widgetId is not found todoId :$completedTodoId or widgetId: $widgetId")
                return
            }

            todoList.find { it.id == completedTodoId }?.let {
                it.isCompleted = true
                Toast.makeText(context, "${it.title} is Completed", Toast.LENGTH_SHORT)
                refreshWidget(context, widgetId)
            }
        }
    }

    private fun refreshWidget(context: Context, widgetId: Int) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        appWidgetManager.notifyAppWidgetViewDataChanged(intArrayOf(widgetId), R.id.widget_listView)
    }

    companion object {
        private const val ACTION_ITEM_CLICK = "com.example.firstapplication.ACTION_ITEM_CLICK"
        const val TODO_ID = "todo"
    }
}


class TodoWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent) = TodoWidgetRemoteViewsFactory(applicationContext, intent)
}


class TodoWidgetRemoteViewsFactory(
    private val context: Context, intent: Intent
) : RemoteViewsService.RemoteViewsFactory {

    private var items = listOf<TodoData>()

    override fun onCreate() {
        // Initialize data
        loadData()
    }

    override fun onDataSetChanged() {
        // Refresh data - called when notifyAppWidgetViewDataChanged is called
        loadData()
    }

    private fun loadData() {
        // We can fetch data from db, api, etc...
        items = todoList.filter { !it.isCompleted }
    }

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.single_todo_item)
        views.setTextViewText(R.id.todoTV, items[position].title)
        views.setCompoundButtonChecked(R.id.checkBox, items[position].isCompleted)

        // Set Click Listener
        val fillInIntent = Intent()
        fillInIntent.putExtra(TodoWidgetProvider.TODO_ID, items[position].id)
        views.setOnClickFillInIntent(R.id.checkBox, fillInIntent)

        return views
    }

    override fun getCount() = items.size

    // As of now our data don't contains any id so returning position
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getViewTypeCount() = 1

    override fun hasStableIds() = true

    override fun getLoadingView(): RemoteViews? = null

    override fun onDestroy() {
        items = emptyList()
    }

}

