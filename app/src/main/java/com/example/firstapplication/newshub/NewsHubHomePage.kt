package com.example.firstapplication.newshub

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.R
import cutomutils.printLogInfo

class NewsHubHomePage : AppCompatActivity() {
//    private var userAuthManager: UserAuthManager = UserAuthManager(this) // ❌ Invalid here — 'this' not available yet
    private lateinit var sessionCacheManager: SessionCacheManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_news_hub_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // interactWithSharedPref()
        // interactWithExternalFileStorage()
        interactWithExternalCacheStorage()
    }

    fun interactWithInternalFileStorage() {
        val userAuthManager = UserAuthManager(this)
        userAuthManager.saveUserToken("password!234")
        printLogInfo("Saved User Toke: ${userAuthManager.getUserToken() ?: "No Token Found"}")
        val preferences = mapOf(
            "content_filters" to listOf("tech", "science", "business"),
            "custom_themes" to mapOf(
                "background_color" to "#FFFFFF",
                "text_color" to "#000000"
            )
        )
        printLogInfo("Saving user preferences: ${userAuthManager.saveReadingPreferences(preferences)}")
    }

    private fun interactWithInternalCacheStorage() {
        sessionCacheManager = SessionCacheManager(this)
        val isSessionCached = sessionCacheManager.cacheUserSession(mapOf(
            "id" to 421,
            "name" to "sabari",
            "expiryTime" to 14354654,
            "refreshToken" to "g1o4m3m5a4l6a54",
        ))
        printLogInfo("isSessionCached: $isSessionCached")
        val article1 = listOf(mapOf(
            "id" to 1,
            "title" to "About AI",
            "imageUrl" to "url"
        ))
        printLogInfo("cacheReadingHistory: ${sessionCacheManager.cacheReadingHistory(article1)}")
        val article2 = listOf(mapOf(
            "id" to 2,
            "title" to "About Android",
            "imageUrl" to "url"
        ))
        printLogInfo("cacheReadingHistory: ${sessionCacheManager.cacheReadingHistory(article2)}")
        printLogInfo("clearInternalCache: ${sessionCacheManager.clearInternalCache()}")
    }

    private fun interactWithSharedPref() {
        // SET
        with(NewsHubPreferences(this)) {
            fontSize = 18
            darkModeEnabled = true
            isFirstLaunch = false
            searchHistory = setOf("thandhi", "dhinamalar", "hindu")

            // GET
            printLogInfo("fontSize: $fontSize")
            printLogInfo("darkModeEnabled: $darkModeEnabled")
            printLogInfo("isFirstLaunch: $isFirstLaunch")
            printLogInfo("preferredCategories: $preferredCategories")
            printLogInfo("searchHistory: $searchHistory")
        }
    }

    private fun interactWithExternalFileStorage() {
        with(OfflineArticleManager(this)) {
            // CREATE
            saveOfflineArticle("1a", "content 1a", listOf<ByteArray>())
            saveOfflineArticle("2a", "content 2a", listOf<ByteArray>())
            // READ
            val offlineArticles = getOfflineArticles()
            printLogInfo("offlineArticlesCount: ${offlineArticles.size} ")
            // DELETE
            deleteOfflineArticle("1a")
            deleteOfflineArticle("2a")
        }
    }

    private fun interactWithExternalCacheStorage() {
        with(ImageCacheManager(this)) {
            cacheImage("rohit.sharma", "hitman".toByteArray())
            getCachedImage("rohit.sharma")
        }
    }
}