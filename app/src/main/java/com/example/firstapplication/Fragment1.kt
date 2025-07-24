package com.example.firstapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Fragment1 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("Fragment1", "Fragment1 onCreateView Started")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_1, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Fragment1", "I Also have my own lifecycle and ill run before onCreateView")
    }

    // Life Cycle
    override fun onStart() {
        Log.i("Fragment1","Fragment1 onStart.....-------------->")
        super.onStart()
    }

//    override fun onRestart() {
//        Log.i("MainActivity","onRestart.....-------------->")
//        super.onRestart()
//    }

    override fun onResume() {
        Log.i("Fragment1","Fragment1 onResume.....-------------->")
        super.onResume()
    }

    override fun onPause() {
        Log.i("Fragment1","Fragment1 onPause.....-------------->")
        super.onPause()
    }

    override fun onStop() {
        Log.i("Fragment1","Fragment1 onStop.....-------------->")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i("Fragment1","Fragment1 onDestroy.....-------------->")
        super.onDestroy()
    }
}