package com.example.firstapplication.architectures.mvp2.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper private constructor(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

        companion object {
            private const val DATABASE_NAME = "MVP_DB"
            private const val DATABASE_VERSION = 1
            private var INSTANCE: DatabaseHelper? = null

            fun getInstance(context: Context): DatabaseHelper {
                return INSTANCE ?: synchronized(this) {
                    INSTANCE ?: DatabaseHelper(context).also { INSTANCE = it }
                }
            }
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(BookTable.CREATE_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { }
}