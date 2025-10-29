package com.example.firstapplication.learn_room_db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.firstapplication.learn_room_db.data.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM t_user ORDER BY id ASC")
    suspend fun readAllUser(): List<User>
}