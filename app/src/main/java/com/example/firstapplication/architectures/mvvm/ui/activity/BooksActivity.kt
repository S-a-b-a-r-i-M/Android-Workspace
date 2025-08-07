package com.example.firstapplication.architectures.mvvm.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstapplication.R
import com.example.firstapplication.architectures.mvvm.data.database.BookDatabaseHelper
import com.example.firstapplication.architectures.mvvm.data.model.BookDaoImpl
import com.example.firstapplication.architectures.mvvm.data.repo.BookRepositoryImpl
import com.example.firstapplication.architectures.mvvm.ui.adapter.BookAdapter
import com.example.firstapplication.architectures.mvvm.ui.viewmodel.BookViewModel
import com.example.firstapplication.architectures.mvvm.ui.viewmodel.BookViewModelFactory
import com.example.firstapplication.databinding.ActivityBooksBinding
import com.google.android.material.snackbar.Snackbar
import cutomutils.customToast
import cutomutils.printLogInfo

class BooksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBooksBinding
    private lateinit var bookViewModel: BookViewModel
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        println("<---- onCreate. next is OnStart ---->")
        super.onCreate(savedInstanceState)
        binding = ActivityBooksBinding.inflate(layoutInflater)
        enableEdgeToEdge()
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
        setUpRecyclerView()
        setUpClickListeners()
        observeViewModel()

        // LOAD INITIAL DATA
        if (savedInstanceState == null) {
            printLogInfo("<--- onCreate called first time ---> ")
            bookViewModel.loadAllBooks()
        }
    }

    private fun setUpRecyclerView() {
        adapter = BookAdapter { book ->
            val intent = Intent(this, BookDetailActivity::class.java)
            intent.putExtra("book_id", book.id)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setUpClickListeners() {
        binding.fabAddBook.setOnClickListener {
            val intent = Intent(this, AddBookActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        println("<---- onStart. next is view model sends data to all observers ---->")
    }

    override fun onResume() {
        super.onResume()
        println("<---- onResume. data received before  ---->")
    }

    /**
     * Observe ViewModel's LiveData and update UI accordingly
     * This is the key aspect of MVVM - reactive UI updates
     */
    private fun observeViewModel() {
        bookViewModel.books.observe(this) { books ->
            Log.d(TAG, books.toString())
            adapter.updateBooks(books)
        }

        bookViewModel.loading.observe(this ) { isLoading ->
            Log.d(TAG, "isLoading: $isLoading")
            // Show some loading stats
        }

        bookViewModel.success.observe(this) { message ->
            Log.i(TAG, "Success: $message")
            if (message != null) {
                customToast(this, message)
                bookViewModel.clearSuccess()
            }
        }

        // NOTE: We can add mote than one observer for an single owner.
        bookViewModel.success.observe(this) { message ->
            Log.i(TAG, "Success2: $message")
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

    companion object {
        private const val TAG = "BooksActivity"
    }
}