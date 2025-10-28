package com.example.firstapplication.learn_retrofit.api_services

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalReq = chain.request()

        val token = tokenProvider()
        // If No Token Then Proceed
        if (token == null)
            return chain.proceed(originalReq)

        // Add Header
        val authenticatedReq = originalReq.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authenticatedReq)
    }
}