package com.example.firstapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Fragment1 : Fragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("Fragment1", "<------ onAttach ----->")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Fragment1", "<------ onCreate ----->")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("Fragment1", "<------ onCreateView ----->")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Fragment1", "<------ onViewCreated ----->")
    }

    // Life Cycle
    override fun onStart() {
        Log.i("Fragment1","<------ onStart ----->")
        super.onStart()
    }

    override fun onResume() {
        Log.i("Fragment1","<------ onResume ----->")
        super.onResume()
    }

    override fun onPause() {
        Log.i("Fragment1","<------ onPause ----->")
        super.onPause()
    }

    override fun onStop() {
        Log.i("Fragment1","<------ onStop ----->")
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("Fragment1","<------ onDestroyView ----->")

    }

    override fun onDestroy() {
        Log.i("Fragment1","<------ onDestroy ----->")
        super.onDestroy()
    }
}