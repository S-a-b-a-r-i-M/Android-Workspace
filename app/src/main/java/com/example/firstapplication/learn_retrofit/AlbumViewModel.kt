package com.example.firstapplication.learn_retrofit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData

class AlbumViewModel : ViewModel() {
    private val albumApiService = RetrofitInstance.getRetrofitInstance().create(AlbumApiService::class.java)

    fun getAlbumsLiveData() = liveData {
        val response = albumApiService.getAlbums()
        emit(response)
    }
}