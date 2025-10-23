package com.example.firstapplication.learn_retrofit

import com.google.gson.annotations.SerializedName

/*
    1. @SerializedName("key") - mapping between json to object and vice versa.
 */

data class AlbumItem(
    @SerializedName("id")
    val albumId: Int,
    val userId: Int,
    val title: String,
)
