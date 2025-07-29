package com.example.firstapplication.architectures.mvp2.presentation.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.R
import com.example.firstapplication.architectures.mvp2.data.model.Book
import com.example.firstapplication.architectures.mvp2.presentation.contract.BookListContract

class BookListActivity : AppCompatActivity(), BookListContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun showAllBooks(books: List<Book>) {
        TODO("Not yet implemented")
    }

    override fun updateViewOnAdd(books: List<Book>) {
        TODO("Not yet implemented")
    }

    override fun showError(message: String) {
        TODO("Not yet implemented")
    }

    override fun navigateToSingleBookActivity(bookId: Long) {
        TODO("Not yet implemented")
    }
}