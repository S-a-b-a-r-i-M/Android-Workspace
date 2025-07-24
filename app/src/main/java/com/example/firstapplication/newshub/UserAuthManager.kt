package com.example.firstapplication.newshub

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.File

// Internal Storage - User Authentication
class UserAuthManager(private val context: Context) {
    val authDir = File(context.filesDir, "auth")
    val userTokenFileName = "user_token.txt"

    // TASK: Save user token to internal storage
    fun saveUserToken(token: String): Boolean {
        try {
            if (!authDir.exists() && !authDir.mkdir()) { // CHECK AND CRATE DIR
                Log.e(TAG, "Failed to create auth directory")
                return false
            }

            val tokenFile = File(authDir, userTokenFileName)
            tokenFile.writeText(token)
            Log.i(TAG, "User Token saved successfully.")
            return true
        } catch (exp: Exception) {
            Log.e(TAG, "${exp.message}")
            return false
        }
    }

    // TASK: Read user token
    fun getUserToken(): String? {
        return try {
            val userTokenFile = File(authDir, userTokenFileName)
            if (userTokenFile.exists())
                userTokenFile.readText() // returns the file contents as String
            else {
                Log.e(TAG, "user token file is not exists")
                null
            }
        } catch (exp: Exception) {
            Log.e(TAG, "${exp.message}")
            null
        }
    }

    // TASK: Save user reading preferences (internal files)
    fun saveReadingPreferences(preferences: Map<String, Any>): Boolean {
        val filename = "user_prefs.json"
        return try {
            val userPrefFile = File(context.filesDir, filename)
            val preferencesStr = JSONObject(preferences).toString(2)
            userPrefFile.writeText(preferencesStr)
            true
        } catch (exp: Exception) {
            Log.e(TAG, "${exp.message}")
            false
        }
    }

    companion object {
        private const val TAG = "UserAuthManagement"
    }
}