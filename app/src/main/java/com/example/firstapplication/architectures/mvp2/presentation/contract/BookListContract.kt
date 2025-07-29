package com.example.firstapplication.architectures.mvp2.presentation.contract

import com.example.firstapplication.architectures.mvp2.data.model.Book

interface BookListContract {
    interface View {
        fun showAllBooks(books: List<Book>)

        fun updateViewOnAdd(books: List<Book>)

        fun showError(message: String)

        fun navigateToSingleBookActivity(bookId: Long)
    }

    interface Presenter {
        fun onAddButtonClicked(bookName: String, authorName: String)

        fun onItemClicked(bookId: Long)
    }
}