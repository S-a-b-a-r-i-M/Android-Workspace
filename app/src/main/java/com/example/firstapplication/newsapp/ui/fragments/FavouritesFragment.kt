package com.example.firstapplication.newsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.R
import com.example.firstapplication.databinding.FragmentFavouritesBinding
import com.example.firstapplication.newsapp.adapters.NewsAdapter
import com.example.firstapplication.newsapp.models.Article
import com.example.firstapplication.newsapp.ui.NewsMainActivity
import com.example.firstapplication.newsapp.ui.NewsViewModel


/* TODO
    1. Show Articles that saved in the db
    2. If the user click open that article
    3. swipe left delete article
    4. can undo the action
 */

class FavouritesFragment : Fragment(R.layout.fragment_favourites) {
    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouritesBinding.bind(view)
        newsViewModel = (requireActivity() as NewsMainActivity).viewModel

        setupRecyclerView()

        // Rendering another view inside an fragment.
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val errorView: View = inflater.inflate(R.layout.item_error, null)

        setupRecyclerView()

        // Swipe Left Delete
        println(ItemTouchHelper.LEFT and ItemTouchHelper.RIGHT)
            val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvFavourites)

        val articles = newsViewModel.getFavourites().value
        if (articles != null)
            newsAdapter.setArticles(articles)
    }


    private fun onItemClick(article: Article) {
        val destinationFragment = ArticleFragment()
        destinationFragment.arguments = Bundle().apply {
            putSerializable("article", article)
        }
        // or
        newsViewModel.currentArticle = article
        (requireActivity() as NewsMainActivity).loadFragment(destinationFragment, true)
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(::onItemClick)
        binding.rvFavourites.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}