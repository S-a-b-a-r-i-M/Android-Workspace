package com.example.firstapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ListActivity2 : AppCompatActivity() {
    companion object {
        val TAG = "ListActivity2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var selectedListItemId = -1
        val listView = findViewById<ListView>(R.id.listView)
        val leaders = listOf(
            "Alexander the Great – Macedonia (Greece)",
            "Julius Caesar – Roman Empire (Italy)",
            "Augustus Caesar – Roman Empire (Italy)",
            "Ashoka the Great – India (Maurya Empire)",
            "Qin Shi Huang – China",
            "Genghis Khan – Mongolia",
            "Saladin – Egypt/Syria (Ayyubid Dynasty)",
            "Akbar the Great – India (Mughal Empire)",
            "Tokugawa Ieyasu – Japan",
            "Charlemagne – Frankish Empire (France/Germany)",
            "George Washington – United States",
            "Napoleon Bonaparte – France",
            "Abraham Lincoln – United States",
            "Simón Bolívar – Venezuela/South America",
            "Queen Victoria – United Kingdom",
            "Otto von Bismarck – Germany",
            "Mahatma Gandhi – India (spiritual/political leader, not a head of state)",
            "Giuseppe Garibaldi – Italy",
            "Shaka Zulu – Zulu Kingdom (South Africa)",
            "Thomas Jefferson – United States",
            "Winston Churchill – United Kingdom",
            "Franklin D. Roosevelt – United States",
            "Joseph Stalin – Soviet Union (Russia)",
            "Theodore Roosevelt – United States",
            "Jawaharlal Nehru – India",
            "Nelson Mandela – South Africa",
            "Mustafa Kemal Atatürk – Turkey",
            "Dwight D. Eisenhower – United States",
            "Charles de Gaulle – France",
            "Ho Chi Minh – Vietnam"
        )

        val adapter = ArrayAdapter(
            this, R.layout.custom_single_list_item, leaders
        )
        listView.adapter = MyListAdapter(this, leaders) { view, position ->
            if (selectedListItemId == position)
                view.setBackgroundColor(resources.getColor(R.color.brown_orange))
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->
            println("parent: $parent, view: $view, position: $position, id: $id")
            selectedListItemId = position
            adapter.notifyDataSetChanged()
        }
    }
}

class MyListAdapter(context: Context, val dataList: List<String>, val onItemClick: (View, Int) -> Unit) :
    ArrayAdapter<String>(context, 0, dataList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context).inflate(
            R.layout.custom_single_list_item, parent, false
        )
        val textView: TextView = view.findViewById(R.id.textView)
        textView.text = dataList[position]
        onItemClick(view, position)
        return view
    }
}