package ru.iteco.fmhandroid.page

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.utils.Wait.forAnyDisplayed

object MainPage {
    fun assertOpened(timeoutMs: Long = 10_000) {
        forAnyDisplayed(withId(R.id.all_news_text_view), timeoutMs = timeoutMs)
        onView(withId(R.id.all_news_text_view)).check(matches(isDisplayed()))
    }

    fun logout() {
        onView(withId(R.id.authorization_image_button)).perform(click())
        onView(withText("Log out")).perform(click())
    }
}