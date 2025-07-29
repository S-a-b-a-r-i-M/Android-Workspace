package com.example.firstapplication.architectures.mvp2.data.model

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.example.firstapplication.architectures.mvp.data.database.DatabaseHelper
import com.example.firstapplication.architectures.mvp.data.database.UserTable
import com.example.firstapplication.architectures.mvp2.data.database.BookTable
import cutomutils.ErrorCode
import cutomutils.Result

class BookDaoImpl(private val dbHelper: DatabaseHelper) : BookDao {
    override fun insertBook(name: String, author: String): Result<Book> {
        val values = ContentValues().apply {
            put(UserTable.COLUMN_NAME, name)
            put(UserTable.COLUMN_EMAIL, author)
        }

        try {
            val id = dbHelper.writableDatabase.insert(UserTable.TABLE_NAME, null, values)
            return if (id != -1L)
                Result.Success(Book(id, name, author))
            else
                Result.Error("User Creation failed")
        } catch (exp: Exception) {
            Log.e("NoteRepoImpl", "Error on creating note")
            return Result.Error(exp.message.toString())
        }
    }

    override fun getAllBooks(): Result<List<Book>> {
        try {
            val books = mutableListOf<Book>()
            dbHelper.readableDatabase.query(
                BookTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
            ).use { cursor ->
                while (cursor.moveToNext()) {
                    books.add(parseBookFromCursor(cursor))
                }
            }
            return Result.Success(books)
        } catch (exp: Exception) {
            Log.e(TAG, "Error on reading note")
            return Result.Error(exp.message.toString())
        }
    }

    override fun getBookById(id: Long): Result<Book> {
        val whereClause = "${BookTable.COLUMN_ID} = ?"
        val whereArgs = arrayOf(id.toString())
        try {
            dbHelper.readableDatabase.query(
                BookTable.TABLE_NAME,
                null, // Get all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    val user: Book = parseBookFromCursor(cursor)
                    return Result.Success(user)
                }
                return Result.Error(message = "Id Not Found", ErrorCode.RESOURCE_NOT_FOUND)
            }
        } catch (exp: Exception) {
            Log.e(TAG, "Error on reading note")
            return Result.Error(exp.message.toString())
        }
    }

    private fun parseBookFromCursor(cursor: Cursor) =
        Book(
            cursor.getLong(
                cursor.getColumnIndexOrThrow(BookTable.COLUMN_ID)
            ),
            cursor.getString(
                cursor.getColumnIndexOrThrow(BookTable.COLUMN_NAME)
            ),
            cursor.getString(
                cursor.getColumnIndexOrThrow(BookTable.COLUMN_AUTHOR)
            ),
        )

    companion object {
        private const val TAG = "BookDaoImpl"
    }
}