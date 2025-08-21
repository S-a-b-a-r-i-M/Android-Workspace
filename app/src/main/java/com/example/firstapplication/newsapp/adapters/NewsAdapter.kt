package com.example.firstapplication.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firstapplication.R
import com.example.firstapplication.newsapp.models.Article

// TODO: Use Diff-Util and AsyncListDiffer
class NewsAdapter(
    val articles: List<Article>,
    val onClick: (Article) -> Unit = {}
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val sourceTV: TextView = itemView.findViewById(R.id.articleSourceTV)
        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val descriptionTV: TextView = itemView.findViewById(R.id.descriptionTV)
        val dateTimeTV: TextView = itemView.findViewById(R.id.dateTimeTV)

        fun bind(article: Article) {
            // Load ImageView
            Glide.with(itemView)
                .load(article.urlToImage)
                .into(imageView)
            // Set Other Details
            sourceTV.text = article.source.name
            titleTV.text = article.title
            descriptionTV.text = article.description
            dateTimeTV.text = article.publishedAt
            // OnClick Event
            itemView.setOnClickListener( { v ->
                onClick(article)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.news_item, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.size
}