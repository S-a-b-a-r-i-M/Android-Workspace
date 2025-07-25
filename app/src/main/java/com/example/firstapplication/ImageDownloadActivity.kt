package com.example.firstapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.firstapplication.databinding.ActivityImageDownloadBinding
import cutomutils.printLogInfo
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class RandomImageResponse(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val downloadUrl: String
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

    // API service
    private val imageApiService by lazy { createApiService() }

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

        loadRandomImage()
    }

    private fun setUpClickListeners() {
        binding.loadImageBtn.setOnClickListener {
            loadRandomImage()
        }

        binding.downloadImageBtn.setOnClickListener {

        }
    }

    private fun loadRandomImage() {
        binding.downloadImageBtn.isEnabled = false

        lifecycleScope.launch {
            val images = imageApiService.getRandomImages(limit = 10)

            if (images.isNotEmpty()) {
                val image = images[0]
                printLogInfo("image: $image")

                // Load image with Glide
                Glide.with(this@ImageDownloadActivity)
                    .load(image.url)
                    .into(binding.imageView)
            }
        }
    }

    private fun createApiService(): ImageApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://picsum.photos/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ImageApiService::class.java)
    }

    companion object {
        private const val TAG = "ImageDownloadActivity"
    }

}