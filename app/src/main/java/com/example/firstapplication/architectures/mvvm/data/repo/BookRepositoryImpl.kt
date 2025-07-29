package com.example.firstapplication.architectures.mvvm.data.repo

import com.example.firstapplication.architectures.mvvm.data.model.BookDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of UserRepository using SQLite database
 * Handles all data operations with proper error handling
 */
class BookRepositoryImpl(
    private val bookDao: BookDao,
    // All The Data Source Objects
) : BookRepository {
    override suspend fun saveBook(name: String, author: String) = withContext(Dispatchers.IO) {
        bookDao.insertBook(name, author)
    }

    override suspend fun getBook(bookId: Long) = withContext(Dispatchers.IO) {
        bookDao.getBookById(bookId)
    }

    override suspend fun getAllBooks() = withContext(Dispatchers.IO) {
        bookDao.getAllBooks()
    }

    override suspend fun deleteBook(bookId: Long) = withContext(Dispatchers.IO) {
        bookDao.deleteBook(bookId)
    }
}