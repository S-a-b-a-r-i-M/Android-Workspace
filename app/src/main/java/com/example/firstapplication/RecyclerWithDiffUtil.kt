package com.example.firstapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firstapplication.databinding.ActivityRecyclerWithDiffUtilBinding

class RecyclerWithDiffUtil : AppCompatActivity() {
    private lateinit var binding: ActivityRecyclerWithDiffUtilBinding
    private lateinit var recyclerAdapter: RecyclerWithDiffUtilAdapter
    private var offset = 0
    private var limit = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecyclerWithDiffUtilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowInsets()

        setRecyclerView()
        setClickListeners()
    }

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setRecyclerView() {
        recyclerAdapter = RecyclerWithDiffUtilAdapter()
        recyclerAdapter.setDataList(events.subList(0, 2))
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = recyclerAdapter
    }

    private fun setClickListeners() {
        binding.button.setOnClickListener {
            recyclerAdapter.insertData(events[(0..events.size).random()].copy(id = recyclerAdapter.getItemCount()))
        }
    }

    private fun loadData(offset: Int, limit: Int): List<Event> {
        return events.subList(
            offset.coerceAtLeast(0),
            limit.coerceAtMost(events.size)
        )
    }
}

data class Event (
    val id: Int,
    val imageUrl: String,
    val title: String,
    val description: String,
    val source: String,
    val date: String
)

class RecyclerWithDiffUtilAdapter()
    : RecyclerView.Adapter<RecyclerWithDiffUtilAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val descriptionTV: TextView = itemView.findViewById(R.id.descriptionTV)
        val articleSourceTV: TextView = itemView.findViewById(R.id.articleSourceTV)
        val dateTimeTV: TextView = itemView.findViewById(R.id.dateTimeTV)

        fun bind(event: Event) {
            Glide.with(itemView)
                .load(event.imageUrl.toUri())
                .into(imageView)

            titleTV.text = event.title
            descriptionTV.text = event.description
            articleSourceTV.text = event.source
            dateTimeTV.text = event.date
        }
    }

    private var dataList: MutableList<Event> = mutableListOf()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dataList.size

    fun insertData(event: Event) {
        dataList.add(event)
        notifyItemInserted(dataList.size - 1)
    }

    fun insertDataList(dataList: List<Event>) {
        this.dataList.addAll(dataList)
    }

    fun setDataList(newDataList: List<Event>) {
        dataList = newDataList.toMutableList()
        notifyDataSetChanged()
    }
}


// DiffUtil
class MyDiffUtil (
    private val oldList: List<Event>,
    private val newList: List<Event>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemIdx: Int, newItemIdx: Int): Boolean {
        return oldList[oldItemIdx].id == newList[newItemIdx].id
    }

    override fun areContentsTheSame(oldItemIdx: Int, newItemIdx: Int): Boolean {
        val oldData = oldList[oldItemIdx]
        val newData = newList[newItemIdx]
        return !(
            oldData.id != newData.id ||
            oldData.title != newData.title ||
            oldData.description != newData.description ||
            oldData.source != newData.source ||
            oldData.date != newData.date
        )
    }
}

