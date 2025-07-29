package com.example.firstapplication.architectures.mvp2.data.repo

import com.example.firstapplication.architectures.mvp2.data.model.Book
import com.example.firstapplication.architectures.mvp2.data.model.BookDao
import cutomutils.Result

/**
 * Concrete implementation of UserRepository using SQLite database
 * Handles all data operations with proper error handling
 */
class BookRepositoryImpl(
    private val bookDao: BookDao,
    // All The Data Source Objects
) : BookRepository {
    override fun saveBook(name: String, author: String) = bookDao.insertBook(name, author)

    override fun getBookById(bookId: Long) = bookDao.getBookById(bookId)

    override fun getAllBooks() = bookDao.getAllBooks()
}