package com.example.firstapplication

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapplication.kt_java.ToastDemo
import com.example.firstapplication.kt_java.ToastDemoJ

/*
 Use ComponentActivity for Compose,
 AppCompatActivity for traditional Views or when you need ActionBar/Material Design features.
 */
class MainActivity : StackInfoAppCompactActivity() {
    private val toastDemoKt = ToastDemo(this)
    private val toastDemoJ = ToastDemoJ(this)
    private var count = 0 // While rotating mobile you can notice this state value is not persisting

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { it ->
        if (it.resultCode == RESULT_OK && it.data != null) {
            val name: String? = it.data?.getStringExtra("name") ?: ""
            customToast("Welcome back $name 🥳")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("MainActivity","onCreate.....-------------->")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main1)

        applicationContext
        this
        baseContext

      // TOAST
        val btn: View = findViewById(R.id.toastBtn2)
        setLongToastKt(btn) // KT
//        setLongToastJ(btn) // J

      // TOGGLE
        val toggleButton: ToggleButton = findViewById(R.id.toggleButton)
        toggleClick(toggleButton)

      // COUNTER
        val incButton: Button = findViewById(R.id.incCountBtn)
        incrementCounter(incButton)

      // CHECK BOX
        setCheckBoxListener(findViewById(R.id.pooriCB))
        setCheckBoxListener(findViewById(R.id.dosaCB))
        setCheckBoxListener(findViewById(R.id.parottaCB))

        val placeOrderBtn: Button = findViewById(R.id.placeOrderBtn)
        placeOrderUsingCheckBox(placeOrderBtn)

      // RADIO BUTTON
        setRadioButtonListener(findViewById(R.id.maleRB))
        setRadioButtonListener(findViewById(R.id.femaleRB))
        val radioGroup: RadioGroup = findViewById(R.id.genderRGroup)
        val saveGenderButton: Button = findViewById(R.id.saveGenderBtn)
        setGenderSelection(saveGenderButton, radioGroup)
    }

    // Life Cycle
    override fun onStart() {
        Log.i("MainActivity","onStart.....-------------->")
        super.onStart()
    }

    override fun onResume() {
        Log.i("MainActivity","onResume.....-------------->")
        super.onResume()
    }

    override fun onPause() {
        Log.i("MainActivity","onPause.....-------------->")
        super.onPause()
    }

    override fun onStop() {
        Log.i("MainActivity","onStop.....-------------->")
        super.onStop()
    }

    override fun onRestart() {
        Log.i("MainActivity","onRestart.....-------------->")
        super.onRestart()
    }

    override fun onDestroy() {
        Log.i("MainActivity","onDestroy.....-------------->")
        super.onDestroy()
    }

