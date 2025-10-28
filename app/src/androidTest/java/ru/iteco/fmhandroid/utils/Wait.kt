package ru.iteco.fmhandroid.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider.getApplicationContext
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
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.Assert.fail
import ru.iteco.fmhandroid.utils.Wait.TIMEOUT_SHORT
import java.util.concurrent.TimeoutException

object Wait {
    const val TIMEOUT_SHORT: Long = 1500L
    const val TIMEOUT_LONG: Long = 10_000L

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

    fun forTextOnScreen(@StringRes resId: Int, timeoutMs: Long = TIMEOUT_SHORT) {
        val text = getApplicationContext<Context>().getString(resId)
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val ok = device.wait(Until.hasObject(By.text(text)), timeoutMs)
        if (!ok) fail("Expected transient text '$text' within ${timeoutMs}ms")
    }

}


