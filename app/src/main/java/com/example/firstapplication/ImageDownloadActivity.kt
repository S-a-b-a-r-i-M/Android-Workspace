package com.example.firstapplication

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.firstapplication.databinding.ActivityImageDownloadBinding
import cutomutils.customToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.InputStream

data class RandomImageResponse(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val download_url: String
)

// Using Picsum Photos API - free random images
interface ImageApiService {
    @GET("v2/list")
    suspend fun getRandomImages(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 1
    ) : List<RandomImageResponse>
}

class ImageDownloadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageDownloadBinding
    private var currentImageInfo: Pair<String, String>? = null  // ImageName, ImageUrl

    // API service
    private val imageApiService by lazy { createApiService() }

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Notification permission granted or denied
        // Continue with download regardless
        downloadCurrentImage()
    }

    private val storagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted)
            downloadCurrentImage()
        else
            customToast(this, "Storage permission denied. Hence, can't download image.")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityImageDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.loadImageBtn.setOnClickListener {
            loadRandomImage()
        }

        binding.downloadImageBtn.setOnClickListener {
            requestPermissionAndDownload()
        }
    }

    private fun loadRandomImage() {
        binding.downloadImageBtn.isEnabled = false

        lifecycleScope.launch {
            val images = imageApiService.getRandomImages(limit = (1..100).random())

            if (images.isNotEmpty()) {
                val image = images[0]
                currentImageInfo = Pair("image_${image.id}.jpg", image.download_url)
                Log.d(TAG ,"response image: $image")

                // Load image with Glide
                Glide.with(this@ImageDownloadActivity)
                    .load(image.download_url)
                    .into(binding.imageView)

                binding.downloadImageBtn.isEnabled = true
            }
            else
                customToast(this@ImageDownloadActivity, "No Images found")
        }
    }

    private fun createApiService(): ImageApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://picsum.photos/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ImageApiService::class.java)
    }

    private fun requestPermissionAndDownload() {
        // Android 10+ with scoped storage - no permission needed for app-specific directories
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            // For Android 13+, request notification permission for download progress
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                 requestNotificationPermission()
            else
                 downloadCurrentImage()
        } else
            // Android 9 and below - need storage permission
             requestStoragePermission()
    }

    private fun requestNotificationPermission() {
        when {
            // CHECK IS PERMISSION ALREADY GRANTED
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                downloadCurrentImage()
            }
            // Skipped: Rationale
            else -> {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun requestStoragePermission() {
        when {
            // CHECK IS PERMISSION ALREADY GRANTED
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                downloadCurrentImage()
            }
            // Skipped: Rationale
            else -> {
                storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun downloadCurrentImage() {
        currentImageInfo?.let {
            lifecycleScope.launch {
                println(downloadImageToStorage(it.first, it.second))
            }
        } ?: Log.d(TAG, "No Current image info selected to download")
    }

    private suspend fun downloadImageToStorage(fileName: String, imageUrl: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Download image using OkHttp
                val client = OkHttpClient()
                val request = Request.Builder().url(imageUrl).build()
                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    Log.e(TAG, "response is failed to download image...")
                    return@withContext false
                }

                val inputStream = response.body?.byteStream() ?: return@withContext false
                // Save to appropriate location based on Android version
                val isSuccess = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    saveImageToMediaStore(inputStream, fileName)
                else
                    saveImageToExternalStorage(inputStream, fileName)

                inputStream.close()
                isSuccess
            } catch (e: Exception) {
                Log.e("ImageDownload", "Download failed", e)
                false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveImageToMediaStore(inputStream: InputStream, fileName: String): Boolean {
         return try {
             val contentValues = ContentValues().apply {
                 put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                 put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                 put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyApp")
             }

             val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
             uri?.let {
                 contentResolver.openOutputStream(it)?.use { outputStream ->
                     inputStream.copyTo(outputStream)
                 }
             } ?: return false

             true
         } catch (e: Exception) {
            Log.e("ImageDownload", "Failed to save to MediaStore", e)
            false
        }
    }

    fun saveImageToExternalStorage(inputStream: InputStream, fileName: String): Boolean {
        TODO("not yet implemented")
//        Log.w(TAG, "saveImageToMediaStore is invoked")
//        return true
    }

    companion object {
        private const val TAG = "ImageDownloadActivity"
    }

}