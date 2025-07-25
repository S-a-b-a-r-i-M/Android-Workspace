package com.example.firstapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.databinding.ActivityPickerBinding
import cutomutils.customToast
import cutomutils.printLogInfo
import java.io.File

class PickerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPickerBinding

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val imageBitMap = it.data?.extras?.get("data") as? Bitmap
            if (imageBitMap != null) binding.imageView.setImageBitmap(imageBitMap)
        }
        else
            Log.d(TAG, "No Images returned by camera.")
    }

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        // usi: content uri
        uri?.let { handleSelectedFile(it) }
    }

    private val isPhoneCallPermissionGranted: Boolean
        get() = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED

    private val phoneNumber: String
        get() = binding.phoneET.text.toString().trim()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted)
            makeCall(phoneNumber)
        else
            customToast(this, "Permission denied to make call.")
    }

    // TODO: Fix
    private val multiPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        println("permissions: $permissions")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        multiPermissionLauncher.launch(arrayOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA
        ))

        // CAMERA
        setTakePictureButton(binding.takePictureBtn)

        // User picks file → Your app gets URI → Copy to internal storage → Use the file
        setFileChooserButton(binding.pickFileBtn)

        // DIAL
        setMakeCallButton(binding.makeCallBtn)
    }

    private fun setTakePictureButton(button: Button) {
        button.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        }
    }

    private fun setFileChooserButton(button: Button) {
        button.setOnClickListener {
            filePickerLauncher.launch("*/*")

            // Or specific types:
            // filePickerLauncher.launch("application/pdf")  // PDF only
            // filePickerLauncher.launch("image/*")          // Images only
        }
    }

    private fun getFileName(uri: Uri): String {
        println("uri: $uri")
        val cursor = contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        } ?: "unknown_file"
    }

    private fun getFileSize(uri: Uri): Long {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
            it.moveToFirst()
            it.getLong(sizeIndex)
        } ?: 0L
    }

    private fun getFileInfo(uri: Uri): Pair<String, Long> {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return cursor?.let {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
            it.moveToFirst()
            Pair(it.getString(nameIndex), it.getLong(sizeIndex))
        } ?: Pair("unknown_file", 0L)
    }

    private fun handleSelectedFile(uri: Uri) {
        try {
            println("uri: $uri")
            // GET FILE INFO
            val (fileName, fileSize) = getFileInfo(uri)

            // SAVE INTO INTERNAL STORAGE
            val inputStream = contentResolver.openInputStream(uri)
            val destinationFile = File(filesDir, fileName)

            inputStream?.use { input ->
                destinationFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            // DISPLAY
            binding.fileInfo.text = "Selected File: ${fileName}(${fileSize / 1000}KB)"

            printLogInfo("File saved into path: ${destinationFile.absolutePath}")
        } catch (exp: Exception) {
            Log.e("FilePicker", "Error handling file: ${exp.message}")
        }
    }

    private fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = "tel:$phoneNumber".toUri()
        startActivity(intent)
    }

    private fun setMakeCallButton(button: Button) {
        button.setOnClickListener {
            if (phoneNumber.isNotEmpty()) {
                // CHECK, PERMISSION IS ALREADY GRANTED
                if (isPhoneCallPermissionGranted)
                    makeCall(phoneNumber)
                else
                    // Request permission
                    permissionLauncher.launch(Manifest.permission.CALL_PHONE)
            } else {
                customToast(this, "Enter valid phone number")
//                binding.phoneET.focusable = true
            }
        }
    }

    companion object {
        private const val TAG = "CameraActivity"
    }
}