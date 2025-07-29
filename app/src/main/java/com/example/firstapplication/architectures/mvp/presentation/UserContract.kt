package com.example.firstapplication.architectures.mvp.presentation

import com.example.firstapplication.architectures.mvp.data.model.User

/**
 * Contract defining the communication interface between View and Presenter
 * This interface establishes the communication protocol and ensures loose coupling
 */

interface UserContract {
    /**
     * View interface - defines what the UI layer must implement
     * The Presenter will call these methods to update the UI
     */
    interface View {
        /** Display user information in the UI */
        fun showUser(user: User)

        /** Show/hide loading indicator */
        fun showLoading(isLoading: Boolean)

        /** Display error message to the user */
        fun showError(message: String)

        /** Navigate to user profile edit screen */
        fun showSuccess(message: String)
    }

    /**
     * Presenter interface - defines the business logic operations
     * The View will call these methods to trigger business operations
     */
    interface Presenter {
        /** Attach view to presenter */
        fun attachView(view: View)

        /** Detach view from presenter to prevent memory leaks */
        fun detachView()

        /** Save new user */
        fun saveUser(name: String, email: String)

        /** Load user data from repository */
        fun loadUser(userId: Long)

        /**
         * Clean up resources and cancel ongoing operations
         * Should be called when the view is being destroyed
         */
        fun onDestroy()
    }
}