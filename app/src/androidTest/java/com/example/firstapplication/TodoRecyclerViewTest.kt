package com.example.firstapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoRecyclerViewTest {
    // TODO-NOTE: If we include more than one ActivityScenarioRule the last Rule Screen will be visible at last(homeActivityRule(open and close) -> todoActivityRule(open and visible))
    /*
    @get:Rule
    val homeActivityRule = ActivityScenarioRule(HomePageActivity::class.java) */
    //
    @get:Rule
    val todoActivityRule = ActivityScenarioRule(TodoRecyclerViewActivity::class.java)

    //@Test(expected = NoMatchingViewException::class)
    @Test
    fun withText_doesNotExist() {
        assertThrows(NoMatchingViewException::class.java) {
            onView(withText("not exists")).perform(click())
        }
    }

    @Test
    fun existingItemWithText_markDone() {
        // Attempt to scroll to an item that contains the special text.
        onView(withId(R.id.recyclerView2))
            .perform(
                RecyclerViewActions.scrollTo<TodoAdapter.ViewHolder>(
                    hasDescendant(
                        withText("Learn Tab Creation")
                    )
                )
            ).check(matches(isCompletelyDisplayed()))
    }
}