package com.example.firstapplication.learn_app_widgets

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import cutomutils.logDebug
import cutomutils.logInfo

class MyBroadCastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        logInfo("BroadCast Received !!!!")

        if (intent == null) return
        logDebug("Intent Action: ${intent.action}")

        // TODO-NOTE: ACTION_TIME_CHANGED is Working.
        val message = when (intent.action) {
            Intent.ACTION_POWER_CONNECTED -> "Power Connected🔋 - via broadcast receiver"
            Intent .ACTION_POWER_DISCONNECTED -> "Power DisConnected🪫 - via broadcast receiver"
            Intent .ACTION_TIME_CHANGED -> "Time changed⏰ - via broadcast receiver"
            else -> "OnReceive received via broadcast receiver"
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}