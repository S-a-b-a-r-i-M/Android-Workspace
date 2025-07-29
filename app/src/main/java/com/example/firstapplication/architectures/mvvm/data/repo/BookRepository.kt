package com.example.firstapplication.architectures.mvvm.data.repo

import com.example.firstapplication.architectures.mvvm.data.model.Book
import cutomutils.Result

/**
 * Repository interface for data operations
 * Abstracts the data source and provides clean API for data operations
 */
interface BookRepository {
    suspend fun saveBook(name: String, author: String): Result<Book>

    suspend fun getAllBooks(): Result<List<Book>>

    suspend fun getBook(bookId: Long): Result<Book>

    suspend fun deleteBook(bookId: Long): Result<Boolean>
}