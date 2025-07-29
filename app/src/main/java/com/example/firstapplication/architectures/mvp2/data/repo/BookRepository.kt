package com.example.firstapplication.architectures.mvp2.data.repo

import com.example.firstapplication.architectures.mvp.data.model.User
import com.example.firstapplication.architectures.mvp2.data.model.Book
import cutomutils.Result

/**
 * Repository interface for data operations
 * Abstracts the data source and provides clean API for data operations
 */
interface BookRepository {
    fun saveBook(name: String, author: String): Result<Book>

    fun getBookById(bookId: Long): Result<Book>

    fun getAllBooks(): Result<List<Book>>
}