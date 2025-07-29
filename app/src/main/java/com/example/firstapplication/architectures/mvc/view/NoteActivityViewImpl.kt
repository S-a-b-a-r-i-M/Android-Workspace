package com.example.firstapplication.architectures.mvc.view

import android.app.Activity
import android.content.Context
import com.example.firstapplication.architectures.mvc.controller.NoteController
import com.example.firstapplication.architectures.mvc.model.entity.Note
import com.example.firstapplication.databinding.ActivityNoteBinding

class NoteActivityViewImpl(context: Context) : AbstractNoteActivityView {
    private var noteController: NoteController = NoteController(context, this)
    private var binding: ActivityNoteBinding = ActivityNoteBinding.inflate((context as Activity).layoutInflater)

    override fun getRootView() = binding.root

    override fun initViews() {
        with(binding) {
            addBtn.setOnClickListener {
                noteController.onAddButtonClicked(
                    noteET.text.toString(),
                    descriptionET.text.toString()
                )
            }

            removeBtn.setOnClickListener {
                noteController.onRemoveButtonClicked(removeNoteET.text.toString().toLong())
            }
        }
    }

    override fun bindDataToView() {
        noteController.onViewLoaded()
    }

    override fun showAllNotes(notes: List<Note>) {
        binding.textView.text = notes.toString()
    }

    override fun updateView(notes: List<Note>) {
        showAllNotes(notes)
        clearEditTexts()
    }

    private fun clearEditTexts() {
        with(binding) {
            noteET.text.clear()
            descriptionET.text.clear()
            removeNoteET.text.clear()
        }
    }
}