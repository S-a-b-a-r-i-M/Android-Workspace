package com.example.firstapplication.newsapp.repo

import android.content.Context
import com.example.firstapplication.newsapp.api.RetrofitInstance
import com.example.firstapplication.newsapp.db.ArticleDatabase
import com.example.firstapplication.newsapp.models.Article

class NewsRepo(context: Context) {
    private val db = ArticleDatabase(context)

    // Get Headlines API
    suspend fun getHeadlines(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getHeadlines(countryCode = countryCode, pageNumber = pageNumber)

    // Search News
    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchNews(query = searchQuery, pageNumber = pageNumber)

    // Create Article(upsert)
    suspend fun addArticleToFavourites(article: Article) = db.getArticleDao().upsert(article)

    // Get Favourites
    suspend fun getFavourite() = db.getArticleDao().getAllArticles()

    // Delete Article
    suspend fun removeFromFavourites(articleId: Int) = db.getArticleDao().deleteArticle(articleId)
}