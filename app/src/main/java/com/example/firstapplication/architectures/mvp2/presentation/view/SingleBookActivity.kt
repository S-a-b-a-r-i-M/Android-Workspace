package com.example.firstapplication.architectures.mvp2.presentation.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.R
import com.example.firstapplication.architectures.mvp2.data.model.Book
import com.example.firstapplication.architectures.mvp2.presentation.contract.SingleBookContract

class SingleBookActivity : AppCompatActivity(), SingleBookContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_single_book)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun showSelectedBook(book: Book) {
        TODO("Not yet implemented")
    }

    override fun updateViewOnModify(book: Book) {
        TODO("Not yet implemented")
    }

    override fun showError(message: String) {
        TODO("Not yet implemented")
    }
}