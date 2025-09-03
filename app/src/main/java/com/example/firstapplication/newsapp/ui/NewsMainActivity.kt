package com.example.firstapplication.newsapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.firstapplication.R
import com.example.firstapplication.databinding.ActivityNewsMainBinding
import com.example.firstapplication.newsapp.repo.NewsRepo
import com.example.firstapplication.newsapp.ui.fragments.FavouritesFragment
import com.example.firstapplication.newsapp.ui.fragments.HeadlinesFragment
import com.example.firstapplication.newsapp.ui.fragments.SearchFragment

// API KEY: 7cba23e9807e4b6393ec4483bb4fa57b

class NewsMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsMainBinding
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewsMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowInset()

        setupViewModel()
        setupUI()

        if (savedInstanceState == null)
            loadFragment(HeadlinesFragment())
    }

    private fun setWindowInset() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupViewModel() {
        val repo = NewsRepo(this)
        val factory = NewsViewModelProviderFactory(application, repo)
        viewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)
    }

    private fun setupUI() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.headlines_bnav -> {
                    loadFragment(HeadlinesFragment())
                }
                R.id.favourites_bnav -> {
                    loadFragment(FavouritesFragment())
                }
                R.id.search_bnav -> {
                    loadFragment(SearchFragment())
                }
            }

            return@setOnItemSelectedListener true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}