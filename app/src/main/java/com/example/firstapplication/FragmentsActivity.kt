package com.example.firstapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.firstapplication.databinding.ActivityFragments5Binding

class FragmentsActivity : StackInfoAppCompactActivity() {
    lateinit var binding: ActivityFragments5Binding // Used to bind the view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFragments5Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadFragment(Fragment1()) // Initial fragment
        // Add On Click Listeners
        binding.fragmentBtn1.setOnClickListener {
            loadFragment(Fragment1())
        }
        binding.fragmentBtn2.setOnClickListener {
            loadFragment(Fragment2())
        }

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager // Responsible for fragment add, replace, remove, etc... with in a activity
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    // Life Cycle
    override fun onStart() {
        Log.i("Fragment Activity","Fragment Activity onStart.....-------------->")
        super.onStart()
    }

    override fun onRestart() {
        Log.i("Fragment Activity","Fragment Activity onRestart.....-------------->")
        super.onRestart()
    }

    override fun onResume() {
        Log.i("Fragment Activity","Fragment Activity onResume.....-------------->")
        super.onResume()
    }

    override fun onPause() {
        Log.i("Fragment Activity","Fragment Activity onPause.....-------------->")
        super.onPause()
    }

    override fun onStop() {
        Log.i("Fragment Activity","Fragment Activity onStop.....-------------->")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i("Fragment Activity","Fragment Activity onDestroy.....-------------->")
        super.onDestroy()
    }
}