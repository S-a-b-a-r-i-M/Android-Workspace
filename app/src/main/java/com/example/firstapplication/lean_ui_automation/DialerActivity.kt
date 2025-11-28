 package com.example.firstapplication.lean_ui_automation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.R
import com.example.firstapplication.databinding.ActivityDialerBinding
import cutomutils.customToast
import java.security.Permission

 class DialerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDialerBinding
    private var phoneNumber: String = ""

     private val permissionLauncher = registerForActivityResult(
         ActivityResultContracts.RequestPermission()
     ) { isGranted ->
         if (isGranted)
             makeCall(phoneNumber)
         else
             customToast(this, "Permission denied to make call.")
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDialerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.button2.setOnClickListener {
            phoneNumber = binding.editTextText.text.toString()
            if (phoneNumber.isNotBlank()) {
                val isPermissionGranted = ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
                if (isPermissionGranted)
                    makeCall(phoneNumber)
                else
                    permissionLauncher.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }

     private fun makeCall(phoneNumber: String) {
         val intent = Intent(Intent.ACTION_CALL)
         intent.data = "tel:$phoneNumber".toUri()
         startActivity(intent)
     }
}