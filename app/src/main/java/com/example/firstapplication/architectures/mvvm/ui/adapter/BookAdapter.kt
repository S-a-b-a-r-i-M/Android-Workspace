package com.example.firstapplication.architectures.mvvm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.R
import com.example.firstapplication.architectures.mvvm.data.model.Book

/**
 * Simple RecyclerView.Adapter implementation
 * Easier to understand but less efficient for large datasets
 */
class BookAdapter(private val onBookClick: (Book) -> Unit) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    private var books: List<Book> = emptyList()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bookName: TextView = itemView.findViewById(R.id.bookNameTV)
        private val authorName: TextView = itemView.findViewById(R.id.authorNameTV)

        fun bind(book: Book) {
            bookName.text = book.name
            authorName.text = book.author
            itemView.setOnClickListener { onBookClick(book) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.detail_book_view, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    /**
     * Update the list of books and notify adapter
     * This will redraw the entire list (less efficient)
     */
    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged() // Redraws entire list
    }

    /**
     * Add a single book to the list
     * More efficient as it only updates one item
     */
    fun addBook(book: Book) {
        val mutableBooks = books.toMutableList()
        mutableBooks.add(book)
        books = mutableBooks
        notifyItemInserted(books.size - 1) // Only animates the new item
    }

    /**
     * Remove book from list
     * More efficient as it only removes one item
     */
    fun removeBook(bookId: Long) {
        val index = books.indexOfFirst { it.id == bookId }
        if (index != -1) {
            val mutableBooks = books.toMutableList()
            mutableBooks.removeAt(index)
            books = mutableBooks
            notifyItemRemoved(index) // Only animates the removed item
        }
    }
}