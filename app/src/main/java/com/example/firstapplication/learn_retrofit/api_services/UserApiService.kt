package com.example.firstapplication.learn_retrofit.api_services

import com.example.firstapplication.learn_retrofit.models.UserItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ) : List<UserItem>

    @GET("users/{username}")
    suspend fun getUser(@Path("username") userName: String) : UserItem
}