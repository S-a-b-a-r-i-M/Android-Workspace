package com.example.firstapplication.architectures.mvp2.data.model

import cutomutils.Result

interface BookDao {
    fun getAllBooks(): Result<List<Book>>

    fun getBookById(id: Long): Result<Book>

    fun insertBook(name: String, author: String): Result<Book>
}