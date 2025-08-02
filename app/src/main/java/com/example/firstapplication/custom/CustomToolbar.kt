package com.example.firstapplication.custom

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.R
import cutomutils.customToast
import cutomutils.printLogInfo

class CustomToolbar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_custom_toolbar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.whatsapp_toolbar_menu, menu)

        // SEARCH VIEW
        menu?.let {
            val searchView = menu.findItem(R.id.searchMenu)?.actionView as? SearchView ?: run {
                Log.e("CustomToolbar", "search menu not found")
                return@let
            }

            // SEARCH IMPLEMENTATION
            searchView.queryHint = "Type here..."
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    printLogInfo("onQueryTextChange, newText: $newText")
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    printLogInfo("onQueryTextSubmit, query: $query")
                    return true
                }
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.groupMenu -> showToast("New Group created.")
            R.id.broadcastMenu -> showToast("Broadcast published")
            R.id.settingsMenu -> showToast("Settings opened")
            R.id.cameraMenu -> showToast("Camera opened")
            else -> showToast("Invalid option")
        }
        return true
    }

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        customToast(this, message, duration)
    }
}
