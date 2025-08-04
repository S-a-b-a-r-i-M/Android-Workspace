package com.example.firstapplication.notes.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstapplication.LifeCycleInfoAppCompactActivity
import com.example.firstapplication.R
import com.example.firstapplication.databinding.ActivityNotesHomePageBinding
import com.example.firstapplication.notes.core.AbstractNoteRepo
import cutomutils.Result
import com.example.firstapplication.notes.core.entity.Note
import com.example.firstapplication.notes.data.DatabaseHelper
import com.example.firstapplication.notes.data.NoteRepoImpl
import cutomutils.setGotoTargetPageForResult

class NotesHomePage : LifeCycleInfoAppCompactActivity() {
    private lateinit var binding: ActivityNotesHomePageBinding
    private lateinit var noteRepo: AbstractNoteRepo
    private lateinit var noteAdapter: NoteAdapter

    companion object {
        private const val TAG = "ListOFNotesPage"
    }

    val editNoteResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK && it.data != null) handleEditResult(it.data!!)
    }

    val createNoteResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK && it.data != null) handleCreateResult(it.data!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotesHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // INITIALIZE
        noteRepo = NoteRepoImpl(DatabaseHelper.getInstance(this))

        // SETUP RECYCLER VIEW
        setUpRecyclerView()

        // ADD NEW NOTES
        binding.newNotesFBtn.setGotoTargetPageForResult(
            createNoteResultLauncher,
            CreateOrEditNotePage::class.java
        )
    }

    private fun loadNotesFromDB(): List<Note> {
        return when (val result = noteRepo.getAllNotes(0, 100)) {
            is Result.Error -> {
                Log.e(TAG, "Failed to load notes: ${result.message}")
                mutableListOf()
            }
            is Result.Success<List<Note>> -> result.data
            Result.Loading -> TODO()
        }
    }

    private fun handleEditNote(note: Note, position: Int) {
        Log.d(TAG, "Edit button clicked for note: $note")
        val intent = Intent(this, CreateOrEditNotePage::class.java).apply {
            putExtra(CreateOrEditNotePage.EXTRA_NOTE, note)
            putExtra(CreateOrEditNotePage.EXTRA_NOTE_POSITION, position)
        }
        editNoteResultLauncher.launch(intent)
    }

    private fun handleDeleteNote(noteId: Long, position: Int) {
        Log.d(TAG, "Delete button clicked for id: $noteId")
        when(val result = noteRepo.deleteNote(noteId)) {
            is Result.Error -> {
                Log.e(TAG, "Delete failed: ${result.message}")
                showToast("Note not deleted")
            }
            is Result.Success<Boolean> -> {
                if (result.data) {
                    noteAdapter.removeNote(position)
                    showToast("Note deleted successfully")
                }
                else
                    showToast("Failed to delete note")
            }
            Result.Loading -> TODO()
        }
    }

    private fun setUpRecyclerView() {
        binding.notesRView.layoutManager = LinearLayoutManager(this)
        val notes = loadNotesFromDB()
        noteAdapter = NoteAdapter(notes, ::handleEditNote, ::handleDeleteNote)
        binding.notesRView.adapter = noteAdapter
    }

    private fun handleEditResult(data: Intent) {
        // GET UPDATED NOTE AND POSITION AS A RESULT
        val updatedNote = data.getParcelableExtra<Note?>(CreateOrEditNotePage.EXTRA_NOTE)
        val position = data.getIntExtra(CreateOrEditNotePage.EXTRA_NOTE_POSITION, -1)

        if (updatedNote != null && position >= 0)
            noteAdapter.updateNote(updatedNote, position)
        else {
            Log.w(TAG, "handleCreateResult is refreshing whole notes...")
            // Fallback: refresh all notes if specific values not found
            noteAdapter.refreshAllNotes(loadNotesFromDB())
        }
    }

    private fun handleCreateResult(data: Intent) {
        val newNote = data.getParcelableExtra<Note?>("note")
        if (newNote != null)
            noteAdapter.addNote(newNote)
        else {
            // Fallback: refresh all notes
            Log.w(TAG, "handleCreateResult is refreshing whole notes...")
            noteAdapter.refreshAllNotes(loadNotesFromDB())
        }
    }

    private fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }
}