package com.example.firstapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class Fragment2 : Fragment(R.layout.fragment_2) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        Log.i("Fragment2", "Fragment2 onCreateView.....-------------->")
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Fragment2", "I Also have my own lifecycle and ill run before onCreateView")
    }

    // Life Cycle
    override fun onStart() {
        Log.i("Fragment2","Fragment2 onStart.....-------------->")
        super.onStart()
    }

//    override fun onRestart() {
//        Log.i("MainActivity","onRestart.....-------------->")
//        super.onRestart()
//    }

    override fun onResume() {
        Log.i("Fragment2","Fragment2 onResume.....-------------->")
        super.onResume()
    }

    override fun onPause() {
        Log.i("Fragment2","Fragment2 onPause.....-------------->")
        super.onPause()
    }

    override fun onStop() {
        Log.i("Fragment2","Fragment2 onStop.....-------------->")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i("Fragment2","Fragment2 onDestroy.....-------------->")
        super.onDestroy()
    }
}