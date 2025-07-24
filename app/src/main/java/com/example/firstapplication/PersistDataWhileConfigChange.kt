package com.example.firstapplication

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.firstapplication.databinding.ActivityPersistDataWhileConfigChangeBinding
import com.example.firstapplication.viewmodels.MyViewModel

class PersistDataWhileConfigChange : AppCompatActivity() {
    lateinit var binding: ActivityPersistDataWhileConfigChangeBinding
    val COUNTER_KEY = "counter"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPersistDataWhileConfigChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var count = 0
        if (savedInstanceState != null) // Check for the instance state
            count = savedInstanceState.getInt(COUNTER_KEY, 0)

        val countTV: TextView = binding.countTV1
        countTV.text = count.toString()

        binding.incrementBtn1.setOnClickListener {
            countTV.text = (++count).toString()
        }

        binding.decrementBtn1.setOnClickListener {
            if (count > 0)
                countTV.text = (--count).toString()
        }

        // USING VIEW MODEL
        val viewModel = ViewModelProvider(this)[MyViewModel::class]
        binding.countTV2.text = viewModel.counter.toString()

        binding.incrementBtn2.setOnClickListener {
            binding.countTV2.text = viewModel.updateCounter(1).toString()
        }

        binding.decrementBtn2.setOnClickListener {
            binding.countTV2.text = viewModel.updateCounter(-1).toString()
        }

        // USING VIEW MODEL WITH LIVE DATA
        viewModel.counterLV.observe(this, Observer { count ->
            Log.i("PersistDataWhileConfigChange", "Observer for counterLV is invoked with count: $count")
            binding.countTV3.text = count?.toString() ?: "0"
        })

        viewModel.counterLV.observe(this, Observer { count ->
            Log.i("PersistDataWhileConfigChange", "2nd time Observer for counterLV is invoked with count: $count")
            binding.countTV3.text = count?.toString() ?: "0"
        })

        binding.incrementBtn3.setOnClickListener {
            viewModel.updateCounterLV(1)
        }

        binding.decrementBtn3.setOnClickListener {
            viewModel.updateCounterLV(-1)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(COUNTER_KEY, binding.countTV1.text.toString().toInt())
    }
}