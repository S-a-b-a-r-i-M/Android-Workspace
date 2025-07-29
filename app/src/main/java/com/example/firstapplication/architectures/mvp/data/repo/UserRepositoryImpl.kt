package com.example.firstapplication.architectures.mvp.data.repo

import com.example.firstapplication.architectures.mvp.data.model.User
import com.example.firstapplication.architectures.mvp.data.model.UserDao
import cutomutils.Result

/**
 * Concrete implementation of UserRepository using SQLite database
 * Handles all data operations with proper error handling
 */
class UserRepositoryImpl(
    private val userDao: UserDao,
    // All The Data Source Objects
) : UserRepository {
    override fun saveUser(name: String, email: String, callback: (Result<User>) -> Unit) {
        val result = userDao.insertUser(name, email)
        callback(result)
    }

    override fun getUser(userId: Long, callback: (Result<User>) -> Unit) {
        val result = userDao.getUserById(userId)
        callback(result)
    }
}