package com.example.firstapplication

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.net.toUri
import com.example.firstapplication.databinding.SuccessDialogBinding
import cutomutils.printLogInfo
import androidx.core.graphics.drawable.toDrawable
import com.example.firstapplication.databinding.ActivityMain2Binding

class MainActivity2 : StackInfoAppCompactActivity() {

    private lateinit var binding: ActivityMain2Binding
    lateinit var returnDataTV: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // allows app to draw content behind system bars (status bar, navigation bar), enabling a modern, full-screen UI.
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
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

      // CUSTOM ALERT DIALOG
        val customAlertButton: Button = findViewById(R.id.alertDialogCustomBtn)
        setCustomAlertDialog(customAlertButton)

      // IMPLICIT INTENT BUTTONS
        val openWebBtn: Button = findViewById(R.id.openWebBtn)
        setWebSearch(openWebBtn)
        val openMailBtn: Button = findViewById(R.id.openMailBtn)
        setOpenMail(openMailBtn)

      // CUSTOM RADIO BUTTON
        binding.pizzaRB.setOnCheckedChangeListener { buttonView, isChecked ->
            printLogInfo("Pizza Radio Button: $isChecked")
        }
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

    private fun setAlertDialog(button: Button)  {
        button.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Alert Dialog Title")
                .setMessage("This is an example for alert dialog...")
                .setPositiveButton("Accept") { dialog, which ->
                    cutomutils.customToast(this@MainActivity2, "positive button")
                }
                .setNeutralButton("Remind Later") { dialog, which ->
                    cutomutils.customToast(this@MainActivity2, "neutral button")
                }
                .setNegativeButton("Decline") { dialog, which ->
                    cutomutils.customToast(this@MainActivity2, "negative button")
                }
                .create()

            alertDialog.show()
        }
    }

    private fun setCustomAlertDialog(button: Button) {
        button.setOnClickListener {
            showSuccessDialog()
        }
    }

    private fun showSuccessDialog() {
        val successDialogParentLayout = findViewById<ConstraintLayout>(R.id.successDialogParentLayout)
        println("successDialogParentLayout: $successDialogParentLayout") // It's "null" because un-related layout file
//        val view = LayoutInflater.from(this).inflate(R.layout.success_dialog, successDialogParentLayout, false)
//        val alertDialog = AlertDialog.Builder(this)
//            .setView(view)
//            .create()

//        view.findViewById<Button>(R.id.doneBtn).setOnClickListener {
//            if (alertDialog.isShowing)
//                alertDialog.dismiss()
//        }

        val successDialogBinding = SuccessDialogBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this)
            .setView(successDialogBinding.root)
            .create()

        // SET THE PARENT LAYOUT BACKGROUND TRANSPARENT
        alertDialog.window?.setBackgroundDrawable(0.toDrawable()) // 0 - TRANSPARENT

        // CLICK LISTENER FOR DONE BUTTON
        successDialogBinding.doneBtn.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
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

}