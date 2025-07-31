package com.example.firstapplication.coroutine

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.firstapplication.R
import com.example.firstapplication.databinding.FragmentCounterWithoutCoroutineBinding
import cutomutils.customToast

class CounterWithoutCoroutineFragment : Fragment(R.layout.fragment_counter_without_coroutine) {
    private var _binding: FragmentCounterWithoutCoroutineBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var parentActivity: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Get parent activity reference
        parentActivity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_counter_without_coroutine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCounterWithoutCoroutineBinding.bind(view)

        binding.counterTV.text = "0"
        binding.downloadSpinner.visibility = View.GONE

        // SET ON CLICK LISTENERS
        setOnClickListeners()
    }

    fun setOnClickListeners() {
        binding.apply {
            counterBtn.setOnClickListener {
                val currentCount = counterTV.text.toString().toIntOrNull() ?: 0
                counterTV.text = (currentCount + 1).toString()
            }

            downloadBtn.setOnClickListener {
                customToast(parentActivity, "Your app is gonna crash or hang in sometime...")
                binding.downloadSpinner.visibility = View.VISIBLE
                Log.i(TAG, "download process running in ${Thread.currentThread().name}")
                for (i in 0..1000000)
                    print("$i, ")
                Log.i(TAG, "download process completed in ${Thread.currentThread().name}")
                binding.downloadSpinner.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.i(TAG,"<------ onDestroyView ----->")
    }

    companion object {
        private const val TAG = "CounterWithoutCoroutineFragment"
    }
}