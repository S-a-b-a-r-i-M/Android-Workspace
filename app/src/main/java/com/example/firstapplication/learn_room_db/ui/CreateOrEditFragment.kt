package com.example.firstapplication.learn_room_db.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.firstapplication.R
import com.example.firstapplication.databinding.FragmentCreateOrEditBinding
import com.example.firstapplication.learn_room_db.data.entity.Address
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
    private var userToEdit: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userToEdit = arguments?.getParcelable<User>("user")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateOrEditBinding.bind(view)

        setupUI()
        setListeners()
        setObservers()
    }

    private fun setupUI() {
        with(binding) {
            userToEdit?.let {
                tvTitle.text = getString(R.string.update)
                etFirstName.setText(it.firstName)
                etLastName.setText(it.lastName)
                etEmail.setText(it.email)
                etStreet.setText(it.address.street)
                etCity.setText(it.address.city)
                etPincode.setText(it.address.pincode.toString())

                ibtnDelete.visibility = View.VISIBLE
            } ?: run {
                tvTitle.text = getString(R.string.create)
            }
        }
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
                checkIsEmpty(etEmail) ||
                checkIsEmpty(etCity) ||
                checkIsEmpty(etPincode))
                return null

            val streetValue = etStreet.text.toString()
            val address = Address(
                street = if (streetValue.isBlank()) null else streetValue,
                city = etCity.text.toString(),
                pincode = etPincode.text.toString().toInt()
            )
            return User (
                firstName = etFirstName.text.toString(),
                lastName = etLastName.text.toString(),
                email = etEmail.text.toString(),
                address = address
            )
        }
    }

    private fun setListeners() {
        with(binding) {
            btnSubmit.setOnClickListener {
                val _userToEdit = userToEdit
                val user = getUserDetails() ?: return@setOnClickListener

                if (_userToEdit != null) {
                    // Add User Id
                    viewModel.updateUser(user.copy(id = _userToEdit.id))
                }
                else {
                    Glide.with(_context)
                        .load("https://via.assets.so/img.jpg?w=400&h=400&shape=diamond&pattern=grid&bg=e5e7eb&f=png")
                        .into(binding.imageView)
                    val imageBitmap = binding.imageView.drawToBitmap()
                    viewModel.createUser(user.copy(profileImage = imageBitmap))
                }
            }

            userToEdit?.let { user ->
                ibtnDelete.setOnClickListener {
                    AlertDialog.Builder(_context)
                    .setTitle("Delete ${user.firstName} ${user.lastName}")
                    .setMessage("Are you sure ?")
                    .setPositiveButton("Delete") { _, _ ->
                        viewModel.deleteUser(user.id)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
                }
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