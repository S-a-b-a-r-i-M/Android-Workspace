 package com.example.firstapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.databinding.ActivityRecyclerView2Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

 class TodoRecyclerViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecyclerView2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecyclerView2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val todos = mutableListOf(
            TodoData("Learn Android Architecture"),
            TodoData("Learn Android Components"),
            TodoData("Learn VCS"),
            TodoData("Learn Basic Views"),
            TodoData("Learn ViewPager2"),
            TodoData("Learn RecyclerView"),
            TodoData("Learn Navigation Drawer"),
            TodoData("Learn Tab Creation"),
        )
        // SET ADAPTER TO R.VIEW
        val recyclerView: RecyclerView = binding.recyclerView2
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = TodoAdapter(todos) {
            Toast(this).apply {
                setText("Todo completed successfully...")
            }.show()
        }
        recyclerView.adapter = adapter

        with(binding) {
            // ADD NEW TODO
            addTodoBtn.setOnClickListener {
                val newTodo = newTodoET.text.toString().trim()
                if (newTodo.isEmpty()) {
                    // Failure Toast
                    Toast(this@TodoRecyclerViewActivity).apply {
                        setText("Todo cannot be empty...")
                    }.show()
                    return@setOnClickListener
                }

                // Add New Todo
                todos.add(TodoData(newTodoET.text.toString()))
                newTodoET.text.clear()
                Toast(this@TodoRecyclerViewActivity).apply {
                    setText("New Todo added...")
                }.show()

                // Notify Adapter
                adapter.notifyItemInserted(todos.size - 1)
            }
        }
    }
}


data class TodoData(val title: String, val isCompleted: Boolean = false)

// CREATE ADAPTER CLASS
class TodoAdapter(
    private val todos: MutableList<TodoData>,
    private val onTodoRemoved: () -> Unit
) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val todoTv: TextView = itemView.findViewById(R.id.todoTV)
        private val todoCheckBox: CheckBox = itemView.findViewById(R.id.checkBox)

        fun bind(todo: TodoData, onTodoCompleted: (Int) -> Unit) {
            todoTv.text = todo.title
            todoCheckBox.isChecked = todo.isCompleted

            todoCheckBox.setOnCheckedChangeListener(null) // Clear previous listener to avoid unwanted triggers
            todoCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
//                    RecyclerView.NO_POSITION
                    onTodoCompleted(bindingAdapterPosition) /* bindingAdapterPosition is more accurate because
                    it represents the position at the time of binding, which is what we actually want when handling click events. */
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.single_todo_item, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todos[position], ::removeTodoAt)
    }

    private fun removeTodoAt(position:Int) {
        if (position >= 0 && position < todos.size) {
            CoroutineScope(Dispatchers.Main).launch{
                delay(300)
                todos.removeAt(position)
                notifyItemRemoved(position)
                onTodoRemoved()
            }
        }
    }

    override fun getItemCount() = todos.size
}