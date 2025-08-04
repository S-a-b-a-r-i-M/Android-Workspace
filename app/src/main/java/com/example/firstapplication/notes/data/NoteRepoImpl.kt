package com.example.firstapplication.notes.data

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import cutomutils.Result
import com.example.firstapplication.notes.core.AbstractNoteRepo
import com.example.firstapplication.notes.core.entity.Note
import cutomutils.ErrorCode

class NoteRepoImpl(private val dbHelper: DatabaseHelper) : AbstractNoteRepo {

    override fun createNote(title: String, description: String?): Result<Note> {
        val values = ContentValues().apply {
            put(NoteTable.COLUMN_TITLE, title)
            put(NoteTable.COLUMN_DESCRIPTION, description)
        }

        try {
            val id = dbHelper.writableDatabase.insert(NoteTable.TABLE_NAME, null, values)
            return if (id != -1L)
                getNoteById(id)
            else
                Result.Error("Note Creation failed")
        } catch (exp: Exception) {
            Log.e("NoteRepoImpl", "Error on creating note")
            return Result.Error(exp.message.toString())
        }
    }

    override fun getNoteById(id: Long): Result<Note> {
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

    override fun getAllNotes(offset: Int, limit: Int): Result<List<Note>> {
        try {
            val query = """
                SELECT * FROM ${NoteTable.TABLE_NAME} 
                ORDER BY ${NoteTable.COLUMN_CREATED_AT}
                LIMIT $offset, $limit
            """.trimIndent()

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

    override fun updateNote(modifiedData: Note): Result<Boolean> {
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

    override fun deleteNote(id: Long): Result<Boolean> {
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
            cursor.getLong(
                cursor.getColumnIndexOrThrow(NoteTable.COLUMN_CREATED_AT)
            )
        )
}