package com.example.firstapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.databinding.ActivityShareImageBinding
import cutomutils.customToast

class ShareImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShareImageBinding
    private var selectedImageUri: Uri? = null

    // Activity Result Launcher for picking image
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK && it.data != null) {
            selectedImageUri = it.data?.data
            selectedImageUri?.let {
                // Display the selected image
                binding.imageView.setImageURI(selectedImageUri)
                binding.shareImgBtn.isEnabled = true
                customToast(this, "Image selected successfully!")
            }
        }
    }
    // Permission launcher
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted)
            openImagePicker()
        else
            customToast(this, "Permission denied to pick image.")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityShareImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initially disable share button
        binding.shareImgBtn.isEnabled = false
        binding.pickImgBtn.setOnClickListener {
            checkPermissionAndPickImage()
        }
        binding.shareImgBtn.setOnClickListener {
            shareImage()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private fun checkPermissionAndPickImage() {
        // Check which permission to request (for Android 6.0+)
        Log.i(
            "ShareImageActivity",
            "The SDK version of the software currently running on: ${android.os.Build.VERSION.SDK_INT}"
        )
        val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            Log.i("ShareImageActivity", "already permission granted")
            // Permission already granted
            openImagePicker()
        }
        else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            // Show explanation to user
            customToast(this, "Permission needed to access images")
            permissionLauncher.launch(permission)
        } else {
            Log.i("ShareImageActivity", "invoking permission launcher to get permission")
            // Request permission
            permissionLauncher.launch(permission)
        }
    }

    private fun shareImage() {
        selectedImageUri?.let { uri ->
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "Sharing an image from my app")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            // Create chooser to let user pick which app to share with
            val chooser = Intent.createChooser(shareIntent, "Share image via...")
            startActivity(chooser)
        } ?: run {
            customToast(this, "No image selected to share")
        }
    }

}