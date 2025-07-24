package com.example.firstapplication.newshub

import android.content.Context
import android.util.Log
import androidx.compose.ui.util.fastForEachIndexed
import java.io.File

// External App-Specific Files - Offline Articles
class OfflineArticleManager(private val context: Context) {
    private val offlineDir = File(context.getExternalFilesDir(null), "offline")

    // TASK: Save full articles for offline reading
    fun saveOfflineArticle(articleId: String, content: String, images: List<ByteArray>): Boolean {
        // - Why external? Large files, more space needed
        return try {
            // Create Offline Dir
            if (!offlineDir.exists() && !offlineDir.mkdir()) {
                Log.e(TAG, "Failed to create offline directory: $articleId")
                return false
            }

            // Create Article Dir
            val articleDir = File(offlineDir, articleId)
            if (!articleDir.exists() && !articleDir.mkdir()) {
                Log.e(TAG, "Failed to create article directory: $articleId")
                return false
            }

            // Save Content
            val contentFile = File(articleDir, "content.html")
            contentFile.writeText(content)

            // Save Images With Sequential naming
            val imagesDir = File(articleDir, "images")
            if (!imagesDir.exists() && !imagesDir.mkdir()) {
                Log.e(TAG, "Failed to create images directory: $articleId")
                return false
            }
            images.fastForEachIndexed { idx, image ->
                val imageFile = File(imagesDir, "image_${idx + 1}.jpg")
                imageFile.writeBytes(image)
            }

            Log.i(TAG, "Article saved offline: $articleId (${images.size} images)")
            true
        } catch (exp: Exception) {
            Log.e(TAG, "Error saving offline article $articleId: ${exp.message}")
            false
        }
    }

    // TASK: Get all offline articles
    fun getOfflineArticles(): List<String> {
        return try {
            if (!offlineDir.exists()) {
                Log.w(TAG, "Offline directory doesn't exist")
                return emptyList()
            }

            offlineDir.listFiles()?.filter { it.isDirectory }?.map { it.name } ?: emptyList()
        } catch (exp: Exception) {
            Log.e(TAG, "Error getting offline articles: ${exp.message}")
            emptyList()
        }
    }

    // TASK: Delete specific offline article
    fun deleteOfflineArticle(articleId: String): Boolean {
        return try {
            val articleDir = File(offlineDir, articleId)
            if (!articleDir.exists()) {
                Log.w(TAG, "Article directory doesn't exist: $articleId")
                return true // Already deleted
            }

            if (articleDir.deleteRecursively()) {
                Log.i(TAG, "Article deleted: $articleId")
                true
            } else {
                Log.e(TAG, "Failed to delete article: $articleId")
                false
            }
        } catch (exp: Exception) {
            Log.e(TAG, "Error deleting article $articleId: ${exp.message}")
            false
        }
    }

    companion object {
        private const val TAG = "OfflineArticleManager"
    }
}