package com.example.firstapplication.coffeeshop

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.R
import com.example.firstapplication.databinding.ActivityHomePageBinding
import com.example.firstapplication.databinding.ActivityParentPageCfeBinding
import cutomutils.customToast

class ParentPage : AppCompatActivity() {
    private lateinit var binding: ActivityParentPageCfeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityParentPageCfeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Handle OnClicks For Custom Navigation Buttons
//        binding.bnavHome.setOnClickListener {
//            customToast(this, "Home Page")
//        }
//
//        binding.bnavFavourites.setOnClickListener {
//            customToast(this, "Favourites Page")
//        }
//
//        binding.bnavCart.setOnClickListener {
//            customToast(this, "Cart Page")
//        }
//
//        binding.bnavNotifications.setOnClickListener {
//            customToast(this, "Notification Page")
//        }

        BottomNavigationHandler(this).setUpNavItems()
    }
}

class  BottomNavigationHandler(private val activity: AppCompatActivity) {
    var selectedNavItemId = R.id.bnavHome

    data class NavItem(
        val defaultDrawable: Int,
        val selectedDrawable: Int,
        val label: String
    )

    // Map of NavItems
    val navItems = mapOf(
        R.id.bnavHome to NavItem(
            R.drawable.outline_home_app_logo_24,
            R.drawable.outline_home_app_logo_24,
            "Home"
        ),
        R.id.bnavFavourites to NavItem(
            R.drawable.outline_favorite_24,
            R.drawable.outline_favorite_24,
            "Fav"
        ),
        R.id.bnavCart to NavItem(
            R.drawable.outline_garden_cart_24,
            R.drawable.outline_garden_cart_24,
            "Cart"
        ),
        R.id.bnavNotifications to NavItem(
            R.drawable.outline_notifications_active_24,
            R.drawable.outline_notifications_active_24,
            "Notifications"
        )
    )

    // Items Click Listener
        // Deselect and Select and Default Select
    fun setUpNavItems() {
        navItems.forEach { navId, item ->
            activity.findViewById<ImageButton>(navId).apply {
                setOnClickListener {
                    selectNavItem(it as ImageButton)
                }
            }
        }
    }

    fun selectNavItem(navButton: ImageButton) {
        if (selectedNavItemId == navButton.id) return

        // Select
        val navItem = navItems[navButton.id] ?: return
        navButton.animate()
            .scaleX(1.8f)
            .scaleY(1.8f)
            .setDuration(200).withEndAction {
                navButton.setImageResource(R.drawable.outline_android_24)
        }

        navButton.setOnClickListener {
            customToast(activity, navItem.label)
        }

        selectedNavItemId = navButton.id
    }

    fun handleNavigation() {

    }
}