package com.example.firstapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ObservablesViewModel : ViewModel() {
    // LIVEDATA
    private val _liveData = MutableLiveData("Initial State")
    val liveData: LiveData<String> = _liveData

    fun triggerLiveData() {
        _liveData.value = "LiveData Triggered"
    }

    // FLOW
    fun triggerFlow() : Flow<String> {
        return flow {
            repeat(5) {
                emit("Flow Triggered : $it")
                delay(1000)
            }
        }
    }

    // STATE FLOW
    private val _stateFlow = MutableStateFlow("Initial State")
    val stateFlow = _stateFlow.asStateFlow()

    fun triggerStateFlow() {
        _stateFlow.value = "StateFlow Triggered"
    }

    // SHARED FLOW
    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun triggerSharedFlow() {
        // _sharedFlow.tryEmit("SharedFlow Triggered") // isn't working
        viewModelScope.launch {
            _sharedFlow.emit("SharedFlow Triggered")
        }
    }
}