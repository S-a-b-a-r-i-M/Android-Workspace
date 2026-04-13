package com.example.firstapplication.learn_draggable.usingxmlviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firstapplication.R
import com.example.firstapplication.databinding.ActivityReorderableListUsingXmlBinding
import java.util.Collections

class ReorderableListUsingXmlActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReorderableListUsingXmlBinding
    private lateinit var reorderableListAdapter: ReorderableXMLListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReorderableListUsingXmlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
    }

    fun init() {
        binding.titleView.text = "Reorderable List"
        reorderableListAdapter = ReorderableXMLListAdapter()
        val dragCallback = DragCallback(reorderableListAdapter)
        val itemTouchHelper = ItemTouchHelper(dragCallback)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ReorderableListUsingXmlActivity)
            adapter = reorderableListAdapter
            itemTouchHelper.attachToRecyclerView(this)
        }
        reorderableListAdapter.setDataItems(listOf(
            ReorderableItem(
                title="1 Exclusive | Iranian Strike on U.S. Embassy Caused More Damage Than Disclosed - WSJ",
                urlToImage="https://images.wsj.net/im-43467832/social", author="Stephen Kalin, Robbie Gramer, Alexander Ward",
                content="RIYADH, Saudi ArabiaAn Iranian drone attack last month on the U.S. Embassy in Saudi Arabia did more extensive damage than previously disclosed, current and former American officials said, showing Ira… [+421 chars]",
                description="Two drones hit the Saudi compound, sparking a fire that raged for hours",
                publishedAt="2026-04-04T00:00:00Z"
            ),
            ReorderableItem(
                title = "2 Live Updates: U.S. fighter jet downed over Iran, 1 crew member rescued by American forces - CBS News",
                urlToImage = "https://assets3.cbsnewsstatic.com/hub/i/r/2026/04/03/3427b983-3164-41ca-b086-a0e15f685a9d/thumbnail/1200x630/3031aac935b7d61336bed23872047676/iran-war-b1-bridge-2268981536.jpg",
                author = "Tucker Reals, Lucia I Suarez Sang",
                content = "The CMA CGM Kribi, sailing under a Malta flag and operated by French shipping company CMA CGM, became possibly the first vessel with links to France to pass through the Strait of Hormuz since Iran ef… [+1782 chars]",
                description = "Iran shot down a U.S. fighter jet and one crew member was rescued by American forces as a search continues for a second crew member, U.S. officials say.",
                publishedAt = "2026-04-03T23:48:00Z",
            ),
            ReorderableItem(
                title = "3 United Airlines raises bag fees amid rising fuel costs and introduces tiered premium fares - AP News",
                urlToImage = "https://dims.apnews.com/dims4/default/2ea3d66/2147483647/strip/true/crop/6000x3998+0+1/resize/980x653!/quality/90/?url=https%3A%2F%2Fassets.apnews.com%2Fd4%2Fcf%2F57920833cac114cf5161d680099d%2F4d49837d17e1424f97fa95b401d75d10",
                author = "Rio Yamat, Ap Airlines, Travel Writer",
                content = "Most travelers flying with United Airlines will pay $10 more to check their luggage beginning on Friday, as higher jet fuel costs driven by the war in the Middle East push another major U.S. carrier … [+3297 chars]",
                description = "United Airlines is raising checked bag fees starting Friday. The move comes as higher fuel costs ripple through the airline industry. Most travelers flying within the U.S., Mexico, Canada and Latin America will now pay $45 for the first checked bag and $55 for the second.",
                publishedAt = "2026-04-03T23:31:00Z",
            ),
            ReorderableItem(
                title = "4 ‘This is a recalibration’: Trump Cabinet worries no one is safe after Bondi and Noem firings - CNN",
                urlToImage = "https://media.cnn.com/api/v1/images/stellar/prod/gettyimages-2235635789.jpg?c=16x9&q=w_800,c_fill",
                author = "Adam Cancryn, Kristen Holmes",
                content = "When President Donald Trump ousted his attorney general, Pam Bondi, it sent a clear message to his remaining Cabinet members: The job security they’ve enjoyed until now is no longer guaranteed.",
                description = "",
                publishedAt = "",
            ),
            ReorderableItem(
                title = "5 Live Updates: U.S. fighter jet downed over Iran, 1 crew member rescued by American forces - CBS News",
                urlToImage = "https://assets3.cbsnewsstatic.com/hub/i/r/2026/04/03/3427b983-3164-41ca-b086-a0e15f685a9d/thumbnail/1200x630/3031aac935b7d61336bed23872047676/iran-war-b1-bridge-2268981536.jpg",
                author = "5 Tucker Reals, Lucia I Suarez Sang",
                content = "5 The CMA CGM Kribi, sailing under a Malta flag and operated by French shipping company CMA CGM, became possibly the first vessel with links to France to pass through the Strait of Hormuz since Iran ef… [+1782 chars]",
                description = "5 Iran shot down a U.S. fighter jet and one crew member was rescued by American forces as a search continues for a second crew member, U.S. officials say.",
                publishedAt = "2026-04-03T23:48:00Z",
            ),
        ))
    }
}

data class ReorderableItem(
    val title: String,
    val urlToImage: String,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String
)

class ReorderableXMLListAdapter(val onClick: (ReorderableItem) -> Unit = {}) : RecyclerView.Adapter<ReorderableXMLListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val descriptionTV: TextView = itemView.findViewById(R.id.descriptionTV)
        val dateTimeTV: TextView = itemView.findViewById(R.id.dateTimeTV)

        fun bind(item: ReorderableItem) {
            // Load ImageView
            Glide.with(itemView)
                .load(item.urlToImage)
                .into(imageView)
            // Set Other Details
            titleTV.text = item.title
            descriptionTV.text = item.description
            dateTimeTV.text = item.publishedAt
            // OnClick Event
            itemView.setOnClickListener { onClick(item) }
        }
    }

    private var dataItems: MutableList<ReorderableItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.news_item, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataItems[position])
    }

    override fun getItemCount() = dataItems.size

    fun setDataItems(newDataItems: List<ReorderableItem>) {
        dataItems = newDataItems.toMutableList()
        notifyDataSetChanged()
    }

    fun moveItems(from: Int, to: Int) {
        dataItems[from] = dataItems.set(to, dataItems[from])
        notifyItemMoved(from, to)
    }
}

class DragCallback(private val adapter: ReorderableXMLListAdapter) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN, // drag directions
    0 // swipe directions (0 = disabled)
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val from = viewHolder.bindingAdapterPosition
        val to = target.bindingAdapterPosition
        adapter.moveItems(from, to)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // not needed, swipe disabled
    }
}
