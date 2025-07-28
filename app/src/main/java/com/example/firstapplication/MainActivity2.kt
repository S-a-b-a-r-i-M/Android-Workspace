package com.example.firstapplication

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.net.toUri

class MainActivity2 : StackInfoAppCompactActivity() {
    lateinit var returnDataTV: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // allows app to draw content behind system bars (status bar, navigation bar), enabling a modern, full-screen UI.
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            Log.i("MainActivity2", systemBars.toString())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val context: Context

      // RETURN DATA TO CALLED ACTIVITY
        returnDataTV = findViewById(R.id.returnDataTV)
        val returnDataBtn: Button = findViewById(R.id.saveNoteBtn)
        setReturnButtonClick(returnDataBtn)

      // ALERT DIALOG
        val alertButton: Button = findViewById(R.id.alertDialogBtn)
        setAlertDialog(alertButton)

      // IMPLICIT INTENT BUTTONS
        val openWebBtn: Button = findViewById(R.id.openWebBtn)
        setWebSearch(openWebBtn)
        val openMailBtn: Button = findViewById(R.id.openMailBtn)
        setOpenMail(openMailBtn)
    }

    // SET RETURN DATA TO CALLED ACTIVITY
    private fun setReturnButtonClick(returnDataBtn: Button) {
        returnDataBtn.setOnClickListener {
            val name = returnDataTV.text.toString().trim()
            if (name.isEmpty())
                setResult(RESULT_CANCELED)
            else
                setResult(RESULT_OK, Intent().putExtra("name", name))
            finish()
        }
    }

    private fun setAlertDialog(button: Button) {
        button.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this).apply {
                setTitle("Alert Dialog Title")
                setMessage("This is an example for alert dialog...")
                setPositiveButton("Accept") { dialog, which ->
                    cutomutils.customToast(this@MainActivity2, "positive button")
                }
                setNeutralButton("Remind Later") { dialog, which ->
                    cutomutils.customToast(this@MainActivity2, "neutral button")
                }
                setNegativeButton("Decline") { dialog, which ->
                    cutomutils.customToast(this@MainActivity2, "negative button")
                }
            }.create()

            alertDialog.show()
        }
    }

    // PERFORM WEB SEARCH
    private fun setWebSearch(button: Button) {
        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, "https://www.tamil.com".toUri())
//            if (intent.resolveActivity(packageManager) != null) // Not Working use try catch
                startActivity(intent)
//            else
//                println("No Package Manager available for open web search")
        }
    }

    // SET OPEN MAIL
    private fun setOpenMail(button: Button) {
        button.setOnClickListener {
            // for no attachment // Intent.ACTION_SEND - will open all message sending app (mail, message, quick share....)
            val intent = Intent(Intent.ACTION_SENDTO).apply {
//                setDataAndType("mailto:".toUri(), "*/*")
                data = "mailto:".toUri()
//                type = "*/*"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("sabariofficial2002gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Subject is Nothing")
                putExtra(Intent.EXTRA_TEXT, "Main Subject of this email is nothing.")
            }

            try {
                startActivity(intent)
            } catch (exp: ActivityNotFoundException) {
                println("No activity available for mail send")
            }
        }
    }

    // SAVING & RESTORING DATA ON CONFIG CHANGES
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("returnData", returnDataTV.text.toString())
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }
}