package com.example.firstapplication.architectures.mvc.view

import com.example.firstapplication.architectures.mvc.model.entity.Note

interface AbstractNoteActivityView : AbstractView {
    fun showAllNotes(notes: List<Note>)

    fun updateView(notes: List<Note>)
}