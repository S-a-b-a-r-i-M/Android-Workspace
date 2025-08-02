package com.example.firstapplication

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.architectures.mvp.presentation.view.UserActivity
import com.example.firstapplication.architectures.mvvm.ui.activity.BooksActivity
import com.example.firstapplication.databinding.ActivityHomePageBinding
import com.example.firstapplication.auth.SignUpActivity
import com.example.firstapplication.coffeeshop.GetStartedPage
import com.example.firstapplication.coroutine.CounterActivity
import com.example.firstapplication.custom.CustomToolbar
import com.example.firstapplication.newshub.NewsHubHomePage
import com.example.firstapplication.notes.ui.NotesHomePage
import cutomutils.customToast
import cutomutils.printLogInfo

class HomePageActivity : StackInfoAppCompactActivity() {
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var adapter: HomePageAdapter

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { it ->
        if (it.resultCode == RESULT_OK && it.data != null) {
            val name: String? = it.data?.getStringExtra("name") ?: ""
            customToast(this, "Welcome back $name 🥳")
        }
    }

    private fun isPortrait() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    private fun isDarkModeOn() = (
        resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    ) == Configuration.UI_MODE_NIGHT_YES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen() // SPLASH SCREEN
        Thread.sleep(500)

        enableEdgeToEdge()
        printLogInfo("onCreate.....-------------->")
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // UI MODE
        printLogInfo("Is Dark Mode: ${isDarkModeOn()}")

        // ADD CUSTOM TOOLBAR
        setSupportActionBar(binding.toolbar)

        // PREPARE DATA LIST
        val activityDataList = listOf(
            SingleActivityData(
                "Basic Views - 1",
                "Toast, Input, CheckBox, Toggle, Radio examples...",
                MainActivity::class.java,
                R.drawable.outline_android_24
            ),SingleActivityData(
                "Basic Views - 2",
                "Alert Dialog, Intent examples...",
                MainActivity2::class.java,
                R.drawable.outline_android_24,
                activityResultLauncher
            ),SingleActivityData(
                "Scrollable List",
                "Vertically scrollable list example...",
                ListActivity::class.java,
                R.drawable.outline_featured_play_list_24
            ),SingleActivityData(
                "Fragment",
                "Simple fragments example...",
                FragmentsActivity::class.java,
                R.drawable.outline_fullscreen_portrait_24
            ),SingleActivityData(
                "View Pager2",
                "Slide view using view pager2 example...",
                ViewPagerActivity::class.java,
                R.drawable.outline_animated_images_24
            ),SingleActivityData(
                "RecyclerView1",
                "Vertically scrollable recycler view example...",
                RecyclerViewActivity::class.java,
                R.drawable.outline_list_alt_24
            ),SingleActivityData(
                "Tabs",
                "Multiple tabs example...",
                TabsActivity::class.java,
                R.drawable.outline_aod_tablet_24
            ),SingleActivityData(
                "Responsive",
                "Responsive images for orientations example...",
                ResponsiveActivity::class.java,
                R.drawable.outline_amp_stories_24
            ),SingleActivityData(
                "Navigation Drawer",
                "Navigation Drawer with 5 menu items example...",
                NavigationDrawerActivity::class.java,
                R.drawable.outline_menu_open_24
            ),
            SingleActivityData(
                "Prevent Data Loss",
                "Ways of preventing data loss while changing configs...",
                PersistDataWhileConfigChange::class.java
            ),
            SingleActivityData(
                "Bottom Navigation",
                "Navigation menu in screen bottom...",
                BottomNavigationActivity::class.java,
                iconId = R.drawable.outline_bottom_navigation_24
            ),
            SingleActivityData(
                "Coffee Shop App Design",
                "A coffee shop app only design...",
                GetStartedPage::class.java,
                iconId = R.drawable.outline_coffee_24
            ),
            SingleActivityData(
                "Authentication With Database",
                "Authentication pages with SqLite Database...",
                SignUpActivity::class.java,
                iconId = R.drawable.outline_domain_verification_24
            ),
            SingleActivityData(
                "Notes App",
                "Notes taking app with SqLite Database...",
                NotesHomePage::class.java,
                iconId = R.drawable.outline_add_notes_24
            ),
            SingleActivityData(
                "Receive Image",
                "Will receive an image from shared app.....",
                ImplicitIntentActivity::class.java,
                iconId = R.drawable.baseline_call_received_24
            ),
            SingleActivityData(
                "Send Image",
                "Choose and share an image.....",
                ShareImageActivity::class.java,
                iconId = R.drawable.outline_share_24
            ),
            SingleActivityData(
                "Shared Preferences",
                "Saving simple key-value paris in shared preferences.....",
                SharedPreferenceDemoActivity::class.java,
                iconId = R.drawable.outline_file_save_24
            ),
            SingleActivityData(
                "Storages",
                "Tried to implement all kind of storages.....",
                NewsHubHomePage::class.java,
                iconId = R.drawable.baseline_storage_24
            ),
            SingleActivityData(
                "Picker",
                "Accessing camera, file picker.....",
                PickerActivity::class.java,
                iconId = R.drawable.outline_network_ping_24
            ),
            SingleActivityData(
                "Image Download Activity",
                "",
                ImageDownloadActivity::class.java
            ),
            SingleActivityData(
                "ProgressBar & Spinner",
                "Implementing progress bar and spinner...",
                ProgressBarActivity::class.java,
                R.drawable.outline_progress_activity_24
            ),
            SingleActivityData(
                "File Download",
                "downloading pdf, doc, xml files into shared storage...",
                FileDownloadActivity::class.java,
                R.drawable.baseline_file_download_24
            ),
            SingleActivityData(
                "User View (MVP Architecture)",
                "Model-View-Presenter...",
                UserActivity::class.java,
                R.drawable.baseline_supervised_user_circle_24
            ),
            SingleActivityData(
                "Book View (MVVM Architecture)",
                "Model-View-ViewModel...",
                BooksActivity::class.java,
                R.drawable.outline_book_2_24
            ),
            SingleActivityData(
                "Coroutine Example 1",
                "Coroutine example with counter and long progress...",
                CounterActivity::class.java,
            ),
            SingleActivityData(
                "Custom Toolbar",
                "Created custom toolbar...",
                CustomToolbar::class.java,
                R.drawable.outline_toolbar_24
            ),
            SingleActivityData(
                "Bottom & Navigation drawer",
                "Implemented both navigation drawers...",
                AmazonNavigationActivity::class.java,
            )
        )
        val topicsRecyclerView = binding.topicsRecyclerView

