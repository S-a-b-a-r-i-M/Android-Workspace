package com.example.firstapplication.kt_java

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.firstapplication.R

class ToastDemo (private val context: Context) {
    private fun getToast(message: String, durationTime: Int) = Toast(context).apply {
        duration = durationTime
        setText(message)
    }

    fun setShortToast(view: View) {
        getToast("Welcome to Great Kirigaln's short show", Toast.LENGTH_SHORT).show()
    }

    fun setLongToast(view: View, additionalFunction: () -> Unit) {
        view.setOnClickListener {
            getToast("Welcome to Great Kirigaln's long show", Toast.LENGTH_LONG).show()
            // Adding animation
            it.animate().alpha(0f).setDuration(0)
                .withEndAction {
                    it.animate().alpha(1f).setDuration(15)
                }.start()

            additionalFunction()
        }
    }
}