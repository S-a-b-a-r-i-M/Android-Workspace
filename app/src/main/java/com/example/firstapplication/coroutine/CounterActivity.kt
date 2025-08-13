package com.example.firstapplication.coroutine

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.firstapplication.R
import com.example.firstapplication.databinding.ActivityCounterBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CounterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCounterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // SET UP VIEW PAGER WITH ADAPTER
        binding.viewPager.adapter = TabAdapter(this)

        // CONNECT TAB LAYOUT WITH VIEW PAGER
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Tab 1"
                }
                else -> {
                    tab.text = "Tab 2"
                }
            }
        }.attach()
    }
}

// FragmentStateAdapter is a class used with ViewPager2 in Android to manage fragments as pages
class TabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int) = when(position) {
        0 -> CounterWithCoroutineFragment()
//        1 -> CounterWithoutCoroutineFragment(
        else -> CounterWithoutCoroutineFragment()
    }

    override fun getItemCount() = 2
}