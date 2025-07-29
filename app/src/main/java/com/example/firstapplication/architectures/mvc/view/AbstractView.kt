package com.example.firstapplication.architectures.mvc.view

import android.view.View

interface AbstractView {
    fun getRootView() : View

    fun initViews()

    fun bindDataToView()
}