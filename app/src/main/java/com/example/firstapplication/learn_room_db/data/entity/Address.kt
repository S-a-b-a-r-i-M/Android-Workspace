package com.example.firstapplication.learn_room_db.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val street: String? = null,
    val city: String,
    val pincode: Int
) : Parcelable {
    companion object {
        const val STREET_COL = "street"
        const val CITY_COL = "city"
        const val PINCODE_COL = "pincode"
    }
}
