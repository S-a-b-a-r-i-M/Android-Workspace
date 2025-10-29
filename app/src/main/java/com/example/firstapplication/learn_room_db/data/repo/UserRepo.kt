package com.example.firstapplication.learn_room_db.data.repo

import com.example.firstapplication.learn_room_db.data.dao.UserDao
import com.example.firstapplication.learn_room_db.data.entity.User

class UserRepo(private val userDao: UserDao) {

    suspend fun createUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun getAllUsers(): List<User> {
        return userDao.readAllUser()
    }

}