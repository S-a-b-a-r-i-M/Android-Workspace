package com.example.firstapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val dataList: List<RVData> = listOf(
            RVData.Data1(R.mipmap.ic_launcher, "Item1"),
            RVData.Data1(R.mipmap.ic_launcher, "Item2"),
            RVData.Data2(R.mipmap.ic_launcher, "Item3", "description"),
            RVData.Data1(R.mipmap.ic_launcher, "Item4"),
            RVData.Data2(R.mipmap.ic_launcher, "Item5", "description"),
            RVData.Data2(R.mipmap.ic_launcher, "Item6", "description"),
            RVData.Data1(R.mipmap.ic_launcher, "Item7"),
            RVData.Data1(R.mipmap.ic_launcher, "Item8"),
            RVData.Data1(R.mipmap.ic_launcher, "Item9"),
            RVData.Data1(R.mipmap.ic_launcher, "Item10"),
            RVData.Data1(R.mipmap.ic_launcher, "Item11"),
            RVData.Data1(R.mipmap.ic_launcher, "Item12"),
            RVData.Data3(R.mipmap.ic_launcher),
            RVData.Data3(R.mipmap.ic_launcher),
            RVData.Data3(R.mipmap.ic_launcher),
            RVData.Data3(R.mipmap.ic_launcher),
            RVData.Data1(R.mipmap.ic_launcher, "Item17"),
            RVData.Data3(R.mipmap.ic_launcher),
            RVData.Data1(R.mipmap.ic_launcher, "Item19"),
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
sealed class RVData {
    data class Data1(val image: Int, val title: String) : RVData()
    data class Data2(val image: Int, val title: String, val description: String) : RVData()
    data class Data3(val image: Int) : RVData()
}


// ADAPTER CLASS
class RVAdapter(val dataList: List<RVData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class RVData1ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.imageView)
        private val titleTv: TextView = itemView.findViewById(R.id.titleTv)

        fun bind(data: RVData.Data1) {
             titleTv.text = data.title
             image.setImageResource(data.image)
        }
    }

    class RVData2ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.imageView)
        private val titleTv: TextView = itemView.findViewById(R.id.titleTv)
        private val descriptionTv: TextView = itemView.findViewById(R.id.descriptionTv)

        fun bind(data: RVData.Data2) {
            titleTv.text = data.title
            descriptionTv.text = data.description
            image.setImageResource(data.image)
        }
    }

    class RVData3ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(data: RVData.Data3) {
             image.setImageResource(data.image)
        }
    }

    companion object {
        const val DATA_VIEW_1 = 1
        const val DATA_VIEW_2 = 2
        const val DATA_VIEW_3 = 3
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataList[position]) {
            is RVData.Data1 -> DATA_VIEW_1
            is RVData.Data2 -> DATA_VIEW_2
            is RVData.Data3 -> DATA_VIEW_3
        }
    }

  // Implement Member Functions
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.i("RecyclerViewActivity", "onCreateViewHolder viewType: $viewType") // parent - recycler view

        fun createView(layoutId: Int) = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)

        return when (viewType) {
            DATA_VIEW_1 ->
              RVData1ViewHolder(createView(R.layout.recycler_view_single_item1))
            DATA_VIEW_2 ->
              RVData2ViewHolder(createView(R.layout.recycler_view_single_item2))
            DATA_VIEW_3 ->
              RVData3ViewHolder(createView(R.layout.recycler_view_single_item3))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    private var bindViewHolderInvokeCount = 0
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i("RecyclerViewActivity", "onBindViewHolder with position(${position}), invoked count: ${++bindViewHolderInvokeCount}, holder: $holder")
        val data = dataList[position]
        when (data) {
            is RVData.Data1 -> {
                if (holder is RVData1ViewHolder)
                    holder.bind(data)
                else // Note: These warning has never been executed
                    Log.w("RecyclerViewActivity", "Need to check or create RVData1ViewHolder's view ")
            }
            is RVData.Data2 -> {
                if (holder is RVData2ViewHolder)
                    holder.bind(data)
                else
                    Log.w("RecyclerViewActivity", "Need to check or create RVData2ViewHolder's view ")
            }
            is RVData.Data3 -> {
                if (holder is RVData3ViewHolder)
                    holder.bind(data)
                else
                    Log.w("RecyclerViewActivity", "Need to check or create RVData2ViewHolder's view ")
            }
        }
    }

    override fun getItemCount() = dataList.size
}
