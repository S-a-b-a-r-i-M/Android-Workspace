package com.example.firstapplication.lean_ui_automation

import androidx.test.espresso.IdlingResource
import cutomutils.simpleClassName
import java.util.concurrent.atomic.AtomicBoolean

class CustomIdlingResource : IdlingResource {
    private var mCallBack: IdlingResource.ResourceCallback? = null
    // Idleness is controlled with this boolean.
    private val mIsIdleNow = AtomicBoolean(true);

    override fun getName() = this.simpleClassName

    override fun isIdleNow() = mIsIdleNow.get()

    fun setIdleState(idIdleNow: Boolean) {
        mIsIdleNow.set(idIdleNow)
        if (isIdleNow)
            mCallBack?.onTransitionToIdle()
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        mCallBack = callback
    }
}