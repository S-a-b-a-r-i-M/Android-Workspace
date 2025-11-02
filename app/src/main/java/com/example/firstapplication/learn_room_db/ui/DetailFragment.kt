package com.example.firstapplication.learn_room_db.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.firstapplication.R
import com.example.firstapplication.databinding.FragmentDeatailBinding

class DetailFragment : Fragment(R.layout.fragment_deatail) {
    private var _binding: FragmentDeatailBinding? = null
    private val binding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDeatailBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}