package ru.iteco.fmhandroid.ui

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.*
import org.junit.runner.RunWith
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.page.LoginPage
import ru.iteco.fmhandroid.page.MainPage
import ru.iteco.fmhandroid.utils.Wait.forAnyDisplayed

@LargeTest
@RunWith(AndroidJUnit4::class)
class AboutTest {

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(AppActivity::class.java)

    private lateinit var decorView: View

    @Before
    fun ensureLoggedOut() {
        activityRule.scenario.onActivity { decorView = it.window.decorView }

        forAnyDisplayed(
            withHint("Login"),
            withId(R.id.authorization_image_button),
            timeoutMs = ru.iteco.fmhandroid.utils.Wait.TIMEOUT_LONG
        )
        try {
            onView(withHint("Login")).check(matches(isDisplayed()))
            return
        } catch (_: NoMatchingViewException) {
        }

        MainPage.logout()

        forAnyDisplayed(withHint("Login"), timeoutMs = ru.iteco.fmhandroid.utils.Wait.TIMEOUT_LONG)
    }

    @Test
    fun tc009_opensAboutPage() {
        LoginPage.assertOnScreen()

        LoginPage.typeLogin("login2")
        LoginPage.typePassword("password2")
        LoginPage.tapSignIn()
        MainPage.assertOpened()

        MainPage.openAbout()

        onView(withId(R.id.about_version_title_text_view))
            .check(matches(withText("Version:")))

        onView(withId(R.id.about_privacy_policy_value_text_view))
            .check(matches(withText("https://vhospice.org/#/privacy-policy/")))

        onView(withId(R.id.about_terms_of_use_value_text_view))
            .check(matches(withText("https://vhospice.org/#/terms-of-use")))
    }
}
