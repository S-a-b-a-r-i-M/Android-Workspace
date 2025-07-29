package com.example.firstapplication.architectures.mvp.data.repo

import com.example.firstapplication.architectures.mvp.data.model.User
import cutomutils.Result

/**
 * Repository interface for data operations
 * Abstracts the data source and provides clean API for data operations
 */
interface UserRepository {
    fun saveUser(name: String, email: String, callback: (Result<User>) -> Unit)

    fun getUser(userId: Long, callback: (Result<User>) -> Unit)
}