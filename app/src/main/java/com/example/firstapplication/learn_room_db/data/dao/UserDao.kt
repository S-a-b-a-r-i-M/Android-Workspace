package com.example.firstapplication.learn_room_db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.firstapplication.learn_room_db.data.entity.User

@Dao
interface UserDao {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addUser(user: User): Long

    // READ
    @Query("SELECT * FROM t_user ORDER BY id ASC")
    suspend fun readAllUser(): List<User>

    // UPDATE
    @Update
    suspend fun updateUser(user: User): Int

    @Query("""
        UPDATE t_user 
        SET ${User.FIRST_NAME_COL} = :firstName, ${User.LAST_NAME_COL} = :lastName
        WHERE  ${User.ID_COL} = :userId
    """)
    suspend fun updateUserName(userId: Long, firstName: String, lastName: String): Int

    // DELETE
    @Delete
    suspend fun deleteUser(user: User): Int // Delete by entity

    @Query("DELETE FROM ${User.TABLE_NAME} WHERE ${User.ID_COL} = :userId")
    suspend fun deleteUser(userId: Int): Int
}