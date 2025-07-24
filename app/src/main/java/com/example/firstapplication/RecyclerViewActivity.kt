package com.example.firstapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.databinding.ActivityRecyclerViewBinding

class RecyclerViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecyclerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_recycler_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dataList = listOf(
            RVData(R.mipmap.ic_launcher, "Item1"),
            RVData(R.mipmap.ic_launcher, "Item2"),
            RVData(R.mipmap.ic_launcher, "Item3"),
            RVData(R.mipmap.ic_launcher, "Item4"),
            RVData(R.mipmap.ic_launcher, "Item5"),
            RVData(R.mipmap.ic_launcher, "Item6"),
            RVData(R.mipmap.ic_launcher, "Item7"),
            RVData(R.mipmap.ic_launcher, "Item8"),
            RVData(R.mipmap.ic_launcher, "Item9"),
            RVData(R.mipmap.ic_launcher, "Item10"),
            RVData(R.mipmap.ic_launcher, "Item11"),
            RVData(R.mipmap.ic_launcher, "Item12"),
            RVData(R.mipmap.ic_launcher, "Item13"),
        )
        // SETUP RECYCLER VIEW
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = GridLayoutManager(this, 2)
//        recyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = RVAdapter(dataList)
    }
}

// DATA CLASS FOR AN ITEM
data class RVData(val image: Int, val title: String)

// ADAPTER CLASS
class RVAdapter(val dataList: List<RVData>) : RecyclerView.Adapter<RVAdapter.ViewHolder>() {

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.imageView)
        private val titleTv: TextView = itemView.findViewById(R.id.titleTv)

        fun bind(data: RVData) {
            titleTv.text = data.title
            image.setImageResource(data.image)
        }
    }

  // Implement Member Functions
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("RecyclerViewActivity", parent.toString()) // parent - recycler view
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_single_item, parent, false
        )
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount() = dataList.size
}
