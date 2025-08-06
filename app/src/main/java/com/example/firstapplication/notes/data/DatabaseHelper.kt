package com.example.firstapplication.notes.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper private constructor(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "NotesDB"
        private const val DATABASE_VERSION = 2 // Added IsCompleted column
        private var INSTANCE: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DatabaseHelper(context).also { INSTANCE = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i("DatabaseHelper", "<----------------- onCreate ----------------->")
        db?.execSQL(NoteTable.createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i("DatabaseHelper", "<----------------- onUpgrade ----------------->")
        when(oldVersion) {
            1 -> // Migrate from version 1 to 2: Add is_completed column
                db?.execSQL("ALTER TABLE ${NoteTable.TABLE_NAME} ADD COLUMN ${NoteTable.COLUMN_IS_COMPLETED} INTEGER DEFAULT 0")
        }
    }
}