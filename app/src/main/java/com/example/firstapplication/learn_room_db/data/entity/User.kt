package com.example.firstapplication.learn_room_db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole(val readable: String) {
    TUTOR("Tutor"),
    STUDENT("Student")
}

@Entity(tableName = "t_user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String, // Has to be unique
)
