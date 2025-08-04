package com.example.firstapplication

import android.content.Context
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        val items = arrayOf(
            "Narendra Modi - India",
            "Keir Starmer - United Kingdom",
            "Mark Carney - Canada",
            "Shigeru Ishiba - Japan",
            "Olaf Scholz - Germany",
            "Giorgia Meloni - Italy",
            "Anthony Albanese - Australia",
            "Mikhail Mishustin - Russia",
            "Dick Schoof - Netherlands",
            "Pedro Sánchez - Spain",
            "Mohammed bin Salman and Mohammed bin Salman2 - Saudi Arabia",
            "Qatar – Sheikh Mohammed bin Abdulrahman Al Thani",
            "Bangladesh – Muhammad Yunus ",
            "Pedro Sánchez - Spain",
        )

        val arrayAdapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, items
        )
        listView.adapter = arrayAdapter
        // OnClick listener for each item
        listView.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->
            customToast(this, "Selected item : ${items[position]}")
        }

        findViewById<Button>(R.id.nextPageBtn).setGotoTargetPage(ListActivity2::class.java)
    }
}

class ListAdapter<T>(context: Context, resourceId: Int) : ArrayAdapter<T>(context, resourceId) {

}