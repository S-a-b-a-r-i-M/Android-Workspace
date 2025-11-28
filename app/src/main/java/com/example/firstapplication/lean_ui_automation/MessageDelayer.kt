package com.example.firstapplication.lean_ui_automation

import android.os.Handler

class MessageDelayer {
    val DELAY_MILLIS = 3000L;

    // Will Be Implemented By The Child Class
    interface DelayerCallBack {
        fun onDone(message: String)
    }

    /**
     * Takes a String and returns it after DELAY_MILLIS via a DelayerCallback.
     */
    fun processMessage(
        message: String,
        delayerCallBack: DelayerCallBack,
        idlingResource: CustomIdlingResource?
    ) {
        // If an IdlingResource is provided (only during instrumented tests),
        // mark the app as "busy". This tells Espresso to WAIT until we
        // explicitly set the idle state back to true.
        //
        // In production, idlingResource will be null and this line is skipped.
        idlingResource?.setIdleState(false)

        // Create a Handler to post a delayed task on the main thread.
        // This simulates an asynchronous operation (e.g., network call delay).
        val handler = Handler()

        // Post a task that runs after DELAY_MILLIS.
        handler.postDelayed({
            // Notify the caller that the message processing is done.
            delayerCallBack.onDone(message)

            // Mark the resource as idle again — telling Espresso that the
            // asynchronous work has completed, so it can continue the test.
            idlingResource?.setIdleState(true)
        }, DELAY_MILLIS)
    }

}