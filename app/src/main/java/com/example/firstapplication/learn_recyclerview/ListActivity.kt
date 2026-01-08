package com.example.firstapplication.learn_recyclerview

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.R
import cutomutils.customToast
import cutomutils.setGotoTargetPage

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    // LIST VIEW
        val listView: ListView = findViewById(R.id.simpleListView)
//        val items = arrayOf(
//            "Narendra Modi - India",
//            "Keir Starmer - United Kingdom",
//            "Mark Carney - Canada",
//            "Shigeru Ishiba - Japan",
//            "Olaf Scholz - Germany",
//            "Giorgia Meloni - Italy",
//            "Anthony Albanese - Australia",
//            "Mikhail Mishustin - Russia",
//            "Dick Schoof - Netherlands",
//            "Pedro Sánchez - Spain",
//            "Mohammed bin Salman and Mohammed bin Salman2 - Saudi Arabia",
//            "Qatar – Sheikh Mohammed bin Abdulrahman Al Thani",
//            "Bangladesh – Muhammad Yunus ",
//            "Pedro Sánchez - Spain",
//        )
        val items = (1..100).toList()
        var childAddedCount = 0
        var childRemovedCount = 0
        listView.setOnHierarchyChangeListener( object : ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View?, child: View?) {
                childAddedCount++
                child?.contentDescription = (child as TextView).text ?: ""
                println("onChildViewAdded: ${(child as TextView).text}")
            }

            override fun onChildViewRemoved(parent: View?, child: View?) {
                childRemovedCount++
                println("onChildViewRemoved: ${(child as TextView).text}")
            }
        })

        val arrayAdapter = ArrayAdapter<Int>(
            this, android.R.layout.simple_list_item_1, items
        )
        listView.adapter = arrayAdapter

        // OnClick listener for each item
        listView.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->
            customToast(this, "Selected item : ${items[position]}")
            println("childAddedCount: $childAddedCount , childRemovedCount: $childRemovedCount")
        }

        findViewById<Button>(R.id.nextPageBtn).setGotoTargetPage(ListActivity2::class.java)
    }
}

class CustomListAdapter<T>(
    context: Context,
    @LayoutRes resourceId: Int,
    items: List<T>,
) : ArrayAdapter<T>(context, resourceId, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = super.getView(position, convertView, parent)
        if(view is TextView) {
            view.contentDescription = view.text
            println("contentDescription: ${view.text}")
        }

        return view
    }
}