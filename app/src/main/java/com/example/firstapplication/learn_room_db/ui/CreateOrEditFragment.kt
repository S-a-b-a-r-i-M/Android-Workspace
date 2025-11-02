package com.example.firstapplication.learn_room_db.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.example.firstapplication.R
import com.example.firstapplication.databinding.FragmentCreateOrEditBinding
import com.example.firstapplication.learn_room_db.data.entity.User
import com.example.firstapplication.learn_room_db.ui.viewmodel.UserViewModel
import cutomutils.Result
import cutomutils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class CreateOrEditFragment : Fragment(R.layout.fragment_create_or_edit) {
    private var _binding: FragmentCreateOrEditBinding? = null
    private val binding
        get() = _binding!!
    private val _context
        get() = requireContext()

    private val viewModel: UserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateOrEditBinding.bind(view)

        setListeners()
        setObservers()
    }

    private fun getUserDetails(): User? {
        fun checkIsEmpty(editText: EditText) : Boolean {
            val value = editText.text.toString()
            if (value.trim().isEmpty()) {
                editText.error = "Can't be empty"
                return true
            }

            return false
        }

        with(binding) {
            if (checkIsEmpty(etFirstName) ||
                checkIsEmpty(etLastName) ||
                checkIsEmpty(etEmail))
                return null

            return User (
                firstName = etFirstName.text.toString(),
                lastName = etLastName.text.toString(),
                email = etEmail.text.toString()
            )
        }
    }

    private fun setListeners() {
        with(binding) {
            btnSubmit.setOnClickListener {
                val user = getUserDetails()
                if (user != null)
                    viewModel.createUser(user)
            }
        }
    }

    private fun setObservers() {
        viewModel.result.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Success<String> -> {
                    _context.showToast(it.data)
                    parentFragmentManager.popBackStack()
                }
                is Result.Error -> {
                    _context.showToast(it.message)
                }
                Result.Loading -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}