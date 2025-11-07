package com.example.firstapplication.learn_room_db.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstapplication.learn_room_db.data.entity.User
import com.example.firstapplication.learn_room_db.data.repo.UserRepo
import cutomutils.Result
import cutomutils.logDebug
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepo: UserRepo) : ViewModel() {
    private var _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private var _result = MutableLiveData<Result<String>>()
    val result: LiveData<Result<String>> = _result

    fun loadAllUsers() {
        _result.value = Result.Loading
        viewModelScope.launch {
            _users.value = userRepo.getAllUsers()
            _result.value = Result.Success("Users fetched successfully.")
            logDebug("Users fetched from db.")
        }
    }

    fun createUser(user: User) {
        _result.value = Result.Loading
        viewModelScope.launch {
            val id = userRepo.createUser(user)
            _result.value = Result.Success("User created successfully.")
        }
    }

    fun updateUser(user: User) {
        _result.value = Result.Loading
        viewModelScope.launch {
            if (userRepo.updateUser(user))
                _result.value = Result.Success("User updated.")
            else
                _result.value = Result.Error("User update failed.")
        }
    }

    fun deleteUser(userId: Int) {
        _result.value = Result.Loading
        viewModelScope.launch {
            if (userRepo.deleteUser(userId))
                _result.value = Result.Success("User deleted successfully.")
            else
                _result.value = Result.Error("User deletion failed.")
        }
    }
}