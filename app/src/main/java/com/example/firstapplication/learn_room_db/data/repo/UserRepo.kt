package com.example.firstapplication.learn_room_db.data.repo

import com.example.firstapplication.learn_room_db.data.dao.UserDao
import com.example.firstapplication.learn_room_db.data.entity.User
import javax.inject.Inject

class UserRepo @Inject constructor(private val userDao: UserDao) {

    suspend fun createUser(user: User): Long {
        return userDao.addUser(user)
    }

    suspend fun getAllUsers(): List<User> {
        return userDao.readAllUser()
    }

    suspend fun updateUser(user: User): Boolean {
        return userDao.updateUser(user) > 0
    }

    suspend fun updateUserName(userId: Long, firstName: String, lastName: String): Boolean {
        return userDao.updateUserName(userId, firstName, lastName) > 0
    }

    suspend fun deleteUser(user: User): Boolean {
        return userDao.deleteUser(user) > 0
    }

    suspend fun deleteUser(userId: Int): Boolean {
        return userDao.deleteUser(userId) > 0
    }
}