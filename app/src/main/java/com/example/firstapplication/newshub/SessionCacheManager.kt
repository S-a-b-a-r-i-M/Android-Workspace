package com.example.firstapplication.newshub

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

// Internal Cache - Session Data
class SessionCacheManager(private val context: Context) {
    private val maxHistoryItems = 10

    // TASK: Cache current user session data
    fun cacheUserSession(sessionData: Map<String, Any>): Boolean {
        val sessionsDirName = "sessions"
        return try {
            val cacheDir = File(context.cacheDir, sessionsDirName)
            if (!cacheDir.exists() && !cacheDir.mkdir()) {
                Log.e(TAG, "Failed to create sessions directory")
                return false
            }
            // TODO-FIX: MULTIPLE CACHE FILE IS GETTING CREATED
            val sessionFile = File(cacheDir,"session.json")
            sessionFile.writeText(mapToJsonString(sessionData))
            Log.i(TAG, "Session cached successfully at ${sessionFile.absolutePath}")
            Log.d(TAG, "Session size: ${sessionFile.length()} bytes")
            true
        } catch (exp: Exception) {
            Log.e(TAG, "${exp.message}")
            false
        }
    }

    // TASK: Get cached session data
    fun getCachedSession(): Map<String, Any>? {
        return try {
            val sessionFile = File(context.cacheDir, "session.json")
            if (sessionFile.exists()) {
                val jsonString = sessionFile.readText()
                jsonToMap(jsonString)
            } else {
                Log.w(TAG, "No cached session found")
                null
            }
        } catch (exp: Exception) {
            Log.e(TAG, "Error reading cached session: ${exp.message}")
            null
        }
    }

    // TASK: Cache recently viewed articles (sensitive reading history)
    fun cacheReadingHistory(articles: List<Map<String, Any>>): Boolean {
        // Path: /data/data/com.newshub.pro/cache/reading_history.json
        return try {
            // ✅ FIXED: Maintain proper array structure
            val historyFile = File(context.cacheDir, "reading_history.json")

            // Limit to last 10 articles
            val limitedArticles = articles.takeLast(maxHistoryItems)
            val historyJson = JSONArray(limitedArticles).toString(2)

            historyFile.writeText(historyJson)
            Log.i(TAG, "Reading history cached: ${limitedArticles.size} articles")
            true

        } catch (exp: Exception) {
            Log.e(TAG, "Error caching reading history: ${exp.message}")
            false
        }
    }

    // TASK: Add single article to reading history
    fun addToReadingHistory(article: Map<String, Any>): Boolean {
        return try {
            // Get existing history
            val existingHistory = getCachedReadingHistory().toMutableList()

            // Remove if already exists (to move to end)
            val articleId = article["id"] as? String
            existingHistory.removeAll { (it["id"] as? String) == articleId }

            // Add to end
            existingHistory.add(article)

            // Keep only last 10
            val limitedHistory = existingHistory.takeLast(maxHistoryItems)

            // Save back
            cacheReadingHistory(limitedHistory)

        } catch (exp: Exception) {
            Log.e(TAG, "Error adding to reading history: ${exp.message}")
            false
        }
    }

    // TASK: Get cached reading history
    fun getCachedReadingHistory(): List<Map<String, Any>> {
        return try {
            val historyFile = File(context.cacheDir, "reading_history.json")
            if (historyFile.exists()) {
                val jsonString = historyFile.readText()
                val jsonArray = JSONArray(jsonString)

                // Convert JSONArray to List<Map<String, Any>>
                (0 until jsonArray.length()).map { index ->
                    val jsonObject = jsonArray.getJSONObject(index)
                    jsonToMap(jsonObject.toString())
                }
            } else {
                Log.w(TAG, "No reading history cache found")
                emptyList()
            }
        } catch (exp: Exception) {
            Log.e(TAG, "Error reading cached history: ${exp.message}")
            emptyList()
        }
    }

    // TASK: Clear all internal cache
    fun clearInternalCache(): Boolean {
        // Requirements:
        // - Delete all files in cache directory
        // - Handle errors gracefully
        // - Log cache size before clearing
        return try {
            val sessionDir = File(context.cacheDir, "sessions")
//            sessionDir.deleteRecursively() // It will delete the entire folder
            sessionDir.listFiles()?.forEach { it.delete() }
            return true
        } catch (exp: Exception) {
            Log.e(TAG, "${exp.message}")
            false
        }
    }

    // Helper Functions
    private fun mapToJsonString(map: Map<String, Any>): String { // TODO: DOUBT: What if i have an object as a value
        return JSONObject(map).toString(2)
    }

    private fun jsonToMap(jsonString: String): Map<String, Any> {
        val jsonObject = JSONObject(jsonString)
        val map = mutableMapOf<String, Any>()

        jsonObject.keys().forEach { key ->
            map[key] = jsonObject.get(key)
        }

        return map
    }

    companion object {
        private const val TAG = "SessionCacheManager"
    }
}