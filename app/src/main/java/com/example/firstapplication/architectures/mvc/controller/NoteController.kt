package com.example.firstapplication.architectures.mvc.controller

import android.content.Context
import com.example.firstapplication.architectures.mvc.model.NoteModelImpl
import com.example.firstapplication.architectures.mvc.model.entity.Note
import cutomutils.Result
import com.example.firstapplication.architectures.mvc.view.NoteActivityViewImpl

class NoteController(context: Context, val noteActivityViewImpl : NoteActivityViewImpl) {
    private val noteMVCModelImpl = NoteModelImpl(context)

    fun onViewLoaded() {
        val result = noteMVCModelImpl.getAllNotes()
        when(result) {
            is Result.Success<List<Note>> -> noteActivityViewImpl.showAllNotes(result.data)
            is Result.Error -> TODO()
            Result.Loading -> TODO()
        }
    }
    
    private fun updateView() {
        val result = noteMVCModelImpl.getAllNotes()
        when (result) {
            is Result.Success<List<Note>> -> noteActivityViewImpl.updateView(result.data)
            is Result.Error -> TODO()
            Result.Loading -> TODO()
        }
    }

    fun onAddButtonClicked(title: String, description: String) {
        val result = noteMVCModelImpl.createNote(title, description)
    }

    fun onRemoveButtonClicked(id: Long) {
        val result = noteMVCModelImpl.deleteNote(id)
    }
}