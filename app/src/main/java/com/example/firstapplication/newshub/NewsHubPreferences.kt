package com.example.firstapplication.newshub

import android.content.Context
import androidx.core.content.edit

// SharedPreferences - App Settings & User Preferences
class NewsHubPreferences(context: Context) {
    private val userPrefs = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)
    private val appPrefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    // TODO-DOUBT: what will happen if i gave mode not as private ?
    private val tempPrefs = context.getSharedPreferences("temp_data", Context.MODE_PRIVATE)

    // TASK: User Customization Settings
    var fontSize: Int
        get() = userPrefs.getInt("fontSize", 16)
        set(value) = userPrefs.edit { putInt("fontSize", value) }

    var darkModeEnabled: Boolean
        get() = userPrefs.getBoolean("darkModeEnabled", false)
        set(value) = userPrefs.edit { putBoolean("darkModeEnabled", value) }

    var preferredCategories: Set<String?>?
        get() = userPrefs.getStringSet("preferredCategories", setOf<String>())
        set(value) = userPrefs.edit { putStringSet("preferredCategories", value) }

    // TASK: App State Management
    var isFirstLaunch: Boolean
        get() = appPrefs.getBoolean("isFirstLaunch", false)
        set(value) = appPrefs.edit { putBoolean("isFirstLaunch", value) }

    // TASK: Temporary data
    var searchHistory: Set<String?>?
        get() = tempPrefs.getStringSet("searchHistory", setOf<String>())
        set(value) = tempPrefs.edit { putStringSet("searchHistory", value) }
}