        // ORIENTATION
        topicsRecyclerView.layoutManager = if (isPortrait())
            LinearLayoutManager(this)
        else
            GridLayoutManager(this, 2)

        adapter = HomePageAdapter(activityDataList).also { topicsRecyclerView.adapter = it }
    }

    // Life Cycle
    override fun onStart() {
        printLogInfo("onStart.....-------------->")
        val currentOrientation = resources.configuration.orientation
        printLogInfo(
            "orientation : ${if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) "Portrait" else "Landscape"}"
        )
        super.onStart()
    }

    override fun onResume() {
        printLogInfo("onResume.....-------------->")
        super.onResume()
    }

    override fun onPause() {
        printLogInfo("onPause.....-------------->")
        super.onPause()
    }

    override fun onStop() {
        printLogInfo("onStop.....-------------->")
        super.onStop()
    }

    override fun onRestart() {
        printLogInfo("onRestart.....-------------->")
        super.onRestart()
    }

    override fun onDestroy() {
        printLogInfo("onDestroy.....-------------->")
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homepage_toolbar_menu, menu)

        // GET THE SEARCH VIEW
        menu?.let {
            val searchView = menu.findItem(R.id.search)?.actionView as? SearchView ?: run {
                Log.e(TAG, "search menu not found")
                return@let
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.i(TAG, "onQueryTextChange : $newText")
                    if (newText != null) adapter.filterValues(newText)

                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.i(TAG, "onQueryTextSubmit : $query")
                    return false
                }
            })
        }
        return true
    }

    // SAVING & RESTORING DATA ON CONFIG CHANGES
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        printLogInfo(
            "onSaveInstanceState will be called after onStop before onDestroy-------------->"
        )
    }

    // I can replace this method responsibility by onCreate.
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        printLogInfo(
            "onRestoreInstanceState will be called after onStart before onResume------------>"
        )
    }

    companion object {
        private const val TAG = "HomePageActivity"
    }
}

data class SingleActivityData(
    val title: String,
    val description: String,
    val targetActivityClass: Class<out Activity>,
    val iconId: Int = R.drawable.outline_android_24,
    val activityResultLauncher: ActivityResultLauncher<Intent>? = null
)

class HomePageAdapter(var dataList: List<SingleActivityData>) : RecyclerView.Adapter<HomePageAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconView: ImageView = itemView.findViewById(R.id.iconView)
        private val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        private val descriptionTV: TextView = itemView.findViewById(R.id.descriptionTV)
        private val openActivityButton: Button = itemView.findViewById(R.id.openActivityBtn)

        fun bind(data: SingleActivityData) {
            iconView.setImageResource(data.iconId)
            titleTV.text = data.title
            descriptionTV.text = data.description
            if (data.activityResultLauncher != null)
                openActivityButton.setGotoTargetPageForResult(
                    data.activityResultLauncher, data.targetActivityClass
                )
            else
                openActivityButton.setGotoTargetPage(data.targetActivityClass)
        }
    }

    private var _dataList = dataList
    private var createViewHolderInvokeCount = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.single_activity_item, parent, false
        )
        Log.i("HomePageAdapter", "onCreateViewHolder invoked count:${++createViewHolderInvokeCount} ")
        return ViewHolder(view)
    }

    private var bindViewHolderInvokeCount = 0
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i("HomePageAdapter", "onBindViewHolder with position(${position + 1}), invoked count: ${++bindViewHolderInvokeCount}")
        holder.bind(_dataList[position])
    }

    override fun getItemCount(): Int {
        return _dataList.size
    }

    fun filterValues(query: String) {
        _dataList = dataList.filter { it.title.contains(query, true) }
        Log.i("HomePageAdapter", "filtered datalist: $_dataList")
        notifyDataSetChanged()
    }
}