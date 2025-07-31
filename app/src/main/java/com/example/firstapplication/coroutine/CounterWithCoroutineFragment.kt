package com.example.firstapplication.coroutine

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.firstapplication.R
import com.example.firstapplication.databinding.FragmentCounterWithCoroutineBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CounterWithCoroutineFragment : Fragment() {
    private var _binding: FragmentCounterWithCoroutineBinding? = null
    private val binding
        get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "<----- onAttach ----->")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "<----- onCreateView ----->")
        return inflater.inflate(R.layout.fragment_counter_with_coroutine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCounterWithCoroutineBinding.bind(view)

        Log.i(TAG, "<----- onViewCreated ----->")
        binding.counterTV.text = "0"
        binding.downloadSpinner.visibility = View.INVISIBLE

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
                downloadSpinner.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.IO).launch {
                    Log.i(TAG, "download process running in ${Thread.currentThread().name}")
                    for (i in 0..1000000)
                        print("$i, ")
                    Log.i(TAG, "download process completed in ${Thread.currentThread().name}")
                }
                downloadSpinner.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.i(TAG,"<------ onDestroyView ----->")
    }

    override fun onDestroy() {
        Log.i(TAG,"<------ onDestroy ----->")
        super.onDestroy()
    }

    companion object {
        private const val TAG = "CounterWithCoroutineFragment"
    }

}