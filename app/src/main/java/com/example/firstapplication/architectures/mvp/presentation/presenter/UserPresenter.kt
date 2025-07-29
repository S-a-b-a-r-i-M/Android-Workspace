package com.example.firstapplication.architectures.mvp.presentation.presenter

import com.example.firstapplication.architectures.mvp.data.model.User
import com.example.firstapplication.architectures.mvp.data.repo.UserRepository
import com.example.firstapplication.architectures.mvp.presentation.UserContract
import cutomutils.Result

class UserPresenter(private val userRepo: UserRepository) : UserContract.Presenter {
    // Weak reference to view to prevent memory leaks
    private var view: UserContract.View? = null

    override fun attachView(view: UserContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    override fun saveUser(name: String, email: String) {
        view?.let {
            it.showLoading(true)

            // Save user through repository
            userRepo.saveUser(name, email) { result ->
                view?.showLoading(false)
                when (result) {
                    is Result.Success -> {
                        view?.showSuccess("User saved successfully")
                    }
                    is Result.Error -> {
                        view?.showError(result.message)
                    }
                    else -> {
                        view?.showError("Failed to save user")
                    }
                }
            }
        }
    }

    override fun loadUser(userId: Long) {
        view?.let {
            it.showLoading(true)

            // Fetch user data from repository
            userRepo.getUser(userId) { result ->
                when(result) {
                    is Result.Success<User> -> {
                        it.showUser(result.data)
                    }
                    is Result.Error -> {
                        it.showError(result.message)
                    }
                    else -> {
                        it.showError("Unknown error occurred")
                    }
                }
            }
            it.showLoading(false)
        }
    }

    override fun onDestroy() {
        // Clean up any ongoing operations
        // Clear references, etc.
        view = null
    }
}