// Data
val events = listOf(
    Event(1, "https://picsum.photos/200/300?random=1", "Tech Conference 2025", "Global leaders discuss the future of AI and blockchain.", "TechCrunch", "2025-01-15"),
    Event(2, "https://picsum.photos/200/300?random=2", "World Cup Final", "Thrilling cricket world cup final with record-breaking scores.", "ESPN", "2025-03-10"),
    Event(3, "https://picsum.photos/200/300?random=3", "SpaceX Mars Mission", "SpaceX launches its first human-crewed mission to Mars.", "NASA", "2025-04-20"),
    Event(4, "https://picsum.photos/200/300?random=4", "Music Awards Night", "Celebrating the best in global music.", "Billboard", "2025-02-05"),
    Event(5, "https://picsum.photos/200/300?random=5", "Global Startup Summit", "Entrepreneurs pitch next-gen innovations.", "Forbes", "2025-01-28"),
    Event(6, "https://picsum.photos/200/300?random=6", "Olympics Opening Ceremony", "Spectacular performances kick off the games.", "BBC", "2025-07-26"),
    Event(7, "https://picsum.photos/200/300?random=7", "Apple Product Launch", "Apple unveils its latest iPhone series.", "The Verge", "2025-09-14"),
    Event(8, "https://picsum.photos/200/300?random=8", "Nobel Prize Ceremony", "Winners in physics and chemistry announced.", "Reuters", "2025-10-10"),
    Event(9, "https://picsum.photos/200/300?random=9", "Fashion Week Paris", "Designers showcase their spring collections.", "Vogue", "2025-03-02"),
    Event(10, "https://picsum.photos/200/300?random=10", "Marathon 2025", "Thousands join the annual marathon event.", "CNN", "2025-04-18"),
    Event(11, "https://picsum.photos/200/300?random=11", "Film Festival Cannes", "Premieres of highly anticipated films.", "Hollywood Reporter", "2025-05-12"),
    Event(12, "https://picsum.photos/200/300?random=12", "G20 Summit", "Leaders discuss global economic issues.", "Al Jazeera", "2025-06-15"),
    Event(13, "https://picsum.photos/200/300?random=13", "E-Sports Championship", "Millions tune in to watch global gamers.", "IGN", "2025-08-21"),
    Event(14, "https://picsum.photos/200/300?random=14", "World Environment Day", "Campaigns to raise awareness on climate change.", "National Geographic", "2025-06-05"),
    Event(15, "https://picsum.photos/200/300?random=15", "Literature Festival", "Authors share their latest works.", "The Guardian", "2025-02-19"),
    Event(16, "https://picsum.photos/200/300?random=16", "Robotics Expo", "Showcasing future automation and robotics tech.", "Wired", "2025-03-25"),
    Event(17, "https://picsum.photos/200/300?random=17", "Independence Day Parade", "Nation celebrates independence with parade.", "NDTV", "2025-08-15"),
    Event(18, "https://picsum.photos/200/300?random=18", "Mars Rover Discovery", "NASA reveals new findings from Mars.", "NASA", "2025-09-09"),
    Event(19, "https://picsum.photos/200/300?random=19", "Startup IPO Launch", "Tech startup debuts on stock exchange.", "Bloomberg", "2025-01-30"),
    Event(20, "https://picsum.photos/200/300?random=20", "Food Festival", "Street food and cuisines from around the world.", "Times Now", "2025-02-22"),
    Event(21, "https://picsum.photos/200/300?random=21", "Education Summit", "Reimagining the future of learning.", "EdTech Magazine", "2025-04-10"),
    Event(22, "https://picsum.photos/200/300?random=22", "International Space Summit", "Space agencies collaborate on lunar missions.", "Space.com", "2025-07-05"),
    Event(23, "https://picsum.photos/200/300?random=23", "Film Premiere", "Blockbuster movie world premiere.", "Variety", "2025-11-18"),
    Event(24, "https://picsum.photos/200/300?random=24", "Health Conference", "Doctors and scientists discuss public health.", "WHO", "2025-05-03"),
    Event(25, "https://picsum.photos/200/300?random=25", "Art Exhibition", "Modern artists display their works.", "NY Times", "2025-06-28"),
    Event(26, "https://picsum.photos/200/300?random=26", "Space Telescope Launch", "New telescope launched into orbit.", "ESA", "2025-07-12"),
    Event(27, "https://picsum.photos/200/300?random=27", "Wildlife Photography Awards", "Celebrating stunning wildlife captures.", "NatGeo", "2025-09-01"),
    Event(28, "https://picsum.photos/200/300?random=28", "World Economic Forum", "Global leaders gather in Davos.", "CNBC", "2025-01-22"),
    Event(29, "https://picsum.photos/200/300?random=29", "Science Fair", "Students present innovative science projects.", "Science Daily", "2025-03-11"),
    Event(30, "https://picsum.photos/200/300?random=30", "Startup Hackathon", "Developers compete in a 48-hour hackathon.", "HackerRank", "2025-06-02"),
    Event(31, "https://picsum.photos/200/300?random=31", "Book Launch", "Bestselling author releases new novel.", "Penguin", "2025-02-14"),
    Event(32, "https://picsum.photos/200/300?random=32", "Gaming Expo", "New consoles and games revealed.", "GameSpot", "2025-05-09"),
    Event(33, "https://picsum.photos/200/300?random=33", "Music Festival", "Three-day live music extravaganza.", "Rolling Stone", "2025-07-21"),
    Event(34, "https://picsum.photos/200/300?random=34", "Marathon Run", "Charity run with international participants.", "CNN", "2025-09-15"),
    Event(35, "https://picsum.photos/200/300?random=35", "Drone Expo", "Exploring commercial uses of drones.", "TechRadar", "2025-03-06"),
    Event(36, "https://picsum.photos/200/300?random=36", "Blockchain Conference", "Future of decentralized finance discussed.", "CoinDesk", "2025-08-09"),
    Event(37, "https://picsum.photos/200/300?random=37", "AI Summit", "Breakthroughs in generative AI showcased.", "MIT Tech Review", "2025-06-19"),
    Event(38, "https://picsum.photos/200/300?random=38", "Film Awards", "Recognizing best movies and actors of the year.", "Oscars", "2025-03-28"),
    Event(39, "https://picsum.photos/200/300?random=39", "Jazz Night", "Live jazz performance at the city hall.", "Billboard", "2025-04-12"),
    Event(40, "https://picsum.photos/200/300?random=40", "TED Talks", "Experts share inspiring stories and ideas.", "TED", "2025-05-25"),
    Event(41, "https://picsum.photos/200/300?random=41", "International Yoga Day", "Millions practice yoga together worldwide.", "WHO", "2025-06-21"),
    Event(42, "https://picsum.photos/200/300?random=42", "Formula 1 Grand Prix", "Thrilling F1 racing event.", "Sky Sports", "2025-07-19"),
    Event(43, "https://picsum.photos/200/300?random=43", "Global Robotics Cup", "Teams compete in building robots.", "IEEE", "2025-08-24"),
    Event(44, "https://picsum.photos/200/300?random=44", "New Year’s Eve Celebration", "Fireworks and parties welcome the new year.", "CNN", "2025-12-31"),
    Event(45, "https://picsum.photos/200/300?random=45", "Charity Gala", "Fundraising event for global causes.", "UNICEF", "2025-09-10"),
    Event(46, "https://picsum.photos/200/300?random=46", "SpaceX Starship Launch", "Starship reaches new milestones.", "SpaceX", "2025-11-05"),
    Event(47, "https://picsum.photos/200/300?random=47", "VR Expo", "Virtual reality shaping future of entertainment.", "CNET", "2025-05-07"),
    Event(48, "https://picsum.photos/200/300?random=48", "Medical Breakthrough", "Scientists announce cancer treatment progress.", "WHO", "2025-10-15"),
    Event(49, "https://picsum.photos/200/300?random=49", "Climate Change Summit", "Nations commit to reducing carbon footprint.", "UN", "2025-11-22"),
    Event(50, "https://picsum.photos/200/300?random=50", "Photography Expo", "Capturing moments through the lens.", "NatGeo", "2025-04-09"),
    Event(51, "https://picsum.photos/200/300?random=51", "E-commerce Conference", "Discussing future of online retail.", "Amazon", "2025-06-14"),
    Event(52, "https://picsum.photos/200/300?random=52", "City Carnival", "A week of parades, music, and food.", "Local Times", "2025-02-27"),
    Event(53, "https://picsum.photos/200/300?random=53", "UN Assembly", "World leaders address global challenges.", "UN", "2025-09-20"),
    Event(54, "https://picsum.photos/200/300?random=54", "Science Lecture", "Renowned scientist delivers keynote talk.", "Nature", "2025-03-22"),
    Event(55, "https://picsum.photos/200/300?random=55", "Innovation Awards", "Honoring breakthroughs in science and tech.", "IEEE", "2025-04-17"),
    Event(56, "https://picsum.photos/200/300?random=56", "Startup Meetup", "Founders connect and share journeys.", "Y Combinator", "2025-08-13"),
    Event(57, "https://picsum.photos/200/300?random=57", "History Exhibition", "Exploring ancient civilizations.", "History Channel", "2025-07-04"),
    Event(58, "https://picsum.photos/200/300?random=58", "Renewable Energy Forum", "Future of sustainable energy discussed.", "Greenpeace", "2025-10-08"),
    Event(59, "https://picsum.photos/200/300?random=59", "Film Documentary Screening", "Documentary on wildlife preservation.", "BBC Earth", "2025-11-02"),
    Event(60, "https://picsum.photos/200/300?random=60", "Cycling Championship", "Cyclists compete across continents.", "ESPN", "2025-05-30"),
    Event(61, "https://picsum.photos/200/300?random=61", "New Book Fair", "Publishers present their latest books.", "BookFair Org", "2025-02-09"),
    Event(62, "https://picsum.photos/200/300?random=62", "International Space Day", "Celebrating achievements in space exploration.", "NASA", "2025-05-01"),
    Event(63, "https://picsum.photos/200/300?random=63", "Startup Pitch Fest", "Investors and entrepreneurs meet.", "TechCrunch", "2025-03-13"),
    Event(64, "https://picsum.photos/200/300?random=64", "Artificial Intelligence Expo", "Future AI trends revealed.", "MIT", "2025-06-27"),
    Event(65, "https://picsum.photos/200/300?random=65", "Basketball Finals", "NBA Finals conclude with dramatic finish.", "NBA", "2025-07-29"),
    Event(66, "https://picsum.photos/200/300?random=66", "Medical Expo", "Healthcare innovation on display.", "WHO", "2025-04-04"),
    Event(67, "https://picsum.photos/200/300?random=67", "Earth Day Campaign", "Actions to protect the planet.", "Greenpeace", "2025-04-22"),
    Event(68, "https://picsum.photos/200/300?random=68", "Coding Bootcamp", "Intense learning experience for devs.", "FreeCodeCamp", "2025-05-16"),
    Event(69, "https://picsum.photos/200/300?random=69", "International Art Fair", "Celebrating creativity worldwide.", "Art Daily", "2025-10-01"),
    Event(70, "https://picsum.photos/200/300?random=70", "Film Makers Meetup", "Directors and producers share insights.", "Hollywood Reporter", "2025-06-07"),
    Event(71, "https://picsum.photos/200/300?random=71", "Concert Night", "Rock band performs live for thousands.", "Rolling Stone", "2025-07-13"),
    Event(72, "https://picsum.photos/200/300?random=72", "Tech HackFest", "Students compete in AI coding challenges.", "HackerRank", "2025-08-02"),
    Event(73, "https://picsum.photos/200/300?random=73", "Cultural Heritage Day", "Celebrating traditions worldwide.", "UNESCO", "2025-09-25"),
    Event(74, "https://picsum.photos/200/300?random=74", "Mobile World Congress", "Latest smartphones unveiled.", "GSM Arena", "2025-03-01"),
    Event(75, "https://picsum.photos/200/300?random=75", "Marathon Charity Run", "Runners raise funds for cancer research.", "ESPN", "2025-12-05")
)
