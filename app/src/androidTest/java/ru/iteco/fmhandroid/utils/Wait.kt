package ru.iteco.fmhandroid.utils

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingRootException
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.Root
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import ru.iteco.fmhandroid.utils.Wait.TIMEOUT_SHORT
import java.util.concurrent.TimeoutException

object Wait {
    const val TIMEOUT_SHORT: Long = 5_000L
    fun forAnyDisplayed(vararg matchers: Matcher<View>, timeoutMs: Long = TIMEOUT_SHORT) {
        val start = System.currentTimeMillis()
        do {
            for (m in matchers) {
                try {
                    onView(m).check(matches(isDisplayed()))
                    return
                } catch (_: Throwable) {
                }
            }
            Thread.sleep(100)
        } while (System.currentTimeMillis() - start < timeoutMs)
        for (m in matchers) onView(m).check(matches(isDisplayed()))
    }

//    fun waitForElement(matcher: Matcher<View>, timeout: Long = 5000): ViewAction {
//        return object : ViewAction {
//            override fun getConstraints(): Matcher<View> {
//                return isRoot()
//            }
//
//            override fun getDescription(): String {
//                return "wait for element matching $matcher for $timeout milliseconds"
//            }
//
//            override fun perform(uiController: UiController, view: View) {
//                val endTime = System.currentTimeMillis() + timeout
//                do {
//                    if (hasMatchingView(view, matcher)) {
//                        return
//                    }
//                    uiController.loopMainThreadForAtLeast(100)
//                } while (System.currentTimeMillis() < endTime)
//
//                throw TimeoutException("Timeout waiting for view matching $matcher") as Throwable
//            }

            private fun hasMatchingView(root: View, matcher: Matcher<View>): Boolean {
                return try {
                    if (matcher.matches(root)) return true
                    if (root is ViewGroup) {
                        for (i in 0 until root.childCount) {
                            if (hasMatchingView(root.getChildAt(i), matcher)) return true
                        }
                    }
                    false
                } catch (e: Exception) {
                    false
                }
            }

    fun forToastDisplayed(
        textMatcher: Matcher<View>,
        rootMatcher: Matcher<Root>,
        timeoutMs: Long = TIMEOUT_SHORT
    ) {
        val start = System.currentTimeMillis()
        do {
            try {
                onView(textMatcher)
                    .inRoot(rootMatcher)
                    .check(matches(isDisplayed()))
                return
            } catch (_: NoMatchingRootException) {
            } catch (_: NoMatchingViewException) {
            } catch (_: Throwable) {
            }
            Thread.sleep(50)
        } while (System.currentTimeMillis() - start < timeoutMs)

        onView(textMatcher)
            .inRoot(rootMatcher)
            .check(matches(isDisplayed()))
    }
}


