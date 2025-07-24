package com.example.firstapplication.notes.core.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(val id: Long, val title: String, val description: String?) : Parcelable