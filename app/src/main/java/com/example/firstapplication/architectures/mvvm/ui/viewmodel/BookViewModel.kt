package com.example.firstapplication.architectures.mvvm.ui.viewmodel

import androidx.compose.ui.input.pointer.PointerId
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.firstapplication.architectures.mvvm.data.model.Book
import com.example.firstapplication.architectures.mvvm.data.repo.BookRepository
import cutomutils.Result
import kotlinx.coroutines.launch

/**
 * ViewModel for managing Book-related UI data and business logic
 * This is the key component that differentiates MVVM from MVP
 * - Survives configuration changes (screen rotation)
 * - Uses LiveData/StateFlow for reactive UI updates
 * - Contains business logic and manages UI-related data
 */
class BookViewModel(private val bookRepo: BookRepository) : ViewModel() {
    // Private MutableLiveData for internal updates
    private val _books = MutableLiveData<List<Book>>()
    private val _currentBook = MutableLiveData<Book?>()
    private val _loading = MutableLiveData<Boolean>()
    private val _success = MutableLiveData<String?>()
    private val _error = MutableLiveData<String?>()

    // Public LiveData for UI observation
    val books: LiveData<List<Book>> = _books
    val currentBook: LiveData<Book?> = _currentBook
    val loading: LiveData<Boolean> = _loading
    val success: LiveData<String?> = _success
    val error: LiveData<String?> = _error

    fun loadAllBooks() {
        viewModelScope.launch {
            _loading.value = true

            when (val result = bookRepo.getAllBooks()) {
                is Result.Success<List<Book>> -> {
                    _books.value = result.data
                    _success.value = "Books are loaded successfully"
                }
                is Result.Error -> {
                    _error.value = result.message
                }
                else -> {
                    _error.value = "Unknown error occurred"
                }
            }

            _loading.value = false
        }
    }

    fun loadBook(bookId: Long) {
        viewModelScope.launch {
            _loading.value = true

            when (val result = bookRepo.getBook(bookId)) {
                is Result.Success<Book> -> {
                    _currentBook.value = result.data
                }
                is Result.Error -> {
                    _error.value = result.message
                    _currentBook.value = null
                }
                else -> {
                    _error.value = "Unknown error occurred"
                    _currentBook.value = null
                }
            }

            _loading.value = false
        }
    }

    fun createBook(bookName: String, authorName: String) {
        if (bookName.isBlank()) {
            _error.value = "BookName cannot be empty"
            return
        }

        if (authorName.isBlank()) {
            _error.value = "AuthorName cannot be empty"
            return
        }

        viewModelScope.launch {
            _loading.value = true

            when (val result = bookRepo.saveBook(bookName, authorName)) {
                is Result.Success<Book> -> {
                    _success.value = "Book Created Successfully"
                    // Refresh the book list
                    // loadAllBooks()
                }
                is Result.Error -> {
                    _error.value = result.message
                }
                else -> {
                    _error.value ="Failed to create book.Something wrong!"
                }
            }

            _loading.value = false
        }
    }

    fun deleteBook(bookId: Long) {
        viewModelScope.launch {
            _loading.value = true

            when (val result = bookRepo.deleteBook(bookId)) {
                is Result.Success -> {
                    _success.value = "Book deleted successfully"
                    // Refresh the books list
                    loadAllBooks()
                }
                is Result.Error -> {
                    _error.value = result.message
                }
                else -> {
                    _error.value = "Failed to delete book"
                }
            }

            _loading.value = false
        }
    }

    /**
     * Clear error message (usually called after showing error to user)
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Clear success message (usually called after showing success to user)
     */
    fun clearSuccess() {
        _success.value = null
    }

    companion object {
        const val TAG = "BookViewModel"
    }
}

/**
 * ViewModelFactory to create BookViewModel with dependencies
 * Required because ViewModel constructor needs parameters
 */
class BookViewModelFactory(private val bookRepo: BookRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java))
            return BookViewModel(bookRepo) as T

        throw IllegalArgumentException("Unknown ViewClass Model")
    }
}