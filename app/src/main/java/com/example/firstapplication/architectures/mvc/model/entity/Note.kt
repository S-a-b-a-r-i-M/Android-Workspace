package com.example.firstapplication.architectures.mvc.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(val id: Long, val title: String, val description: String?) : Parcelable
