package com.example.firstapplication.newsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.R
import com.example.firstapplication.databinding.FragmentHeadlineBinding
import com.example.firstapplication.newsapp.adapters.NewsAdapter
import com.example.firstapplication.newsapp.models.Article
import com.example.firstapplication.newsapp.models.NewsResponse
import com.example.firstapplication.newsapp.ui.NewsMainActivity
import com.example.firstapplication.newsapp.ui.NewsViewModel
import com.example.firstapplication.newsapp.util.Resource
import cutomutils.logDebug
import cutomutils.logInfo
import cutomutils.logWarning

class HeadlinesFragment : Fragment(R.layout.fragment_headline) {
    private lateinit var binding: FragmentHeadlineBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var retryButton: Button
    private lateinit var errorTextView: TextView
    var isError = false
    var isLoading = false
    val isLastPage = false
    var isScrolling = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHeadlineBinding.bind(view)
        newsViewModel = (activity as NewsMainActivity).viewModel

        // Rendering another view inside an fragment.
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val errorView: View = inflater.inflate(R.layout.item_error, null)
        retryButton = errorView.findViewById(R.id.retryBtn)
        errorTextView = errorView.findViewById(R.id.errorText)

        setupRecyclerView()

        setupObservers()

        setupListeners()

        if (newsViewModel.headlinePageNumber == 1) // Initial Fetch
            newsViewModel.getHeadlines("us")
    }

    private fun setupObservers() {
        newsViewModel.headlinesDataState.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Resource.Success<NewsResponse> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data.let { data ->
                        newsAdapter.setArticles(data.articles)
                    }
                    logInfo("response received")
                }
                is Resource.Error -> {
                    hideProgressBar()
                    showErrorMessage(response.message)
                    logInfo("error")
                }
                Resource.Loading -> {
                    showProgressBar()
                    hideErrorMessage()
                    logInfo("loading")
                }
            }
        }
    }

    private fun setupListeners() {
        with(binding) {
        }
    }

    private fun showProgressBar() {
        binding.paginationLoading.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressBar() {
        binding.paginationLoading.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showErrorMessage(message: String) {
        binding.errorView.root.visibility = View.VISIBLE
        binding.errorView.errorText.text = message
        isError = true
    }

    private fun hideErrorMessage() {
        binding.errorView.root.visibility = View.INVISIBLE
        isError = false
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            logInfo("onScrollStateChanged -----> $newState")
            super.onScrollStateChanged(recyclerView, newState)

            val layoutManger = recyclerView.layoutManager as LinearLayoutManager

            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                isScrolling = true
                return // No need to fetch new items while scrolling
            }
            else if (newState == RecyclerView.SCROLL_STATE_IDLE)
                isScrolling = false

//            if (isLoading || !isMore) return

            val lastVisibleItemPosition = layoutManger.findLastCompletelyVisibleItemPosition() // index
            val totalItemCount = recyclerView.adapter?.itemCount ?: run {
                logWarning("totalItemCount is not accessible")
                return
            }
            val shouldPaginate = (lastVisibleItemPosition + 1) >= totalItemCount
            if (shouldPaginate) {
                logInfo("<----------- getHeadlines from onScrollStatechanged ---------->")
                newsViewModel.getHeadlines("us")
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            logInfo("onScrolled -----> dx:$dx, dy:$dy")
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    private fun onItemClick(article: Article) {
        logDebug("onItemClick is clicked $article")
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(::onItemClick)
        binding.rvFavourites.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(scrollListener)
        }
    }
}