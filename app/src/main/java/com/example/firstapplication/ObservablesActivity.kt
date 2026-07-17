package com.example.firstapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.firstapplication.databinding.ActivityObservablesBinding
import com.example.firstapplication.viewmodels.ObservablesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ObservablesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityObservablesBinding
    private lateinit var viewModel: ObservablesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityObservablesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViewModel()
        setObservers()
        setListeners()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[ObservablesViewModel::class.java]
    }

    private fun setObservers() {
        viewModel.liveData.observe(this) {
            binding.tvLiveData.text = it
        }

        lifecycleScope.launch {
           // repeatOnLifecycle(Lifecycle.State.STARTED) {
                // What is the usage ?
          //  }

            launch {
                viewModel.stateFlow.collect {
                    binding.tvStateFlow.text = it
                }
            }

            launch {
                viewModel.sharedFlow.collect {
                    binding.tvSharedFlow.text = it
                }
            }
        }
    }

    private fun setListeners() {
        with(binding) {
            btnLiveData .setOnClickListener {
                viewModel.triggerLiveData()
            }

            btnFlow.setOnClickListener {
                lifecycleScope.launch {
                    // collect vs collectLatest
                    viewModel.triggerFlow().collectLatest {
                        tvFlow.text = it
                    }
                }
            }

            btnStateFlow.setOnClickListener {
                viewModel.triggerStateFlow()
            }

            btnSharedFlow.setOnClickListener {
                viewModel.triggerSharedFlow()
            }
        }
    }
}