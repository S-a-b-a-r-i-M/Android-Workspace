package com.example.firstapplication.architectures.mvc.model.db

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.example.firstapplication.architectures.mvc.model.db.tables.NoteTable
import com.example.firstapplication.architectures.mvc.model.entity.Note
import cutomutils.ErrorCode
import cutomutils.Result

class NoteMVCRepo(private val dbHelper: DatabaseHelper) {

    fun createNote(title: String, description: String?): Result<Note> {
        val values = ContentValues().apply {
            put(NoteTable.COLUMN_TITLE, title)
            put(NoteTable.COLUMN_DESCRIPTION, description)
        }

        try {
            val id = dbHelper.writableDatabase.insert(NoteTable.TABLE_NAME, null, values)
            return if (id != -1L)
                Result.Success(Note(id, title, description))
            else
                Result.Error("Note Creation failed")
        } catch (exp: Exception) {
            Log.e("NoteRepoImpl", "Error on creating note")
            return Result.Error(exp.message.toString())
        }
    }

    fun getNoteById(id: Long): Result<Note?> {
        val whereClause = "${NoteTable.COLUMN_ID} = ?"
        val whereArgs = arrayOf(id.toString())
        try {
            dbHelper.readableDatabase.query(
                NoteTable.TABLE_NAME,
                null, // Get all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    val note: Note = parseNoteFromCursor(cursor)
                    return Result.Success(note)
                }
                return Result.Error(message = "Id Not Found", ErrorCode.RESOURCE_NOT_FOUND)
            }
        } catch (exp: Exception) {
            Log.e("NoteRepoImpl", "Error on reading note")
            return Result.Error(exp.message.toString())
        }
    }

    fun getAllNotes(offset: Int, limit: Int): Result<List<Note>> {
        try {
            val query = "SELECT * FROM ${NoteTable.TABLE_NAME} LIMIT $offset, $limit"
            dbHelper.readableDatabase.rawQuery(query, null).use { cursor ->
                val notes = mutableListOf<Note>()
                while (cursor.moveToNext())
                    notes.add(parseNoteFromCursor(cursor))

                return Result.Success(notes)
            }
        } catch (exp: Exception) {
            Log.e("NoteRepoImpl", "Error on reading all notes")
            return Result.Error(exp.message.toString())
        }
    }

    fun updateNote(modifiedData: Note): Result<Boolean> {
        val values = ContentValues().apply {
            put(NoteTable.COLUMN_TITLE, modifiedData.title)
            put(NoteTable.COLUMN_DESCRIPTION, modifiedData.description)
        }
        val whereClause = "${NoteTable.COLUMN_ID} = ?"
        val whereArgs = arrayOf(modifiedData.id.toString())

        try {
            val rowsUpdated = dbHelper.writableDatabase.update(
                NoteTable.TABLE_NAME,
                values,
                whereClause,
                whereArgs
            )

            return Result.Success(rowsUpdated > 0)
        } catch (exp: Exception) {
            Log.e("NoteRepoImpl", "Error on update note")
            return Result.Error(exp.message.toString())
        }
    }

    fun deleteNote(id: Long): Result<Boolean> {
        val whereClause = "${NoteTable.COLUMN_ID} = ?"
        val whereArgs = arrayOf(id.toString())

        try {
            val rowsDeleted = dbHelper.writableDatabase.delete(
                NoteTable.TABLE_NAME,
                whereClause,
                whereArgs
            )

            return Result.Success(rowsDeleted > 0)
        } catch (exp: Exception) {
            Log.e("NoteRepoImpl", "Error on update note")
            return Result.Error(exp.message.toString())
        }
    }

    private fun parseNoteFromCursor(cursor: Cursor) =
        Note(
            cursor.getLong(
                cursor.getColumnIndexOrThrow(NoteTable.COLUMN_ID)
            ),
            cursor.getString(
                cursor.getColumnIndexOrThrow(NoteTable.COLUMN_TITLE)
            ),
            cursor.getString(
                cursor.getColumnIndexOrThrow(NoteTable.COLUMN_DESCRIPTION)
            ),
        )
}