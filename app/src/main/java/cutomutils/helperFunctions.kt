package cutomutils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

fun customToast(context: Context, message: String, durationTime: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, durationTime).show()
}

//fun AppCompatActivity.customToast(message: String, durationTime: Int = Toast.LENGTH_SHORT) {
//    Toast.makeText(baseContext, message, durationTime).show()
//}

fun Activity.printLogInfo(message: String) {
    Log.i(this.localClassName, message)
}