package com.example.firstapplication.notes.ui

import android.os.Parcelable
import com.example.firstapplication.notes.core.entity.Note
import kotlinx.parcelize.Parcelize

@Parcelize
data class DisplayNoteData(
    val note: Note,
    var showDateSeparator: Boolean,
    val createdAtTime: String? = null,
    val createdAtDate: String? = null,
) : Parcelable