package com.example.firstapplication.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val urlToImage: String,
    val source: Source,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val url: String,
): Serializable