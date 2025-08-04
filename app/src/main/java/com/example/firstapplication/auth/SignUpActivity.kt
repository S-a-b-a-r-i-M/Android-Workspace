package com.example.firstapplication.auth

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.R
import com.example.firstapplication.databinding.ActivitySignUpBinding
import cutomutils.setGotoTargetPage
import cutomutils.customToast

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var dataBaseHelper: DataBaseHelper
    private lateinit var userDAO: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dataBaseHelper = DataBaseHelper(this)
        userDAO = UserDAO(dataBaseHelper)

        binding.signUpBtn.setOnClickListener {
            val userName = binding.userNameET.text.toString().trim()
            val email = binding.emailET.text.toString().trim()
            val password = binding.passwordET.text.toString().trim()

            if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val userId = createNewUser(userName, email, password)
                if (userId != -1L) {
                    Log.d("SignUpActivity", "New User id: $userId")
                    customToast(this, "SignUp Successful ✅")
                    // NAVIGATE TO HOME PAGE
//                    val intent = Intent(this, SignUpActivity::class.java)
//                    startActivity(intent)
//                    finish()
                }
                else
                    customToast(this, "SignUp Failed 👎")
            }
        }

        // EXISTING USER TO LOGIN PAGE
        binding.existingUserET.setGotoTargetPage(LoginActivity::class.java)
    }

    private fun createNewUser(userName: String, email: String, password: String): Long {
        return userDAO.createUser(userName, email, password)
    }

    override fun onDestroy() {
        super.onDestroy()
        dataBaseHelper.close() // RELEASE THE DB RESOURCES
    }
}