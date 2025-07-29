package com.example.firstapplication.architectures.mvvm.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateDecay
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.firstapplication.R
import com.example.firstapplication.architectures.mvvm.data.database.BookDatabaseHelper
import com.example.firstapplication.architectures.mvvm.data.model.BookDaoImpl
import com.example.firstapplication.architectures.mvvm.data.repo.BookRepositoryImpl
import com.example.firstapplication.architectures.mvvm.ui.viewmodel.BookViewModel
import com.example.firstapplication.architectures.mvvm.ui.viewmodel.BookViewModelFactory
import com.example.firstapplication.databinding.ActivityAddBookBinding
import com.google.android.material.snackbar.Snackbar
import cutomutils.customToast
import cutomutils.printLogInfo

class AddBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBookBinding
    private lateinit var bookViewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBookBinding.inflate(layoutInflater)
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

        // SETUP UI
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Add User"
        }

        observeViewModel()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.apply {
            // Add Book
            addBookBtn.setOnClickListener {
                val bookName = bookNameET.text.toString().trim()
                val authorName = authorNameET.text.toString().trim()
                bookViewModel.createBook(bookName, authorName)
            }

            // Cancel
            cancelBtn.setOnClickListener {
                finish()
            }
        }
    }

    private fun observeViewModel() {
        bookViewModel.success.observe(this) { message ->
            Log.i(TAG, "Success: $message")
            if (message != null) {
                customToast(this, message)
                bookViewModel.clearSuccess()
                finish()
            }
        }

        bookViewModel.error.observe(this) { message ->
            Log.i(TAG, "Error: $message")
            if (message != null) {
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
                    .setAction("Retry") {
                        printLogInfo("Retry is clicked...")
                        // handle retry logic here
                    }
                    .show()
                bookViewModel.clearError()
            }
        }
    }

    // TOOLBAR BACK ICON CLICK
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val TAG = "AddBookActivity"
    }
}