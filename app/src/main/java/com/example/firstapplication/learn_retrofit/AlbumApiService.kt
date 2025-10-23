package com.example.firstapplication.learn_retrofit

import retrofit2.Response
import retrofit2.http.GET

interface AlbumApiService {
    @GET("/albums")
    suspend fun getAlbums(): Response<List<AlbumItem>>
}