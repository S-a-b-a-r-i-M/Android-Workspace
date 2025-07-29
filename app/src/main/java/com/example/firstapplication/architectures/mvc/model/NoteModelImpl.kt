package com.example.firstapplication.architectures.mvc.model

import android.content.Context
import com.example.firstapplication.architectures.mvc.model.db.DatabaseHelper
import com.example.firstapplication.architectures.mvc.model.db.NoteMVCRepo
import com.example.firstapplication.architectures.mvc.model.entity.Note
import cutomutils.Result

class NoteModelImpl(context: Context) : NoteModel {
    private val noteRepo: NoteMVCRepo = NoteMVCRepo(DatabaseHelper.getInstance(context))

    override fun getAllNotes(): Result<List<Note>> {
        return noteRepo.getAllNotes(0, 10)
    }

    override fun createNote(title: String, description: String?): Result<Note> {
        return noteRepo.createNote(title, description)
    }

    override fun updateNote(modifiedData: Note): Result<Boolean> {
        return noteRepo.updateNote(modifiedData)
    }

    override fun deleteNote(id: Long): Result<Boolean> {
        return noteRepo.deleteNote(id)
    }
}