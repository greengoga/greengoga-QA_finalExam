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

    fun openNewsFromMain() {
        onView(withId(R.id.all_news_text_view)).perform(click())
    }

    fun openNewsFromMenu() {
        onView(withId(R.id.main_menu_image_button)).perform(click())
        onView(withText("News")).perform(click())
    }

    fun openQuotes() {
        onView(withId(R.id.our_mission_image_button)).perform(click())
    }

    fun openControlPanel() {
        onView(withId(R.id.main_menu_image_button)).perform(click())
        onView(withText("News")).perform(click())
        onView(withId(R.id.edit_news_material_button)).perform(click())
    }

    fun openAbout() {
        onView(withId(R.id.main_menu_image_button)).perform(click())
        onView(withText("About")).perform(click())
    }
}