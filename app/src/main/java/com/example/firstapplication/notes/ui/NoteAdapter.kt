package com.example.firstapplication.notes.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapplication.R
import com.example.firstapplication.notes.core.entity.Note
import cutomutils.DatePattern
import cutomutils.formatEpochTime

class NoteAdapter(
    notes: List<Note>,
    private val onEditClick: (Note, Int) -> Unit,
    private val onDeleteClick: (Long, Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV: TextView = itemView.findViewById(R.id.noteTitleTV)
        val descriptionTV: TextView = itemView.findViewById(R.id.noteDescriptionTV)
        val createdTimeTV: TextView = itemView.findViewById(R.id.createdTimeTV)
        val editBtn: ImageButton = itemView.findViewById(R.id.editIBtn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.deleteIBtn)
        val separatorTV: TextView = itemView.findViewById(R.id.separatorTV)

        /** 4 POINTS TO WRITE BINDING LOGIC INSIDE VIEW HOLDER CLASS
         * 1. Single Responsibility Principle (Adapter's job: Manage data and lifecycle, ViewHolder's job: Handle view binding and presentation).
         * 2. Better Testability (Test view logic without creating entire adapter/RecyclerView setup)
         * 3. Easier Debugging & Maintenance (When UI breaks, you know exactly where to look - inside ViewHolder)
         * 4. Reusability Across Adapters
         */
        fun bind(displayNote: DisplayNoteData, position: Int) {
            titleTV.text = displayNote.note.title
            descriptionTV.text = displayNote.note.description
            createdTimeTV.text = displayNote.createdAtTime ?: ""

            // Handle date separator
            if (displayNote.showDateSeparator) {
                separatorTV.visibility = View.VISIBLE
                separatorTV.text = displayNote.createdAtDate
            } else
                separatorTV.visibility = View.GONE

            // Set click listeners
            editBtn.setOnClickListener { onEditClick(displayNote.note, position) }
            deleteBtn.setOnClickListener { onDeleteClick(displayNote.note.id, position) }
        }
    }

    private lateinit var displayNotes: MutableList<DisplayNoteData>
    private var createViewHolderInvokeCount = 0
    private var bindViewHolderInvokeCount = 0

    init {
        loadDisplayNoteList(notes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder invoked count:${++createViewHolderInvokeCount} ")
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.single_note_item, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder with position(${position}), invoked count: ${++bindViewHolderInvokeCount}")
        holder.bind(displayNotes[position], position)
    }

    override fun getItemCount() = displayNotes.size

    // Public methods for updating the adapter
    fun refreshAllNotes(newNotes: List<Note>) {
        loadDisplayNoteList(newNotes)
        notifyDataSetChanged()
    }

    fun addNote(newNote: Note) {
        displayNotes.add(getDisplayNote(newNote)) // Add to end
        notifyItemInserted(displayNotes.size - 1)
        // notifyDataSetChanged() // Will rebind all other only visible items
    }

    private fun isValidPosition(position: Int): Boolean {
        if (0 > position || position >= displayNotes.size ) {
            Log.e(TAG, "position($position) is not valid")
            return false
        }

        return true
    }

    fun updateNote(updatedNote: Note, position: Int) {
        if (isValidPosition(position)) {
            displayNotes[position] = getDisplayNote(updatedNote)
            Log.d(TAG, "updateNote position($position)")
            notifyItemChanged(position)
        }
    }

    fun removeNote(position: Int) {
        if (isValidPosition(position)) {
            Log.d(TAG, "removeNote position($position)")
            displayNotes.removeAt(position)
            notifyItemRemoved(position)
            // Notify about range change to update positions
            if (position < displayNotes.size) // If the removed element is not last, then ->
                notifyItemRangeChanged(position, displayNotes.size - position)
//                notifyItemRangeRemoved(position, notes.size - position) // TODO: Understand the difference
        }
    }

    // TODO: This kind of logic is not presentation (here is adapter) responsibility.
    private fun getDisplayNote(note: Note): DisplayNoteData {
        var createdAtTime: String? = null
        var createdAtDate: String? = null
        if (note.createdAt != null) {
            createdAtTime = formatEpochTime(
                note.createdAt,
                "${DatePattern.HOURS_12}:${DatePattern.MINUTES} ${DatePattern.MERIDIEM}"
            )
            createdAtDate = formatEpochTime(
                note.createdAt,
                "${DatePattern.DATE} ${DatePattern.MONTH_SHORT_NAME} ${DatePattern.YEAR_DIGIT}"
            )
        }
        return DisplayNoteData(
            note = note,
            showDateSeparator = false,
            createdAtTime = createdAtTime,
            createdAtDate = createdAtDate,
        )
    }

    private fun loadDisplayNoteList(notes: List<Note>) {
        displayNotes = notes.mapIndexed { idx, note -> getDisplayNote(note) }.toMutableList()
        displayNotes.forEachIndexed { idx, note ->
            note.showDateSeparator = shouldShowSeparator(note.createdAtDate, idx)
        }
    }

    private fun shouldShowSeparator(createdAtDate: String?, position: Int) = createdAtDate != null && (
        position == 0 || createdAtDate != displayNotes[position-1].createdAtDate
    )

    companion object {
        private const val TAG = "NoteAdapter"
    }
}