package com.example.firstapplication.learn_retrofit.api_services

import com.example.firstapplication.learn_retrofit.models.AlbumItem
import com.example.firstapplication.learn_retrofit.models.NewPost
import com.example.firstapplication.learn_retrofit.models.Post
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JsonPlaceHolderApiService {
    @GET("/albums")
    suspend fun getAlbums(): Response<List<AlbumItem>>

    @GET("/posts/{postId}")
    suspend fun getPostById(@Path("postId") postId: Int): Post

    @POST("/posts")
    suspend fun createPost(@Body newPost: NewPost): Post
}