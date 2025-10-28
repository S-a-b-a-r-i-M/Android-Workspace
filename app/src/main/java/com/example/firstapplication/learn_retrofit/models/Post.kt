package com.example.firstapplication.learn_retrofit.models

data class Post (
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
)

data class NewPost (
    val userId: Int,
    val title: String,
    val body: String,
)
