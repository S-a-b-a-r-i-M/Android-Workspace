package com.example.firstapplication.newsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.firstapplication.newsapp.models.NewsResponse
import com.example.firstapplication.newsapp.repo.NewsRepo
import com.example.firstapplication.newsapp.util.Resource
import retrofit2.Response
import okio.IOException

class NewsViewModel(app: Application,val repo: NewsRepo) : AndroidViewModel(app) {
    // Headlines vars
    val headlinesLD: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headLineResponse: NewsResponse? = null
    var headlinePageNumber = 1

    // Search Vars
    val searchesLD: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchResponse: NewsResponse? = null
    var searchPageNumber = 1

    var oldSearchQuery: String? = null
    var newSearchQuery: String? = null

    // getHeadlines
    fun getHeadlines(countryCode: String) {

    }

    // searchNews
    fun searchNews(searchQuery: String) {

    }

    // handleHeadlineResponse
    fun handleHeadlineResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                headlinePageNumber++
                if (headLineResponse == null)
                    headLineResponse = newsResponse
                else
                    headLineResponse!!.articles.addAll(newsResponse.articles)

                return Resource.Success(headLineResponse!!)
            }

            return Resource.Error("No data found")
        }
        else
            return Resource.Error("response is failed. ${response.message()}")
    }

    // handleSearchNewsResponse
    fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                if (searchResponse == null || oldSearchQuery != newSearchQuery){
                    searchPageNumber = 1
                    searchResponse = newsResponse
                }
                else {
                    searchPageNumber++
                    searchResponse!!.articles.addAll(newsResponse.articles)
                }

                return Resource.Success(searchResponse!!)
            }

            return Resource.Error("No data found")
        }
        else
            return Resource.Error("response is failed. ${response.message()}")
    }

    // addToFavourites

    // getFavourites

    // removeFromFavourites

    // internetConnection
    fun internetConnection(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).let {
            return it.getNetworkCapabilities(it.activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } ?: false
        }
    }

    // headlinesInternet
    private suspend fun headlinesInternet(countryCode: String) {
        headlinesLD.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repo.getHeadlines(countryCode, headlinePageNumber)
                headlinesLD.postValue(handleHeadlineResponse(response)) // handleHeadlineResponse
            } else
                headlinesLD.postValue(Resource.Error("No internet."))

        } catch (exp: Exception) {
            Log.e("NewsViewModel", "headlinesInternet -> Error: ${exp.message.toString()}")
            when(exp) {
                is IOException ->
                    headlinesLD.postValue(Resource.Error("Unable to connect."))
                else -> headlinesLD.postValue(Resource.Error("No signal."))
            }
        }
    }

    // searchInternet
    private suspend fun searchInternet(searchQuery: String) {
        searchesLD.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                oldSearchQuery = newSearchQuery
                newSearchQuery = searchQuery
                val response = repo.searchNews(searchQuery, searchPageNumber)
                searchesLD.postValue(handleSearchNewsResponse(response)) // handleSearchResponse
            } else
                searchesLD.postValue(Resource.Error("No internet."))

        } catch (exp: Exception) {
            Log.e("NewsViewModel", "headlinesInternet -> Error: ${exp.message.toString()}")
            when(exp) {
                is IOException ->
                    searchesLD.postValue(Resource.Error("Unable to connect."))
                else -> searchesLD.postValue(Resource.Error("No signal."))
            }
        }
    }
}