package com.example.firstapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.databinding.ActivitySharedPrefenceDemoBinding
import cutomutils.customToast

class SharedPreferenceDemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySharedPrefenceDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySharedPrefenceDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val sharedPreferences = getSharedPreferences("Note", MODE_PRIVATE)
        val sharedPreferences = getPreferences(MODE_PRIVATE)

        binding.saveNoteBtn.setOnClickListener {
            val note = binding.noteET.text.toString().trim()
            if (note.isNotEmpty()) {
//                sharedPreferences.edit()
//                    .putString("note", note)
//                    .app

                sharedPreferences.edit {
                    putString("note", note)
                    putFloat("salary", 50_000.1f)
                    putStringSet("notes", setOf("note1", "note2", "note3"))
                }
                customToast(this, "note saved successfully")
                binding.noteET.text.clear()
            }
        }


        binding.showNoteBtn.setOnClickListener {
            binding.displayNoteTV.text = sharedPreferences.getString("note", "No Note Found")
        }
    }
}