package com.example.firstapplication.lean_ui_automation

import android.content.Intent
import android.os.Bundle
import android.os.Message
import androidx.activity.enableEdgeToEdge
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.test.espresso.IdlingResource
import com.example.firstapplication.R
import com.example.firstapplication.databinding.ActivityUiAutomationFirstBinding

class UiAutomationFirstActivity : AppCompatActivity(), MessageDelayer.DelayerCallBack {
    private lateinit var binding: ActivityUiAutomationFirstBinding
    private var mIdlingResource: CustomIdlingResource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUiAutomationFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupClickListeners()
    }

    fun setupClickListeners() {
        with(binding) {
            btnChangeText.setOnClickListener {
                val newText = etInput.text.toString()
                tvToBeChanged.text = newText
            }

            btnOpenNextPage.setOnClickListener {
                val intent = Intent(this@UiAutomationFirstActivity, AutomationSecondActivity::class.java)
                intent.putExtra("Text", etInput.text.toString())
                startActivity(intent)
            }

            btnChangeText2.setOnClickListener {
                tvToBeChanged2.text = "Waiting..."
                // Trigger Loading
                val newText = etInput2.text.toString()
                val messageDelayer = MessageDelayer()
                messageDelayer.processMessage(
                    newText, this@UiAutomationFirstActivity, mIdlingResource
                )
            }
        }
    }

    override fun onDone(message: String) {
        binding.tvToBeChanged2.text = message
    }

    @VisibleForTesting
    fun getIdlingResource() : IdlingResource {
        return mIdlingResource ?: CustomIdlingResource().also { mIdlingResource = it }
    }
}