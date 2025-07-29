package com.example.firstapplication.architectures.mvp.data.model

import cutomutils.Result

interface UserDao {
    /** Get user by ID from database */
    fun getUserById(userId: Long): Result<User>

    /** Insert new user into database */
    fun insertUser(name: String, email: String): Result<User>
}