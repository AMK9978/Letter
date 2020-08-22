package com.fidea.letter.ui.main.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import com.fidea.letter.MainActivity
import com.fidea.letter.R
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HomeFragmentEspressoTest {


    @Rule
    var mActivityRule = (MainActivity::class.java)

    @Test
    fun testToolbarSearch() {
        onView(withId(R.id.toolbar)).perform(click()).check(matches(not(isEnabled())))
    }

    @Test
    fun onScroll() {
        onView(withId(R.id.contentRecycler)).perform(swipeUp()).check(matches(isEnabled()))
    }
}