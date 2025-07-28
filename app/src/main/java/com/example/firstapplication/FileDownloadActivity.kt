package com.example.firstapplication

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.firstapplication.databinding.ActivityFileDonlowdBinding
import cutomutils.customToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import androidx.core.net.toUri

class FileDownloadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFileDonlowdBinding
    private val sampleFilesInfo = mutableMapOf<String, String>()
    private var selectedFileType: String? = null

    // Permission launcher for Android 9 and below
    private val storagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && selectedFileType != null)
            performDownload(selectedFileType!!)
        else
            customToast(this, "Storage Permission Denied")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFileDonlowdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        with(binding) {
            fileCreateBtn.setOnClickListener { createSampleFiles() }
            btnDownloadPdf.setOnClickListener { initiateDownload("pdf") }
            btnDownloadXml.setOnClickListener { initiateDownload("xml") }
            btnDownloadDoc.setOnClickListener { initiateDownload("doc") }
        }
    }

    // Step 1: Create sample files in internal storage
    private fun createSampleFiles() {
        try {
            // Create PDF content (simple text for demo)
            val pdfContent = """
                    %PDF-1.4
                    1 0 obj
                    <<
                    /Type /Catalog
                    /Pages 2 0 R
                    >>
                    endobj

                    2 0 obj
                    <<
                    /Type /Pages
                    /Kids [3 0 R]
                    /Count 1
                    >>
                    endobj

                    Sample PDF content created by MyApp
                """.trimIndent()

            // Create XML content
            val xmlContent = """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <document>
                        <title>Sample Document</title>
                        <content>
                            <section>
                                <heading>Introduction</heading>
                                <text>This is a sample XML document created by MyApp.</text>
                            </section>
                            <section>
                                <heading>Data</heading>
                                <item id="1">Sample Item 1</item>
                                <item id="2">Sample Item 2</item>
                            </section>
                        </content>
                        <metadata>
                            <created>2024-01-01</created>
                            <author>MyApp</author>
                        </metadata>
                    </document>
                """.trimIndent()

            // Create DOC content (RTF format for simplicity)
            val docContent = """
                    {\rtf1\ansi\deff0
                    {\fonttbl {\f0 Times New Roman;}}
                    \f0\fs24
                    Sample Document\par
                    \par
                    This is a sample document created by MyApp.\par
                    \par
                    Content:\par
                    - Item 1\par
                    - Item 2\par
                    - Item 3\par
                    \par
                    End of document.
                    }
                """.trimIndent()

            // SAVE FILES
            saveToInternal("sample_document.pdf", pdfContent)
            saveToInternal("sample_document.xml", xmlContent)
            saveToInternal("sample_document.doc", docContent)

            // STORE INFO
            sampleFilesInfo["pdf"] = "sample_document.pdf"
            sampleFilesInfo["xml"] = "sample_document.xml"
            sampleFilesInfo["doc"] = "sample_document.doc"

            // Update UI
            val fileList = """
                    Created Files:

                    📄 ${sampleFilesInfo["pdf"]} (PDF)
                    📄 ${sampleFilesInfo["xml"]} (XML)
                    📄 ${sampleFilesInfo["doc"]} (DOC)

                    Files are ready for download!
                """.trimIndent()
            binding.textView.text = fileList
            customToast(this, "Files are created.")
        } catch (exp: Exception) {
            Log.e(TAG, exp.message.toString())
        }
    }

    private fun saveToInternal(fileName: String, content: String) {
        File(filesDir, fileName).writeText(content)
    }

    // Step 2: Initiate download with permission check
    fun initiateDownload(fileType: String) {
        selectedFileType = fileType
        // Android 10+ - No permission needed for scoped storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            performDownload(fileType)
        else {
            // Android 9 and below - Need storage permission
            if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED)
                performDownload(fileType)
            else
                storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    // Step 3: Perform actual download
    fun performDownload(fileType: String) {
        val fileName = sampleFilesInfo[fileType]
        if (fileName == null) {
            Log.e(TAG, "fileName not found for filetype $fileType")
            return
        }

        lifecycleScope.launch {
            try {
                // Read File From Storage
                val file = File(filesDir, fileName)
                if (!file.exists()) {
                    Log.e(TAG, "No file found for for filename $fileName")
                    return@launch
                }

                val content = file.readBytes()
                if (saveToSharedStorage(fileName, content, getMimeType(fileType))) {
                    customToast(this@FileDownloadActivity, "file saved")
                    showSuccessMessage(fileName)
                }
                else
                    customToast(this@FileDownloadActivity, "file not saved")
            } catch (e: Exception) {
//                updateStatus("❌ Download error: ${e.message}")
                Log.e(TAG, e.message.toString())
            }
        }
    }

    private suspend fun saveToSharedStorage(
        fileName: String, content: ByteArray, mimeType: String
    ) : Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    saveToMediaStore(fileName, content, mimeType)
                else {
//                    saveToExternalStorage(fileName, content)
                    Log.w(TAG, "saveToExternalStorage is invoked")
                    false
                }
            } catch (e: Exception) {
                Log.e("FileDownload", "Failed to save file", e)
                false
            }
        }
    }

    // For Android 10+ - Use MediaStore API
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveToMediaStore(fileName: String, content: ByteArray, mimeType: String) : Boolean {
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, mimeType)
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/MyApp")
        }

        val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        return uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                outputStream.write(content)
            }
            true
        } ?: false
    }

    private fun showSuccessMessage(fileName: String) {
        val message = "File saved to Downloads/MyApp/$fileName"
        customToast(this, message)

        // Optional: Show option to open file
        runOnUiThread {
            AlertDialog.Builder(this)
                .setTitle("Download Complete")
                .setMessage("$fileName has been downloaded successfully!")
                .setPositiveButton("Open Folder") { _, _ ->
                    openDownloadsFolder()
                }
                .setNegativeButton("OK", null)
                .show()
        }
    }

    private fun openDownloadsFolder() {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(
                    "content://com.android.externalstorage.documents/document/primary%3ADownload".toUri(),
                    "resource/folder"
                )
            }
            startActivity(intent)
        } catch (e: Exception) {
            // Fallback: Open file manager
            try {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "*//*" }
                startActivity(intent)
            } catch (e2: Exception) {
                customToast(this, "Please check Downloads folder manually")
            }
        }
    }

    // Helper methods
    fun getMimeType(fileType: String) = when(fileType.lowercase()) {
        "pdf" -> "application/pdf"
        "xml" -> "application/xml"
        "doc" -> "application/msword"
        else -> "application/octet-stream"
    }

    companion object {
        private const val TAG = "FileDownloadActivity"
    }
}