package com.example.firstapplication.learn_retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    const val BASE_URL_JSP = "https://jsonplaceholder.typicode.com/"
    const val BASE_URL_GITHUB = "https://api.github.com/"

    private fun getRetrofitBuilderWithoutBaseUrl(): Retrofit.Builder {
        val interceptor = OkHttpClient().newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            )
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(interceptor)
    }

    fun getJSHApiService(): Retrofit {
        return getRetrofitBuilderWithoutBaseUrl()
            .baseUrl(BASE_URL_JSP)
            .build()
    }

    fun getUserApiService() : Retrofit {
        return getRetrofitBuilderWithoutBaseUrl()
            .baseUrl(BASE_URL_GITHUB)
            .build()
    }
}
