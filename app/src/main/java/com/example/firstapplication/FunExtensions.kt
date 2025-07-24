package com.example.firstapplication

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Button.setGotoPreviousPage() {
    setOnClickListener {
        (context as Activity).finish() // Destroys current activity, returns to previous
    }
}

fun View.setGotoTargetPage(targetActivity: Class<out Activity>) {
    setOnClickListener {
        val intent = Intent(context, targetActivity)
        context.startActivity(intent)
    }
}

fun View.setGotoTargetPageForResult(activityResultLauncher: ActivityResultLauncher<Intent>, targetActivity: Class<out Activity>) {
    setOnClickListener {
        val intent = Intent(context, targetActivity)
        activityResultLauncher.launch(intent)
    }
}

//fun AppCompatActivity.loadFragment(containerViewId: Int, fragment: Fragment) {
//    supportFragmentManager
//        .beginTransaction()
//        .replace(containerViewId, fragment)
//        .commit()
//}

