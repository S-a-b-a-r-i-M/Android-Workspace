package com.example.firstapplication

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.firstapplication.lean_ui_automation.UiAutomationFirstActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IdlingTextToBeChangedTest {
    private val stringToBeTyped = "Ui Automation Testing"
    private var mIdlingResource: IdlingResource? = null

    /**
     * Before a test executes, we get idling resource from activity and register it into the IdlingRegistry
     */
    @Before
    fun registerIdlingResource() {
        val activityScenario = ActivityScenario.launch(UiAutomationFirstActivity::class.java)

        // activityScenario.onActivity provides a thread safe mechanism to access the activity
        // We pass the activity as a lambda and then register it into idling registry
        activityScenario.onActivity { activity ->
            mIdlingResource = activity.getIdlingResource()
            IdlingRegistry.getInstance().register(mIdlingResource)
        }
    }

    @Test
    fun changeText_WithBackgroundProcess() {
        // Type Text and Press Button1
        onView(withId(R.id.etInput2)) // Returns - ViewInteraction
            .perform(typeText(stringToBeTyped), closeSoftKeyboard())
        onView(withId(R.id.btnChangeText2))
            .perform(click())

        // Check Text Changed
        onView(withId(R.id.tvToBeChanged2))
            .check(matches(withText(stringToBeTyped)))
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null)
            IdlingRegistry.getInstance().unregister(mIdlingResource)
    }
}