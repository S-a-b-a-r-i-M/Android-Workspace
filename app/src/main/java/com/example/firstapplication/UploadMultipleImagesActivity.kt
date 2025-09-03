package com.example.firstapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.databinding.ActivityUploadMultipleImagesBinding
import cutomutils.logInfo

class UploadMultipleImagesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadMultipleImagesBinding

    val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { it ->
        if (it.resultCode == RESULT_OK && it.data != null) {
            // handle max images
            val clipData = it.data?.clipData
            val imageUris = mutableListOf<Uri>()

            if (clipData != null) {
                // Multiple images selected
                for (i in 0 until clipData.itemCount) {
                    imageUris.add(clipData.getItemAt(i).uri)
                }
            } else {
                // Single images selected
                it.data?.data?.let { uri ->  imageUris.add(uri) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUploadMultipleImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowInsets()


        binding.btnPickImages.setOnClickListener {
            // action will tell what exactly we are intent to do.
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) //
            }
            imagePickerLauncher.launch(intent)
        }
    }

    fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}