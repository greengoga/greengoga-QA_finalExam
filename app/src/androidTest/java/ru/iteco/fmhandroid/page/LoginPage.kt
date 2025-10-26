package ru.iteco.fmhandroid.page

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import ru.iteco.fmhandroid.R

object LoginPage {
    private const val H_LOGIN = "Login"
    private const val H_PASSWORD = "Password"
    private const val BTN_SIGN_IN = "Sign in"

    fun assertOnScreen() {
        onView(withHint(H_LOGIN)).check(matches(isDisplayed()))
    }

    fun typeLogin(text: String) {
        onView(withHint(H_LOGIN)).perform(click(), replaceText(text), closeSoftKeyboard())
    }

    fun typePassword(text: String) {
        onView(withHint(H_PASSWORD)).perform(click(), replaceText(text), closeSoftKeyboard())
    }

    fun tapSignIn() {
        onView(allOf(withId(R.id.enter_button), withText(BTN_SIGN_IN))).perform(click())
    }

//    fun `isSignInEnabled`(): Boolean {
//        var enabled = false
//        onView(allOf(withId(R.id.enter_button), withText("Sign in")))
//            .check { v, _ -> enabled = v?.isEnabled == true }
//        return enabled
//    }

//    fun tapSignInExpectStay() {
//        onView(allOf(withId(R.id.enter_button), withText("Sign in"))).perform(click())
//        onView(withHint("Login")).check(matches(isDisplayed()))
//    }
}