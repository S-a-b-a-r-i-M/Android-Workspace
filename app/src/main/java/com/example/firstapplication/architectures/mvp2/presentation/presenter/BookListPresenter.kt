package com.example.firstapplication.architectures.mvp2.presentation.presenter

import com.example.firstapplication.architectures.mvp2.data.repo.BookRepository
import com.example.firstapplication.architectures.mvp2.presentation.contract.BookListContract

class BookListPresenter(
    private val view: BookListContract.View,
    private val bookRepo: BookRepository
) : BookListContract.Presenter {
    override fun onAddButtonClicked(bookName: String, authorName: String) {
        TODO("Not yet implemented")
    }

    override fun onItemClicked(bookId: Long) {
        TODO("Not yet implemented")
    }
}