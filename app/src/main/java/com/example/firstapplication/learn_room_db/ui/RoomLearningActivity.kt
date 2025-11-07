package com.example.firstapplication.learn_room_db.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.firstapplication.R
import com.example.firstapplication.databinding.ActivityRoomLearningBinding
import com.example.firstapplication.learn_room_db.ui.viewmodel.UserViewModel
import cutomutils.addFragment
import cutomutils.loadFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomLearningActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoomLearningBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomLearningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        setWindowInsets()

        // Loading Initial Fragment
        loadFragment(UserListFragment(), binding.fragmentContainer.id)
    }

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, imeInsets.bottom)
            insets
        }
    }

    fun loadFragment(fragment: Fragment, pushToBackStack: Boolean = false) {
        loadFragment(fragment, binding.fragmentContainer.id, pushToBackStack)
    }

    fun addFragment(fragment: Fragment) {
        this.addFragment(fragment, binding.fragmentContainer.id)
    }
}