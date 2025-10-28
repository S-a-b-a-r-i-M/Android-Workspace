package com.example.firstapplication.learn_retrofit.models

import com.google.gson.annotations.SerializedName

data class UserItem (
    val login: String,
    val id: Int,
     @SerializedName("avatar_url")
    val avatarUrl: String,
)
