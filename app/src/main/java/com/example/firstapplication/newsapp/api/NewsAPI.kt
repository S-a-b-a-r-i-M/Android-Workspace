package com.example.firstapplication.newsapp.api

import com.example.firstapplication.newsapp.models.NewsResponse
import com.example.firstapplication.newsapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("apiKey")
        apiKey: String = Constants.API_KEY,
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("pageSize")
        pageSize: Int = Constants.PAGE_SIZE
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("apiKey")
        apiKey: String = Constants.API_KEY,
        @Query("q")
        query: String,
        @Query("page")
        pageNumber: Int = 1,
    ): Response<NewsResponse>
}