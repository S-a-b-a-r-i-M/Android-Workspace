package com.example.firstapplication.architectures.mvp2.presentation.presenter

import com.example.firstapplication.architectures.mvp2.data.model.Book
import com.example.firstapplication.architectures.mvp2.data.repo.BookRepository
import com.example.firstapplication.architectures.mvp2.presentation.contract.SingleBookContract

class SingleBookPresenter(
    private val view: SingleBookContract.View,
    private val bookRepo: BookRepository
) : SingleBookContract.Presenter {
    override fun onUpdateButtonClicked(modifiedBook: Book) {
        TODO("Not yet implemented")
    }
}