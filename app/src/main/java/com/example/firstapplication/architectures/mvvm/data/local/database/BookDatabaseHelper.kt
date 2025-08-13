package com.example.firstapplication.architectures.mvvm.data.local.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BookDatabaseHelper private constructor(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

        companion object {
            private const val DATABASE_NAME = "mvvm.db"
            private const val DATABASE_VERSION = 1
            private var INSTANCE: BookDatabaseHelper? = null

            fun getInstance(context: Context): BookDatabaseHelper {
                return INSTANCE ?: synchronized(this) {
                    INSTANCE ?: BookDatabaseHelper(context).also { INSTANCE = it }
                }
            }
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(BookTable.CREATE_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//            db?.execSQL(BookTable.DROP_TABLE)
//            onCreate(db)
        }
}