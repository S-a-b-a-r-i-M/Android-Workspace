package com.example.firstapplication.learn_room_db.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstapplication.learn_room_db.data.entity.User
import com.example.firstapplication.learn_room_db.data.repo.UserRepo
import cutomutils.Result
import cutomutils.logDebug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val userRepo: UserRepo) : ViewModel() {
    private var _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private var _result = MutableLiveData<Result<String>>(null)
    val result: LiveData<Result<String>> = _result

    fun loadAllUsers() {
        _result.value = Result.Loading
        viewModelScope.launch(Dispatchers.IO) {
            _users.value = userRepo.getAllUsers()
            _result.value = Result.Success("Users fetched successfully.")
            logDebug("Users fetched from db.")
        }
    }

    fun createUser(user: User) {
        viewModelScope.launch {
            userRepo.createUser(user)
        }
    }
}