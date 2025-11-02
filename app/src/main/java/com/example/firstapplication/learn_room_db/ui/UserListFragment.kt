package com.example.firstapplication.learn_room_db.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstapplication.R
import com.example.firstapplication.databinding.FragmentListBinding
import com.example.firstapplication.learn_room_db.ui.adapter.UserListAdapter
import com.example.firstapplication.learn_room_db.ui.viewmodel.UserViewModel
import cutomutils.Result
import cutomutils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class UserListFragment : Fragment(R.layout.fragment_list) {
    private var _binding: FragmentListBinding? = null
    private lateinit var adapter: UserListAdapter

    private val binding
        get() = _binding!!

    private val _context
        get() = requireContext()

    private val viewModel: UserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentListBinding.bind(view)

        setupUI()
        setListeners()
        setObservers()

        if (savedInstanceState == null) {
            viewModel.loadAllUsers()
        }
    }

    private fun setupUI() {
        // Setup Recycler View
        adapter = UserListAdapter()
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(_context)
            adapter = this@UserListFragment.adapter
        }
    }

    private fun setListeners() {
        // Note: Directly Using Parent Activity Is Not Good, But Here It's Exceptional
        binding.fbtnCreate.setOnClickListener {
            (requireActivity() as RoomLearningActivity).loadFragment(
                CreateOrEditFragment(), true
            )
        }
    }

    private fun setObservers() {
        viewModel.result.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Success<*> -> {
                    _context.showToast(it.data.toString())
                }
                is Result.Error -> {
                    _context.showToast(it.message)
                }
                Result.Loading -> {}
            }
        }

        viewModel.users.observe(viewLifecycleOwner) { users ->
            adapter.setUserList(users)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}