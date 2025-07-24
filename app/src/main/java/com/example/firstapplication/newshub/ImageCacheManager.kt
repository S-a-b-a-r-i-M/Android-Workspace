package com.example.firstapplication.newshub

import android.content.Context
import android.util.Log
import java.io.File

//  External Cache - Image Cache
class ImageCacheManager(private val context: Context) {

    private val cacheDir = File(context.externalCacheDir, "images")
    private val maxCacheSize = 50 * 1024 * 1024L // 50MB
    private val targetCacheSize = 40 * 1024 * 1024L // 40MB after cleanup

    // TASK: Cache downloaded article images
    fun cacheImage(imageUrl: String, imageData: ByteArray): Boolean {
        // Why external cache? Images are large, non-sensitive
        return try {
            // Create images directory if needed
            if (!cacheDir.exists() && !cacheDir.mkdir()) {
                Log.e(TAG, "Failed to create cache directory")
                return false
            }

            // Save Image
            val fileName = generateFilename(imageUrl) // Generate filename from URL hash
            val imageFile = File(cacheDir, fileName)
            imageFile.writeBytes(imageData)

            Log.d(TAG, "Image cached: $fileName (${imageData.size} bytes)")
            true
        } catch (exp: Exception) {
            Log.e(TAG, "Error caching image: ${exp.message}")
            false
        }
    }

    // TASK: Retrieve cached image
    fun getCachedImage(imageUrl: String): ByteArray? {
        return try {
            val fileName = generateFilename(imageUrl)
            val cacheFile = File(cacheDir, fileName)
            if (!cacheFile.exists()) {
                Log.d(TAG, "Cache miss for: $fileName")
                return null
            }

            // Update file access time for LRU
            cacheFile.setLastModified(System.currentTimeMillis())
            Log.d(TAG, "Cache hit for: $fileName")
            cacheFile.readBytes()
        } catch (exp: Exception) {
            Log.e(TAG, "Error reading cached image: ${exp.message}")
            null
        }
    }

    // TASK: Implement LRU cache cleanup
    fun cleanupImageCache() {
        try {
            if (!cacheDir.exists()) return

            val cacheFiles = cacheDir.listFiles() ?: return
            val totalSize = cacheFiles.sumOf { it.length() }

            if (totalSize <= maxCacheSize)
                return // No cleanup needed

            Log.i(TAG, "Cache cleanup needed. Current size: ${totalSize / 1024 / 1024}MB")

            // Sort by last modified (oldest first)
            val sortedFiles = cacheFiles.sortedBy { it.lastModified() }

            var currentSize = totalSize
            var deletedCount = 0

            for (file in sortedFiles) {
                if (currentSize <= targetCacheSize) break

                val fileSize = file.length()
                if (file.delete()) {
                    currentSize -= fileSize
                    deletedCount++
                }
            }

            Log.i(TAG, "Cache cleanup completed. Deleted: $deletedCount files, " +
                    "Size reduced from ${totalSize / 1024 / 1024}MB to ${currentSize / 1024 / 1024}MB")

        } catch (exp: Exception) {
            Log.e(TAG, "Error during cache cleanup: ${exp.message}")
        }
    }


    private fun generateFilename(imageUrl: String): String {
        return "${imageUrl.hashCode().toString().replace("-", "")}.jpg"
    }

    companion object {
        private const val TAG = "ImageCacheManager"
    }
}