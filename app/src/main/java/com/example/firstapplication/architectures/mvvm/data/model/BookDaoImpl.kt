package com.example.firstapplication.architectures.mvvm.data.model

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.example.firstapplication.architectures.mvvm.data.local.database.BookDatabaseHelper
import com.example.firstapplication.architectures.mvvm.data.local.database.BookTable
import cutomutils.ErrorCode
import cutomutils.Result

class BookDaoImpl(private val dbHelper: BookDatabaseHelper) : BookDao {
    override fun insertBook(name: String, author: String): Result<Book> {
        val values = ContentValues().apply {
            put(BookTable.COLUMN_NAME, name)
            put(BookTable.COLUMN_AUTHOR, author)
        }

        try {
            val id = dbHelper.writableDatabase.insert(BookTable.TABLE_NAME, null, values)
            return if (id != -1L)
                Result.Success(Book(id, name, author))
            else
                Result.Error("User Creation failed")
        } catch (exp: Exception) {
            Log.e(TAG, "Error on creating book")
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
            Log.e(TAG, "Error on reading book")
            return Result.Error(exp.message.toString())
        }
    }

    override fun getBookById(bookId: Long): Result<Book> {
        val whereClause = "${BookTable.COLUMN_ID} = ?"
        val whereArgs = arrayOf(bookId.toString())
        return try {
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
                Result.Error(message = "Id Not Found", ErrorCode.RESOURCE_NOT_FOUND)
            }
        } catch (exp: Exception) {
            Log.e(TAG, "Error on reading book", exp)
            Result.Error(exp.message.toString())
        }
    }

    override fun deleteBook(bookId: Long): Result<Boolean> {
        val whereClause = "${BookTable.COLUMN_ID} = ?"
        val whereArgs = arrayOf(bookId.toString())
        return try {
            val rows = dbHelper.writableDatabase.delete(
                BookTable.TABLE_NAME,
                whereClause,
                whereArgs
            )

            if (rows > 0)
                Result.Success(true)
            else
                Result.Success(false, "No book record deleted by id: $bookId")
        } catch (exp: Exception) {
            Log.e(TAG, "Error on delete book($bookId)")
            Result.Error(exp.message.toString())
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