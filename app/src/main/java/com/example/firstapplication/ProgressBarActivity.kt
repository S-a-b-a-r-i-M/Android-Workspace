package com.example.firstapplication

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.databinding.ActivityProgressBarBinding

class ProgressBarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProgressBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProgressBarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    // DETERMINATE PROGRESS
        setDeterminateProgress()
        setCustomDeterminateProgress()


    // INDETERMINATE PROGRESS
        setInDeterminateProgress()
        setCustomInDeterminateProgress()
    }

    private fun setDeterminateProgress() {
        with(binding) {
            progressBar.progress = 0
            increaseProgressBtn.setOnClickListener {
                progressBar.progress += 10
            }
        }
    }

    private fun setCustomDeterminateProgress() {
        with(binding) {
            customProgressBar.progress = 0
            increaseCustomProgressBtn.setOnClickListener {
                customProgressBar.progress += 10
            }
        }
    }

    private fun setInDeterminateProgress() {
        with(binding) {
            spinner.visibility = View.INVISIBLE
            toggleButton.setOnClickListener {
                when (toggleButton.text.toString().lowercase()) {
                    "off" -> spinner.visibility = View.INVISIBLE
                    else -> spinner.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setCustomInDeterminateProgress() {
        binding.customSpinner.progress = 0
        binding.customProgressBar.max = 100
        val countDownTimer = object : CountDownTimer(10*1000, 1000) {
            override fun onFinish() {}

            override fun onTick(millisUntilFinished: Long) {
                binding.customSpinner.progress += 10
            }
        }

        binding.startProgressBtn.setOnClickListener {
            countDownTimer.start()
        }
    }
}