package com.example.firstapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.databinding.ActivityViewPager6Binding

class ViewPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewPager6Binding
    // Step 1: Declare Variables
    private lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewPager6Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Step 6: Set Adapter
        pagerAdapter = ViewPagerAdapter(listOf(
            "text 1",
            "text 2",
            "text 3",
        ))
        val viewPager = binding.viewPager
        viewPager.adapter = pagerAdapter
        // Changing Orientation
//        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        // Fake Drag
        viewPager.beginFakeDrag()
        viewPager.fakeDragBy(-2.0f)
        viewPager.endFakeDrag()
    }
}

// Step 3: Create ViewPagerAdapter Class
class ViewPagerAdapter(val dataList: List<String>) : RecyclerView.Adapter<ViewPagerViewHolder>() {
    // Step 5: Implement members
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.view_pager_single_element, parent, false
        )
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount() = dataList.size
}

// Step 2: Create ViewHolder Class
class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { // It can be a inner class of adapter
    private val pagerContentTv: TextView = itemView.findViewById(R.id.pager_content_tv)

    fun bind(content: String) {
        pagerContentTv.text = content
        pagerContentTv.setTextColor(
            itemView.context.resources.getColor(R.color.purple_200)
        )
    }
}