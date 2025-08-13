package com.example.firstapplication.architectures.mvp2.presentation.contract

import com.example.firstapplication.architectures.mvp2.data.model.Book

interface SingleBookContract {

    interface View {
        fun showSelectedBook(book: Book)

        fun updateViewOnModify(book: Book)

        fun showError(message: String)
    }

    interface Presenter {
        fun onUpdateButtonClicked(modifiedBook: Book)
    }
}