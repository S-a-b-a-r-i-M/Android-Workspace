package com.example.firstapplication

import android.Manifest
import android.content.Intent
import androidx.core.net.toUri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.example.firstapplication.lean_ui_automation.DialerActivity
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DialerTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(DialerActivity::class.java)

    @get:Rule
    val grantPermission: GrantPermissionRule? = GrantPermissionRule.grant(
        Manifest.permission.CALL_PHONE
    )

    @Before
    fun setup() {
        init()
    }

    @After
    fun tearDown() {
        // Clears intent state, must be called after each test case
        release()
    }

    @Test
    fun makeCallWithValidPhone() {
        val validPhoneNumber = "9876543210"
        val phoneUri = "tel:$validPhoneNumber".toUri()

        // Enter Phone
        onView(withHint("phone no"))
            .perform(typeText(validPhoneNumber), closeSoftKeyboard())

        // Click Dial Button
        onView(withId(R.id.button2))
            .perform(click())

        // Verify
        intended(
            allOf(
                hasAction(Intent.ACTION_CALL),
                hasData(phoneUri)
            )
        )
    }

}