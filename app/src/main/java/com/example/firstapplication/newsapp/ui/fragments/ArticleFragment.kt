package com.example.firstapplication.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.firstapplication.R
import com.example.firstapplication.databinding.FragmentArticleBinding
import com.example.firstapplication.newsapp.ui.NewsMainActivity
import com.example.firstapplication.newsapp.ui.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {
    private lateinit var newViewModel: NewsViewModel
    private lateinit var binding: FragmentArticleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        newViewModel = (activity as NewsMainActivity).viewModel
        val article = newViewModel.article ?: return

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        binding.toFavFab.setOnClickListener {
            newViewModel.addToFavourites(article)
            Toast.makeText((activity), "Added to favourites", Toast.LENGTH_SHORT).show()
        }
    }
}