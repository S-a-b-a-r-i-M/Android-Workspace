package com.example.firstapplication.architectures.mvp.presentation.view

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstapplication.R
import com.example.firstapplication.architectures.mvp.data.database.DatabaseHelper
import com.example.firstapplication.architectures.mvp.data.model.User
import com.example.firstapplication.architectures.mvp.data.model.UserDaoImpl
import com.example.firstapplication.architectures.mvp.data.repo.UserRepositoryImpl
import com.example.firstapplication.architectures.mvp.presentation.UserContract
import com.example.firstapplication.architectures.mvp.presentation.presenter.UserPresenter
import com.example.firstapplication.databinding.ActivityUserBinding
import cutomutils.customToast

/**
 * Activity implementing the View interface
 * Handles all UI-related operations and user interactions
 * Contains Android-specific code
 */
class UserActivity : AppCompatActivity(), UserContract.View {
    // UI Components
    private lateinit var binding: ActivityUserBinding

    // Presenter
    private lateinit var presenter: UserPresenter

    // Progress dialog for loading states
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialize()
//        presenter.saveUser("sabari", "sabari@gmail.com")
        presenter.loadUser(1)
    }

    private fun initialize() {
        val userDao = UserDaoImpl(DatabaseHelper.getInstance(this))
        val repo = UserRepositoryImpl(userDao)
        presenter = UserPresenter(repo)

        // Attach this view to presenter
        presenter.attachView(this)

        progressDialog = ProgressDialog(this)
    }

    override fun showUser(user: User) {
        binding.userView.text = user.toString()
    }

    override fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressDialog = ProgressDialog.show(
                this,
                "Loading",
                "wait a sec...",
                true,
                false
            )
        } else {
            progressDialog?.dismiss()
            progressDialog = null
        }
    }

    override fun showError(message: String) {
        customToast(this, message)
    }

    override fun showSuccess(message: String) {
        customToast(this, message)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}