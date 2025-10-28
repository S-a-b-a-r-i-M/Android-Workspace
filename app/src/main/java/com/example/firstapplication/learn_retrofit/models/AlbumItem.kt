package com.example.firstapplication.learn_retrofit.models

import com.google.gson.annotations.SerializedName

data class AlbumItem(
    @SerializedName("id")
    val albumId: Int,
    val userId: Int,
    val title: String,
)