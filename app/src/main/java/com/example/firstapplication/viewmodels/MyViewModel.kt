package com.example.firstapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    var counter = 0
        private set

    fun updateCounter(value: Int): Int {
        counter += value
        return counter
    }

    // WITH LIVE DATA
    var _counterLV = MutableLiveData(0)
    val counterLV: LiveData<Int> = _counterLV

    fun updateCounterLV(value: Int) {
        _counterLV.value = (counterLV.value ?: 0) + value
    }
}