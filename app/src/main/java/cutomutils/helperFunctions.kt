package cutomutils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.core.view.drawToBitmap
import com.bumptech.glide.Glide
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun customToast(context: Context, message: String, durationTime: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, durationTime).show()
}

//fun AppCompatActivity.customToast(message: String, durationTime: Int = Toast.LENGTH_SHORT) {
//    Toast.makeText(baseContext, message, durationTime).show()
//}

fun Activity.printLogInfo(message: String) {
    Log.i(this.localClassName, message)
}

val Any.simpleClassName: String
    get() = this::class.java.simpleName

fun getOnlyClassName(classWithPackage: String) = classWithPackage.split(".").last()

fun Any.logInfo(message: String, throwable: Throwable? = null) {
    Log.i(simpleClassName, message, throwable)
}

fun Any.logDebug(message: String, throwable: Throwable? = null) {
    Log.d(simpleClassName, message, throwable)
}

fun Any.logWarning(message: String, throwable: Throwable? = null) {
    Log.w(simpleClassName, message, throwable)
}

fun Any.logError(message: String, throwable: Throwable? = null) {
    Log.e(simpleClassName, message, throwable)
}

object DatePattern {
    const val MONTH_DIGIT = "MM"
    const val MONTH_SHORT_DIGIT = "M"
    const val MONTH_SHORT_NAME = "MMM"
    const val DATE = "d"
    const val YEAR_DIGIT = "yyyy"
    const val YEAR_SHORT_DIGIT = "yy"
    const val HOURS_24 = "HH"
    const val HOURS_12 = "hh"
    const val MINUTES = "mm"
    const val MERIDIEM = "a"
}

/** epochTime - 10 digit of epoch time(not 13) */
fun formatEpochTime(epochTime: Long, format: String): String {
    val formatter = DateTimeFormatter.ofPattern(format)
    val instant = Instant.ofEpochSecond(epochTime)
    return instant.atZone(ZoneId.systemDefault()).format(formatter)
}

fun isWithIn7Days(epochTime: Long): Boolean {
    val givenDate = Instant.ofEpochSecond(epochTime)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    val currentDate = LocalDate.now()

    val dateDifference = ChronoUnit.DAYS.between(givenDate, currentDate)
    return dateDifference in 0..7
}