package com.example.firstapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapplication.databinding.ActivityAmazonNavigationBinding
import cutomutils.customToast

class AmazonNavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAmazonNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAmazonNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        // STEP 1: INITIALIZE LAYOUT, TOOLBAR & NAVIGATION VIEW
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navDrawerView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navd_trending -> customToast(this, "Tending")
                R.id.navd_electronics -> customToast(this, "Electronics")
                R.id.navd_sports -> customToast(this, "Sports")
                R.id.navd_beauty -> customToast(this, "Beauty")
                else -> {
                    Log.w("AmazonNavigationActivity", "Invalid navDrawerView menu item selected")
                    return@setNavigationItemSelectedListener false
                }
            }
            return@setNavigationItemSelectedListener true
        }

        // STEP 2: SET THE ACTION BAR TOGGLE
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }
}