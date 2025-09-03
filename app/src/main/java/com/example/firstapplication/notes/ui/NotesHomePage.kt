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
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.LifeCycleInfoAppCompactActivity
import com.example.firstapplication.R
import com.example.firstapplication.databinding.ActivityNotesHomePageBinding
import com.example.firstapplication.notes.core.AbstractNoteRepo
import cutomutils.Result
import com.example.firstapplication.notes.core.entity.Note
import com.example.firstapplication.notes.data.DatabaseHelper
import com.example.firstapplication.notes.data.NoteRepoImpl
import cutomutils.logInfo
import cutomutils.setGotoTargetPageForResult

class NotesHomePage : LifeCycleInfoAppCompactActivity() {
    private lateinit var binding: ActivityNotesHomePageBinding
    private lateinit var noteRepo: AbstractNoteRepo
    private lateinit var noteAdapter: NoteAdapter

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

    private fun handleNoteStatusChange(noteId: Long, position: Int, isCompleted: Boolean) {
        Log.d(TAG, "Status button clicked for id: $noteId")
        when(val result = noteRepo.updateNoteStatus(noteId, isCompleted)) {
            is Result.Error -> {
                Log.e(TAG, "Status change failed: ${result.message}")
                showToast("Note staus not changed")
            }
            is Result.Success<Boolean> -> {
                if (result.data) {
                    noteAdapter.updateNoteStatus(position, isCompleted)
                    showToast("Note Status changed successfully")
                }
                else
                    showToast("Failed to change note status")
            }
            Result.Loading -> TODO()
        }
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            logInfo("onScrollStateChanged -----> $newState")
            super.onScrollStateChanged(recyclerView, newState)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val firstElement = layoutManager.findFirstVisibleItemPosition()
            val lastElement = layoutManager.findLastVisibleItemPosition() // layoutManager.findLastCompletelyVisibleItemPosition()
            val childCount = layoutManager.childCount
            val totalAdapterItemCount = recyclerView.adapter?.itemCount ?: layoutManager.itemCount
            val totalLayoutItemCount = layoutManager.itemCount

            logInfo("onScrollStateChanged --> totalAdapterItemCount: $totalAdapterItemCount, totalLayoutItemCount: $totalLayoutItemCount")
            logInfo("onScrollStateChanged --> firstElement: $firstElement, lastElement:$lastElement, childCount: $childCount")
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            logInfo("onScrolled -----> dx:$dx, dy:$dy")
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    private fun setUpRecyclerView() {
        binding.notesRView.layoutManager = LinearLayoutManager(this)
        val notes = loadNotesFromDB()
        noteAdapter = NoteAdapter(
            notes,
            ::handleEditNote,
            ::handleDeleteNote,
            ::handleNoteStatusChange
        )
        binding.notesRView.adapter = noteAdapter
        binding.notesRView.addOnScrollListener(scrollListener)
    }

    private fun handleEditResult(data: Intent) {
        // GET UPDATED NOTE AND POSITION AS A RESULT
        val updatedNote = data.getParcelableExtra<Note?>(CreateOrEditNotePage.EXTRA_NOTE)
        val position = data.getIntExtra(CreateOrEditNotePage.EXTRA_NOTE_POSITION, -1)

        if (updatedNote != null && position >= 0)
            noteAdapter.updateNote(updatedNote.title, updatedNote.description, position)
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

    companion object {
        private const val TAG = "NotesHomePage"
    }
}