package com.example.firstapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.firstapplication.databinding.ActivityBottomNavigationAtivityBinding

class BottomNavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBottomNavigationAtivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBottomNavigationAtivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.bnavHome -> {
                    loadFragment(Home())
                    true
                }
                R.id.bnav_profile -> {
                    loadFragment(About())
                    true
                }
                R.id.bnav_add -> {
                    Toast.makeText(this, "Add new status", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.bnav_logout -> {
                    Toast.makeText(this, "Logout User", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            loadFragment(Home())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}