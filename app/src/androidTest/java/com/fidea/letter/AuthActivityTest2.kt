package com.fidea.letter


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AuthActivityTest2 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(AuthActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )

    @Test
    fun authActivityTest2() {
        val appCompatEditText = onView(
            allOf(
                withId(R.id.editTextEmail),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.textInputEmail),
                        0
                    ),
                    0
                )
            )
        )
        appCompatEditText.perform(scrollTo(), replaceText("admin"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.editTextPassword),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.textInputPassword),
                        0
                    ),
                    0
                )
            )
        )
        appCompatEditText2.perform(scrollTo(), replaceText("admin"), closeSoftKeyboard())

        val circularProgressButton = onView(
            allOf(
                withId(R.id.cirLoginButton), withText("ورود"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        circularProgressButton.perform(scrollTo(), click())

        val circularProgressButton2 = onView(
            allOf(
                withId(R.id.cirLoginButton), withText("ورود"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        circularProgressButton2.perform(scrollTo(), click())

        val circularProgressButton3 = onView(
            allOf(
                withId(R.id.cirLoginButton), withText("ورود"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        circularProgressButton3.perform(scrollTo(), click())

        val circularProgressButton4 = onView(
            allOf(
                withId(R.id.cirLoginButton), withText("ورود"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        circularProgressButton4.perform(scrollTo(), click())

        val circularProgressButton5 = onView(
            allOf(
                withId(R.id.cirLoginButton), withText("ورود"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        circularProgressButton5.perform(scrollTo(), click())

        val circularProgressButton6 = onView(
            allOf(
                withId(R.id.cirLoginButton), withText("ورود"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        circularProgressButton6.perform(scrollTo(), click())

        val circularProgressButton7 = onView(
            allOf(
                withId(R.id.cirLoginButton), withText("ورود"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        circularProgressButton7.perform(scrollTo(), click())

        val circularProgressButton8 = onView(
            allOf(
                withId(R.id.cirLoginButton), withText("ورود"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        circularProgressButton8.perform(scrollTo(), click())

        val appCompatImageView = onView(
            allOf(
                withId(R.id.image_gotoRegister),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        val appCompatTextView = onView(
            allOf(
                withId(R.id.gotoLogin), withText("قبلا عضو شده اید؟"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.cardview.widget.CardView")),
                        0
                    ),
                    5
                )
            )
        )
        appCompatTextView.perform(scrollTo(), click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
