package com.example.firstapplication.notes.core
import cutomutils.Result
import com.example.firstapplication.notes.core.entity.Note

interface AbstractNoteRepo {
    fun createNote(title: String, description: String?): Result<Note>

    fun getNoteById(id: Long): Result<Note?>

    fun getAllNotes(offset: Int, limit: Int): Result<List<Note>>

    fun updateNote(modifiedData: Note): Result<Boolean>

    fun updateNoteStatus(noteId: Long, isCompleted: Boolean): Result<Boolean>

    fun deleteNote(id: Long): Result<Boolean>
}