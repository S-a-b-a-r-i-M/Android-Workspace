package com.example.firstapplication

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapplication.databinding.ActivityBottomSheetBinding
import cutomutils.setWindowInsets

class BottomSheetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBottomSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowInsets()

        setListeners()
    }

    private fun setListeners() {
        with(binding) {
            btnOpenCustomBSheet.setOnClickListener {
                showCustomBottomSheet()
            }
        }
    }

    private fun showCustomBottomSheet() {
        val bottomDialog = Dialog(this)
        bottomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE) /*
            The requestWindowFeature(Window.FEATURE_NO_TITLE) method removes the default title bar from the dialog.
            Important timing: This must be called BEFORE setContentView() or any content is set on the dialog. If you call it after setting content, it will throw an exception or have no effect.
        */
        bottomDialog.setContentView(R.layout.custom_bottom_sheet_layout)

        // Dialog Views Listeners


        bottomDialog.window?.let {
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.attributes.windowAnimations = R.style.DialogAnimation
            it.setGravity(Gravity.BOTTOM)
        }
        bottomDialog.show()
    }
}