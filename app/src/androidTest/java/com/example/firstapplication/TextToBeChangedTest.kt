package com.example.firstapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import com.example.firstapplication.lean_ui_automation.UiAutomationFirstActivity
import retrofit2.SkipCallbackExecutor


@RunWith(AndroidJUnit4::class)
class TextToBeChangedTest {
    val stringToBeTyped = "Ui Automation Testing"

    @get:Rule
    val activityRule = ActivityScenarioRule(UiAutomationFirstActivity::class.java)

    @Test
    fun changeTextInSameActivity() {
        // Type Text and Press Button1
        onView(withId(R.id.etInput)) // Returns - ViewInteraction
            .perform(typeText(stringToBeTyped), closeSoftKeyboard() )
        onView(withId(R.id.btnChangeText))
            .perform(click())

        // Check Text Changed
        onView(withId(R.id.tvToBeChanged))
            .check(matches(withText(stringToBeTyped)))
    }

    @Test
    fun changeTextInNewActivity() {
        // Type Text and Press Button2
        onView(withId(R.id.etInput))
            .perform(typeText(stringToBeTyped), closeSoftKeyboard())
        onView(withId(R.id.btnOpenNextPage))
            .perform(click())

        // Check Text Changed
        // TODO-NOTE: We don't need to tell about the newly launched activity to Espresso.
        onView(withId(R.id.textView))
            .check(matches(withText(stringToBeTyped)))
    }

}