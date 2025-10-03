package com.example.firstapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.databinding.ActivityPractice3Binding

class PracticeActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityPractice3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPractice3Binding.inflate(layoutInflater)
        //setContentView(R.layout.activity_practice3)
        setContentView(binding.root) // TODO-Doubt: while adding view using inflater the parent layout margin is missing
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
    }

    private fun setupUI() {
        with(binding) {
            bedroomsCounter.apply {
                maxCount = 10
            }

            iEditViewName.onValueChanged = { newValue ->
                println("Changed Value")
            }
        }
    }
}