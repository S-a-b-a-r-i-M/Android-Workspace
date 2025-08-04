package com.example.firstapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.databinding.ActivityResponsiveBinding
import cutomutils.setGotoTargetPage

class ResponsiveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResponsiveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResponsiveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.imageView1.setImageResource(R.drawable.sponge_bob)
        binding.imageView2.setImageResource(R.drawable.mickey_mouse_disney)

        binding.nextPageBtn?.setGotoTargetPage(PracticeLayoutActivity1::class.java)
    }
}