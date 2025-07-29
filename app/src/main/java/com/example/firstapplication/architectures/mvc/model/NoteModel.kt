package com.example.firstapplication.architectures.mvc.model

import com.example.firstapplication.architectures.mvc.model.entity.Note
import cutomutils.Result

interface NoteModel {
    fun getAllNotes(): Result<List<Note>>
    fun createNote(title: String, description: String?): Result<Note>
    fun updateNote(modifiedData: Note): Result<Boolean>
    fun deleteNote(id: Long): Result<Boolean>
}