    // SAVING & RESTORING DATA ON CONFIG CHANGES
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.i("MainActivity", "onSaveInstanceState.....-------------->")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("MainActivity", "onRestoreInstanceState.....-------------->")
    }

    // Counter
    private fun incrementCounter(button: Button) {
        val tv: TextView = findViewById(R.id.countTV)
        button.setOnClickListener {
            tv.text = (++count).toString()
        }
    }

    // Toast
    fun setShortToastKt(view: View) {
        toastDemoKt.setShortToast(view)
        findViewById<Button>(R.id.toastBtn2)
            .setBackgroundColor(getColor(R.color.primary_button_cfe))
        findViewById<Button>(R.id.toastBtn1).setBackgroundColor(getColor(R.color.light_gray))
    }

    private fun setLongToastKt(view: View) {
        toastDemoKt.setLongToast(view) {
            findViewById<Button>(R.id.toastBtn2).setBackgroundColor(getColor(R.color.light_gray))
            findViewById<Button>(R.id.toastBtn1)
                .setBackgroundColor(getColor(R.color.primary_button_cfe))
        }
    }

    fun setShortToastJ(view: View) {
        toastDemoJ.setShortToast(view)
    }

    private fun setLongToastJ(view: View) {
        toastDemoJ.setLongToast(view)
    }

    private fun customToast(message: String, durationTime: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, durationTime).show()
    }

    fun saveInput(view: View) {
        if (view.id == R.id.saveNoteBtn) {
            val et: EditText = findViewById(R.id.editText)
            val tv: TextView = findViewById(R.id.InputDisplayTV)
            val input = et.text.toString().trim()
            if (input.isNotBlank()) {
                tv.text = "Hello!! $input"
                et.text.clear()
            }
        }
    }

  // TOGGLE
    private fun toggleClick(toggleBtn: ToggleButton) {
        val tv: TextView = findViewById(R.id.toggleStausTV)
        tv.text = toggleBtn.text // For initial load
        toggleBtn.setOnClickListener {
            tv.text = toggleBtn.text
        }
    }

  // CHECK BOX
    private fun placeOrderUsingCheckBox(placeOrderBtn: Button) {
        val prices = mutableMapOf(
            "poori" to 35,
            "dosa" to 25,
            "parotta" to 35
        )
        // Fetch All Check Boxes
        val poori: CheckBox = findViewById(R.id.pooriCB)
        val dosa: CheckBox = findViewById(R.id.dosaCB)
        val parotta: CheckBox = findViewById(R.id.parottaCB)

        placeOrderBtn.setOnClickListener {
            val orders: StringBuilder = StringBuilder("Orders:")
            var amount = 0
            var price: Int

            if (poori.isChecked) {
                price = prices.getValue("poori")
                orders.append(" Poori: $price")
                amount += price
            }
            if (dosa.isChecked) {
                price = prices.getValue("dosa")
                orders.append(" Dosa: $price")
                amount += price
            }
            if (parotta.isChecked) {
                price = prices.getValue("parotta")
                orders.append(" Parotta: $price")
                amount += price
            }

            Toast(this).apply {
                duration = Toast.LENGTH_SHORT
                setText( if (amount > 0) "$orders. Amount: $amount" else "Please select any items...")
            }.show()
        }
    }

    private fun setCheckBoxListener(checkBox: CheckBox) {
        checkBox.setOnCheckedChangeListener { button, result ->
            customToast("${checkBox.text} is ${if(result) "checked" else "unchecked"}")
        }
    }

  // RADIO GROUP & BUTTON
    private fun setGenderSelection(saveButton: Button, radioGroup: RadioGroup) {
        saveButton.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId == -1) {
                customToast("Select an option...")
                return@setOnClickListener
            }

            val radioButton: RadioButton = findViewById(selectedId)
            customToast("Gander ${radioButton.text} is saved...")
        }
    }

    private fun setRadioButtonListener(radioButton: RadioButton) {
        radioButton.setOnCheckedChangeListener { button, isSelected ->
            customToast("${button.text} is ${if(isSelected) "selected" else "un-selected"}")
        }
    }

//    override fun onActivityResult(
//        requestCode: Int,
//        resultCode: Int,
//        data: Intent?,
//        caller: ComponentCaller
//    ) {
//        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
//            val name: String? = data.getStringExtra("name")
//            customToast("Welcome back $name 🥳")
//        }
//    }
}

open class StackInfoAppCompactActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
//        printActivityStack()
        printFragmentBackStack()
    }

    fun printActivityStack() {
        val activityManger = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val taskInfo = activityManger.getRunningTasks(1).firstOrNull()

        Log.d("ActivityStack", "Current Task: ${taskInfo?.topActivity?.className}")
        Log.d("ActivityStack", "Base Activity: ${taskInfo?.baseActivity?.className}")
        Log.d("ActivityStack", "Num Activities: ${taskInfo?.numActivities}")
    }

    fun printFragmentBackStack() {
        val backStackCount = supportFragmentManager.backStackEntryCount

        Log.d("BackStack", "Fragment BackStack Count: $backStackCount")
        for (i in 0 until backStackCount) {
            val backStackEntry = supportFragmentManager.getBackStackEntryAt(i)
            Log.d("BackStack", "[$i] Name: ${backStackEntry.name}, ID: ${backStackEntry.id}")
        }
    }
}

open class LifeCycleInfoAppCompactActivity : AppCompatActivity() {
    // Life Cycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(this.localClassName,"onCreate.....-------------->")
    }

    override fun onStart() {
        super.onStart()
        Log.i(this.localClassName,"onStart.....-------------->")
    }

    override fun onResume() {
        super.onResume()
        Log.i(this.localClassName,"onResume.....-------------->")
    }

    override fun onPause() {
        super.onPause()
        Log.i(this.localClassName,"onPause.....-------------->")
    }

    override fun onStop() {
        super.onStop()
        Log.i(this.localClassName,"onStop.....-------------->")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(this.localClassName,"onRestart.....-------------->")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(this.localClassName,"onDestroy.....-------------->")
    }
}