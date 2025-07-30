package com.example.firstapplication

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.firstapplication.databinding.ActivityNavigationDrawerBinding
import com.google.android.material.navigation.NavigationView
import cutomutils.customToast

class NavigationDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityNavigationDrawerBinding

    // Register back press callback
    private val drawerCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.close()
            else {
                remove() // Remove this callback, let others handle
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // STEP 1: INITIALIZE LAYOUT, TOOLBAR & NAVIGATION VIEW
        drawerLayout = binding.drawerLayout
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navigationView: NavigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)
        /*
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> loadFragment(Home())
                R.id.nav_settings -> loadFragment(Settings())
                R.id.nav_share -> loadFragment(Share())
                R.id.nav_about -> loadFragment(About())
                R.id.nav_logout -> customToast(this, "Logout!!!")
            }
            drawerLayout.close()
            return true
        }
         */

        // STEP 2: SET THE ACTION BAR TOGGLE
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // STEP 4: ON BACK PRESS METHOD
//        onBackPressedDispatcher.addCallback(this, drawerCallBack)

        // STEP 5: DEFAULT FRAGMENT
        if (savedInstanceState == null) {
            loadFragment(Home())
            navigationView.setCheckedItem(R.id.nav_home)
        }
    }

    // STEP 6: ON NAVIGATION ITEM SELECTED
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId) {
            R.id.nav_home -> loadFragment(Home())
            R.id.nav_settings -> loadFragment(Settings())
            R.id.nav_share -> loadFragment(Share())
            R.id.nav_about -> loadFragment(About())
            R.id.nav_logout -> customToast(this, "Logout!!!")
        }
        drawerLayout.close()
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem) = toggle.onOptionsItemSelected(item)

    // STEP 3: REPLACE FRAGMENT METHOD
    fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}