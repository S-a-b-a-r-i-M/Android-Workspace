package com.example.firstapplication.learn_jetpack_compose.performancekillers.killer5

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val repo = FileRepo()

    private val _userData = MutableStateFlow("")
    val userData = _userData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadUserDataBad(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            _userData.value = repo.loadUserDataBad(userId)

            _isLoading.value = false
        }
    }

    fun loadUserDataGood(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            _userData.value = repo.loadUserDataGood(userId)

            _isLoading.value = false
        }
    }
}