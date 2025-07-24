package com.example.firstapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.databinding.ActivityTabsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import cutomutils.customToast

class TabsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityTabsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Tabs
        val tabs = listOf(
            TabData("Poppy", R.drawable.poppey, R.mipmap.ic_launcher),
            TabData("Pikachu", R.drawable.pikachu, badgeCount = 2),
            TabData("Bob", R.drawable.sponge_bob),
            TabData("Mickey", R.drawable.mickey_mouse_disney),
            TabData("T & J", R.drawable.tom_and_jerry),
            TabData("bunny", R.drawable.bugs_bunny)
        )
        // Set Adapter
        val viewPager = binding.cartoonViewPager
        viewPager.adapter = TabViewPagerAdapter(tabs.map { tab -> tab.content })

        // TAB
        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            val currentTabData = tabs[position]
            /*
            tab.text = currentTabData.title
            if(currentTabData.iconId != -1)
                tab.setIcon(currentTabData.iconId) // Adding Icons
//            if(currentTabData.badgeCount > 0) TODO
//                tab.badge = currentTabData.badgeCount
             */

            // CUSTOM TAB
            val customTab = LayoutInflater.from(this).inflate(R.layout.custom_tab, null).apply {
                findViewById<TextView>(R.id.tabTitleTV).text = currentTabData.title
                val iconView: ImageView = findViewById(R.id.tabIconView)
                if(currentTabData.iconId != -1)
                    iconView.setImageResource(currentTabData.iconId)
                else
                    iconView.visibility = View.GONE
            }
            tab.customView = customTab
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                customToast(this@TabsActivity, "${tab?.customView?.findViewById<TextView>(R.id.tabTitleTV)?.text} Selected ✅")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                customToast(this@TabsActivity, "${tab?.customView?.findViewById<TextView>(R.id.tabTitleTV)?.text} Un-selected ❌")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                customToast(this@TabsActivity, "${tab?.customView?.findViewById<TextView>(R.id.tabTitleTV)?.text} Reselected 🔄")
            }
        })
    }
}

data class TabData<T>(val title: String, val content: T, val iconId: Int = -1, val badgeCount: Int = 0)

internal class TabViewPagerAdapter(val dataList: List<Int>) : RecyclerView.Adapter<TabViewPagerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(imageId: Int) {
            imageView.setImageResource(imageId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.cartoon_tab_sinle_item, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount() = dataList.size
}