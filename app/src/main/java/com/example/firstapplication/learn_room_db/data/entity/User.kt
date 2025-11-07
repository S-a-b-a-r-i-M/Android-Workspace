package com.example.firstapplication.learn_room_db.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

enum class UserRole(val readable: String) {
    TUTOR("Tutor"),
    STUDENT("Student")
}

@Parcelize
@Entity(tableName = User.TABLE_NAME)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String, // Has to be unique
) : Parcelable {
    companion object {
        const val TABLE_NAME = "t_user"
        const val ID_COL = "id"
        const val FIRST_NAME_COL = "firstName"
        const val LAST_NAME_COL = "lastName"
        const val EMAIL_COL = "email"
    }
}
