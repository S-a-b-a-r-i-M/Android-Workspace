package com.example.firstapplication.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.R
import com.example.firstapplication.databinding.ActivityLoginBinding
import com.example.firstapplication.setGotoTargetPage
import cutomutils.customToast

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dataBaseHelper: DataBaseHelper
    private lateinit var userDAO: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dataBaseHelper = DataBaseHelper(this)
        userDAO = UserDAO(dataBaseHelper)

        binding.logInBtn.setOnClickListener {
            val email = binding.emailET.text.toString().trim()
            val password = binding.passwordET.text.toString().trim()

            // TODO: fix it
            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (getUserByEmail(email)) {
                    customToast(this, "LogIn Successful ✅")
//                    finish()
                }
                else
                    customToast(this, "LogIn Failed 👎")
            } else
                customToast(this, "email or password is empty")
        }

        // NEW USER TO SIGNUP PAGE
        binding.newUserET.setGotoTargetPage(SignUpActivity::class.java)
    }

    private fun getUserByEmail(email: String) = userDAO.getUserByEmail(email)

    override fun onDestroy() {
        super.onDestroy()
        dataBaseHelper.close()
    }
}