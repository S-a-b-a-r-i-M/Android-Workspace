package com.example.firstapplication

import android.content.Intent
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.databinding.ActivityCameraBinding
import cutomutils.printLogInfo
import java.io.File

class PickerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding

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
        uri?.let { handleSelectedFile(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        setTakePictureButton(binding.takePictureBtn)
        setFileChooserButton(binding.pickFileBtn) // User picks file → Your app gets URI → Copy to internal storage → Use the file
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

            printLogInfo("File saved into path: ${destinationFile.absolutePath}")
        } catch (exp: Exception) {
            Log.e("FilePicker", "Error handling file: ${exp.message}")
        }
    }

    companion object {
        private const val TAG = "CameraActivity"
    }
}