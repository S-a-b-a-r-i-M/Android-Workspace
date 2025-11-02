package cutomutils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

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

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun AppCompatActivity.clearBackStackHistory(tag: String, immediate: Boolean = false) {
    supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    if (immediate) // The above popBackStack is async.Hence, want to exec immediately before doing next action
        supportFragmentManager.executePendingTransactions()
}

fun AppCompatActivity.loadFragment(
    fragment: Fragment,
    containerId: Int,
    pushToBackStack: Boolean = false,
) {
    supportFragmentManager.beginTransaction().apply {
        replace(containerId, fragment)
        if (pushToBackStack) addToBackStack(fragment.simpleClassName) // adding the current fragment/activity into the backstack
        commit()
    }
}

fun AppCompatActivity.addFragment(
    fragment: Fragment,
    containerId: Int,
    pushToBackStack: Boolean = false,
) {
    // EXISTING FRAGMENT
    val existingFragment = supportFragmentManager.findFragmentById(containerId)

    supportFragmentManager.beginTransaction().apply {
        add(containerId, fragment)
        if (pushToBackStack) addToBackStack(fragment.simpleClassName) // adding the current fragment/activity into the backstack
        commit()
    }
}

