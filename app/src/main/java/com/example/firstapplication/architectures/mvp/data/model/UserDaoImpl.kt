package com.example.firstapplication.architectures.mvp.data.model

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.example.firstapplication.architectures.mvp.data.database.DatabaseHelper
import com.example.firstapplication.architectures.mvp.data.database.UserTable
import com.example.firstapplication.notes.core.entity.Note
import com.example.firstapplication.notes.data.NoteTable
import cutomutils.ErrorCode
import cutomutils.Result

class UserDaoImpl(private val dbHelper: DatabaseHelper) : UserDao {
    override fun insertUser(name: String, email: String): Result<User> {
        val values = ContentValues().apply {
            put(UserTable.COLUMN_NAME, name)
            put(UserTable.COLUMN_EMAIL, email)
        }

        try {
            val id = dbHelper.writableDatabase.insert(UserTable.TABLE_NAME, null, values)
            return if (id != -1L)
                Result.Success(User(id, name, email))
            else
                Result.Error("User Creation failed")
        } catch (exp: Exception) {
            Log.e("NoteRepoImpl", "Error on creating note")
            return Result.Error(exp.message.toString())
        }
    }

    override fun getUserById(userId: Long): Result<User> {
        val whereClause = "${UserTable.COLUMN_ID} = ?"
        val whereArgs = arrayOf(userId.toString())
        try {
            dbHelper.readableDatabase.query(
                UserTable.TABLE_NAME,
                null, // Get all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    val user: User = parseUserFromCursor(cursor)
                    return Result.Success(user)
                }
                return Result.Error(message = "Id Not Found", ErrorCode.RESOURCE_NOT_FOUND)
            }
        } catch (exp: Exception) {
            Log.e("NoteRepoImpl", "Error on reading note")
            return Result.Error(exp.message.toString())
        }
    }

    private fun parseUserFromCursor(cursor: Cursor) =
        User(
            cursor.getLong(
                cursor.getColumnIndexOrThrow(UserTable.COLUMN_ID)
            ),
            cursor.getString(
                cursor.getColumnIndexOrThrow(UserTable.COLUMN_NAME)
            ),
            cursor.getString(
                cursor.getColumnIndexOrThrow(UserTable.COLUMN_EMAIL)
            ),
        )
}