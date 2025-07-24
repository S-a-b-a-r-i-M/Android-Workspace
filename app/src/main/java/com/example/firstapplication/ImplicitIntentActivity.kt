package com.example.firstapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cutomutils.customToast

class ImplicitIntentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_implicit_intent)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // CHECK IF ANY IMAGE HAS BEEN SENT
        if (intent.action == Intent.ACTION_SEND && intent.type?.startsWith("image") == true)
            receiveImage(intent)
        else
            customToast(this, "No image has been sent.")
    }

    private fun receiveImage(intent: Intent) {
        val imageUri: Uri? = intent.getParcelableExtra(Intent.EXTRA_STREAM)
        if (imageUri != null) {
            val imageView: ImageView = findViewById(R.id.imageView)
            imageView.setImageURI(imageUri)
        } else
            customToast(this, "Image Uri not found.")
    }
}