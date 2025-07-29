package com.example.firstapplication.architectures.mvvm.data.model

import cutomutils.Result

interface BookDao {
    fun getAllBooks(): Result<List<Book>>

    fun getBookById(bookId: Long): Result<Book>

    fun insertBook(name: String, author: String): Result<Book>

    fun deleteBook(bookId: Long): Result<Boolean>
}