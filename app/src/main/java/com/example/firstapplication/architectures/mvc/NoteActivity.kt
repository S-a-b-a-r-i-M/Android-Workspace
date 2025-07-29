package com.example.firstapplication.architectures.mvc

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.R
import com.example.firstapplication.architectures.mvc.view.AbstractNoteActivityView
import com.example.firstapplication.architectures.mvc.view.NoteActivityViewImpl

class NoteActivity : AppCompatActivity() {
    private lateinit var noteActivityViewImpl: AbstractNoteActivityView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteActivityViewImpl = NoteActivityViewImpl(this)
        setContentView(noteActivityViewImpl.getRootView())
        addWindowInsets()

        noteActivityViewImpl.initViews()
    }

    fun addWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        noteActivityViewImpl.bindDataToView()
    }
}