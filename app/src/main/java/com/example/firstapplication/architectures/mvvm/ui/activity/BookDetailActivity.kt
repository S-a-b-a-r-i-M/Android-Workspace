package com.example.firstapplication.architectures.mvvm.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Snackbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.firstapplication.R
import com.example.firstapplication.architectures.mvvm.data.database.BookDatabaseHelper
import com.example.firstapplication.architectures.mvvm.data.model.BookDaoImpl
import com.example.firstapplication.architectures.mvvm.data.repo.BookRepositoryImpl
import com.example.firstapplication.architectures.mvvm.ui.viewmodel.BookViewModel
import com.example.firstapplication.architectures.mvvm.ui.viewmodel.BookViewModelFactory
import com.example.firstapplication.databinding.ActivityBookDetailBinding
import com.google.android.material.snackbar.Snackbar
import cutomutils.customToast
import cutomutils.printLogInfo

class BookDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailBinding
    private lateinit var bookViewModel: BookViewModel
    private var currentBookId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // INITIALIZE VIEW MODEL
        val bookDao = BookDaoImpl(BookDatabaseHelper.getInstance(this))
        val bookRepo = BookRepositoryImpl(bookDao)
        val factory = BookViewModelFactory(bookRepo)
        bookViewModel = ViewModelProvider(this, factory)[BookViewModel::class.java]

        observeViewModel()
        setupClickListeners()
        loadBookData()
    }

    private fun loadBookData() {
        currentBookId = intent.getLongExtra("book_id", -1L)

        if (currentBookId != -1L)
            bookViewModel.loadBook(currentBookId!!)
        else {
            customToast(this, "Current Book Id is not found or not valid.")
            finish()
        }
    }

    private fun setupClickListeners() {
        binding.deleteBtn.setOnClickListener {
            currentBookId?.let {
                showDeleteConfirmation(it)
            }
        }
    }

    private fun observeViewModel() {
        bookViewModel.currentBook.observe(this) { book ->
            printLogInfo(book.toString())
            if (book != null) {
                binding.bookNameTV.text = book.name
                binding.authorNameTV.text = book.author
            }
        }

        bookViewModel.success.observe(this) { message ->
            if (message != null) {
                customToast(this, message)
                bookViewModel.clearSuccess()
            }
        }

        bookViewModel.error.observe(this) { message ->
            if (message != null) {
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
                    .setAction("Back") {
                        finish()
                    }
                    .show()
                bookViewModel.clearError()
            }
        }
    }

    private fun showDeleteConfirmation(bookId: Long){
        AlertDialog.Builder(this)
            .setTitle("Delete Book!")
            .setMessage("Do you want to delete this book ?")
            .setPositiveButton("Delete") { _, _ ->
                 bookViewModel.deleteBook(bookId)
                 finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}