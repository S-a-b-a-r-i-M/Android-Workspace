package com.example.firstapplication.auth

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBaseHelper (context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_NAME = "PlayGround.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null)
            UserTable.createTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        println("onUpgrade")
    }
}

object UserTable {
    const val TABLE_NAME = "t_user"
    const val COLUMN_ID = "id"
    const val COLUMN_USERNAME = "user_name"
    const val COLUMN_EMAIL = "email"
    const val COLUMN_PASSWORD = "password"

    fun createTable(db: SQLiteDatabase) {
        val createQuery = """
                |CREATE TABLE $TABLE_NAME (
                |$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                |$COLUMN_USERNAME TEXT,
                |$COLUMN_EMAIL TEXT,
                |$COLUMN_PASSWORD TEXT)
            """.trimMargin()
        db.execSQL(createQuery)
    }
}

class UserDAO(private val dbHelper: DataBaseHelper) {
    fun createUser(userName: String, email: String, password: String): Long {
        val values = ContentValues().apply {
            put(UserTable.COLUMN_USERNAME, userName)
            put(UserTable.COLUMN_EMAIL, email)
            put(UserTable.COLUMN_PASSWORD, password)
        }

        val userId = dbHelper.writableDatabase.insert(UserTable.TABLE_NAME, null, values)
        return userId
    }

    fun getUserByEmail(email: String): Boolean {
        // Fields to Filter
        val columns = arrayOf(
            UserTable.COLUMN_ID,
            UserTable.COLUMN_USERNAME,
            UserTable.COLUMN_EMAIL,
            UserTable.COLUMN_PASSWORD
        )
        // Where Query
        val selection = "${UserTable.COLUMN_EMAIL} = ?"
        val selectionArgs = arrayOf(email)

        dbHelper.readableDatabase.query(
            UserTable.TABLE_NAME,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                Log.d("DataBaseHelper", "name: ${cursor.getString(1)}")
                return true
            }

            return false
        }
    }
}