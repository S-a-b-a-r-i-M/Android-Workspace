package com.example.firstapplication.newsapp.util


sealed class Resource<out T> {
    class Success<T>(data: T): Resource<T>()
    class Error(message: String): Resource<Nothing>()
    object Loading : Resource<Nothing>()
}