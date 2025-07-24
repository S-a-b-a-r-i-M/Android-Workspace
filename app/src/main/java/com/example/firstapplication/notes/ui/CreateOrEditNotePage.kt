package com.example.firstapplication.notes.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.R
import com.example.firstapplication.notes.data.DatabaseHelper
import com.example.firstapplication.databinding.ActivityCreateOrEditNotePageBinding
import com.example.firstapplication.notes.core.AbstractNoteRepo
import com.example.firstapplication.notes.core.Result
import com.example.firstapplication.notes.core.entity.Note
import com.example.firstapplication.notes.data.NoteRepoImpl
import cutomutils.customToast

class CreateOrEditNotePage : AppCompatActivity() {
    private lateinit var binding: ActivityCreateOrEditNotePageBinding
    private lateinit var noteRepo: AbstractNoteRepo

    private var existingNote: Note? = null
    private var position: Int = -1
    private var isEditMode = false

    companion object {
        private const val TAG = "CreateOrEditNotePage"
        const val EXTRA_NOTE = "note"
        const val EXTRA_NOTE_POSITION = "position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateOrEditNotePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        noteRepo = NoteRepoImpl(DatabaseHelper.getInstance(this))
        existingNote = intent.getParcelableExtra<Note?>(EXTRA_NOTE)
        position = intent.getIntExtra("position", -1)

        existingNote?.let {
            setupUI(it)
            isEditMode = true
        }
        // SAVE FUNCTIONALITY
        setUpSaveButton()
    }

    private fun setupUI(note: Note) {
        binding.titleTV.text = "Edit Note."
        binding.noteTitleET.setText(note.title)
        binding.noteDescriptionET.setText(note.description)
    }

    private fun updateExistingNote(title: String, description: String) {
        val noteToUpdate = existingNote
        if (noteToUpdate == null) {
            Log.w(TAG, "Edit Mode is True but Existing note data is not found")
            return
        }

        // UPDATE IN DB
        val updatedNote = Note(
            id = noteToUpdate.id,
            title = title,
            description = description
        )
        when(val result = noteRepo.updateNote(updatedNote)) {
            is Result.Error -> {
                Log.i(
                    TAG,
                    "Note(id:${updatedNote.id} update failed due to ${result.message}"
                )
                customToast(this, "Note note saved, try later....")
            }
            is Result.Success<Boolean> -> {
                Log.i(TAG, "New updated successfully(id: ${updatedNote.id}")
                finishWithResult(updatedNote)
                customToast(this, "Note Updated ✅")
            }
        }
    }

    private fun createNewNote(title: String, description: String) {
        when(val result = noteRepo.createNote(title, description)) {
            is Result.Error -> {
                Log.i(
                    TAG,
                    "Note creation failed due to ${result.message}"
                )
            }
            is Result.Success<Note> -> {
                Log.i(TAG, "New Note created with id: ${result.data.id}")
                finishWithResult(result.data)
                customToast(this, "Note saved successfully ✅")
            }
        }
    }

    private fun finishWithResult(note: Note) {
        setResult(
            RESULT_OK,
            Intent()
                .putExtra(EXTRA_NOTE, note)
                .putExtra(EXTRA_NOTE_POSITION, position)
        )
        finish()
    }

    private fun setUpSaveButton() {
        binding.saveIBtn.setOnClickListener {
            val title = binding.noteTitleET.text.toString().trim()
            val description = binding.noteDescriptionET.text.toString().trim()

            if (isEditMode)
                updateExistingNote(title, description)
            else
                createNewNote(title, description)
        }
    }
}