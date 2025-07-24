package com.example.firstapplication

import android.os.Bundle
import android.os.storage.StorageManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cutomutils.printLogInfo
import java.io.File

class DifferentStorageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_different_storage)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    // INTERNAL FILE
        withInternalFiles()

    // INTERNAL CACHE
        // WRITE
        val fileName = "one_piece"
        var cacheFile = File.createTempFile(fileName, null, cacheDir)
        cacheFile.writeText("Luffy, Zoro, Sanji")

        // READ
//        cacheFile = File(cacheDir, fileName)
//        printLogInfo("cacheFile content: ${cacheFile.readText()}")
    }

    fun withInternalFiles() {
        // CREATE AND WRITE
        val filename1 ="internal_file"
        printLogInfo("filesDir: $filesDir")
        val file = File(filesDir, filename1)
        file.writeText("Hi tech universe...👋")

        val filename2 = "naruto"
        val narutoCharacters = "Naruto, Sakura, Saske, Kakashi, Guy, Lee"
        openFileOutput(filename2, MODE_PRIVATE).use {
            it.write(narutoCharacters.toByteArray())
            printLogInfo("content written using openFileOutput")
        }

        // READ
        openFileInput(filename2).bufferedReader().useLines { lines ->
            val content = lines.fold("") { acc, newLine ->
                "$acc\n$newLine"
            }

            printLogInfo("content: $content")
        }
    }

    companion object {
        private const val TAG = "DifferentStorageActivity"
    }
}