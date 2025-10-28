package com.example.firstapplication.learn_retrofit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.firstapplication.learn_retrofit.api_services.JsonPlaceHolderApiService
import com.example.firstapplication.learn_retrofit.api_services.UserApiService
import com.example.firstapplication.learn_retrofit.models.NewPost
import cutomutils.logDebug
import kotlinx.coroutines.launch

class RetrofitPracticeViewModel : ViewModel() {
    private val jsphApiService = RetrofitInstance.getJSHApiService().create(JsonPlaceHolderApiService::class.java)
    private val userApiService = RetrofitInstance.getUserApiService().create(UserApiService::class.java)

    fun getAlbumsLiveData() = liveData {
        val response = jsphApiService.getAlbums()
        emit(response)
    }

    fun loadUsers() {
        viewModelScope.launch {
            val users = userApiService.getUsers(10, 5)
            logDebug(users.toString())
        }
    }

    fun getUsersLiveData() = liveData {
        val users = userApiService.getUsers(11, 1)
        emit(users)
    }

    @JvmOverloads
    fun loadUserByName(userName: String = "defunkt") {
        viewModelScope.launch {
            val user = userApiService.getUser(userName)
            logDebug("user with name: $userName is $user")
        }
    }

    @JvmOverloads
    fun getPostById(postId: Int = 11) = liveData {
        val post = jsphApiService.getPostById(postId)
        emit(post)
    }

    fun createPost(newPost: NewPost) = liveData {
        val post = jsphApiService.createPost(newPost)
        logDebug("< ---- New Post Create Response ---- > $post")
        emit(post)
    }
}