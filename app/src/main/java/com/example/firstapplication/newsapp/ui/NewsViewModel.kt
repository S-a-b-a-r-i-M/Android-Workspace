package com.example.firstapplication.newsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.firstapplication.newsapp.models.Article
import com.example.firstapplication.newsapp.models.NewsResponse
import com.example.firstapplication.newsapp.repo.NewsRepo
import com.example.firstapplication.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import okio.IOException

class NewsViewModel(app: Application, val repo: NewsRepo) : AndroidViewModel(app) {
    // Headlines vars
    val headlinesDataState: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlineResponse: NewsResponse? = null
    var headlinePageNumber = 1

    // Search Vars
    val searchDataState: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchResponse: NewsResponse? = null
    var searchPageNumber = 1

    var oldSearchQuery: String? = null
    var newSearchQuery: String? = null

    // Currently Opened Article
    var currentArticle: Article? = null

    // getHeadlines
    fun getHeadlines(countryCode: String) {
        viewModelScope.launch {
            fetchHeadlinesFromInternet(countryCode)
        }
    }

    // searchNews
    fun searchNews(searchQuery: String) {
        oldSearchQuery = newSearchQuery
        newSearchQuery = searchQuery
        viewModelScope.launch {
            searchInternet(searchQuery)
        }
    }

    // handleHeadlineResponse
    fun handleHeadlineResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                headlinePageNumber++
                if (headlineResponse == null)
                    headlineResponse = newsResponse
                else
                    headlineResponse!!.articles.addAll(newsResponse.articles)

                return Resource.Success(headlineResponse!!)
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
    fun addToFavourites(article: Article) {
        viewModelScope.launch {
            repo.addArticleToFavourites(article)
        }
    }

    // getFavourites
    fun getFavourites()  = repo.getFavourites()

    // removeFromFavourites
    fun removeFromFavourites(articleId: Int) {
        viewModelScope.launch{
//            repo.removeFromFavourites(articleId)
        }
        Log.d(TAG, " article $articleId removed from Favourites")
    }

    // Check Internet Connection
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
    private suspend fun fetchHeadlinesFromInternet(countryCode: String) {
        headlinesDataState.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repo.getHeadlines(countryCode, headlinePageNumber)
                headlinesDataState.postValue(handleHeadlineResponse(response)) // handleHeadlineResponse
            } else
                headlinesDataState.postValue(Resource.Error("No internet."))

        } catch (exp: Exception) {
            Log.e("NewsViewModel", "headlinesInternet -> Error: ${exp.message.toString()}")
            when(exp) {
                is IOException ->
                    headlinesDataState.postValue(Resource.Error("Unable to connect."))
                else -> headlinesDataState.postValue(Resource.Error("No signal."))
            }
        }
    }

    // searchInternet
    private suspend fun searchInternet(searchQuery: String) {
        searchDataState.postValue(Resource.Loading)
        try {
            if (internetConnection(this.getApplication())) {
                val response = repo.searchNews(searchQuery, searchPageNumber)
                searchDataState.postValue(handleSearchNewsResponse(response)) // handleSearchResponse
            } else
                searchDataState.postValue(Resource.Error("No internet."))

        } catch (exp: Exception) {
            Log.e("NewsViewModel", "headlinesInternet -> Error: ${exp.message.toString()}")
            when(exp) {
                is IOException ->
                    searchDataState.postValue(Resource.Error("Unable to connect."))
                else -> searchDataState.postValue(Resource.Error("No signal."))
            }
        }
    }

    companion object {
        const val TAG = "NewsVieModel"
    }
}

class NewsViewModelProviderFactory(val app: Application, val repo: NewsRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java))
            return NewsViewModel(app, repo) as T

        throw IllegalArgumentException("Wrong ViewModel class provided")
    }
}