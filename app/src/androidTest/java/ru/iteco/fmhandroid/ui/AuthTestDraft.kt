package ru.iteco.fmhandroid.ui

import android.view.View
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.junit.*
import org.junit.runner.RunWith
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.page.LoginPage
import ru.iteco.fmhandroid.page.MainPage
import ru.iteco.fmhandroid.utils.Wait.forAnyDisplayed

@LargeTest
@RunWith(AndroidJUnit4::class)
class AuthTestDraft {

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
    fun tc001_successfulLoginShowsMainScreen() {
        forAnyDisplayed(withHint("Login"))
        LoginPage.assertOnScreen()
        LoginPage.typeLogin("login2")
        LoginPage.typePassword("password2")
        LoginPage.tapSignIn()

        MainPage.assertOpened()
    }


    @Test
    fun tc002_loginWithInvalidCredentialsShowsToast() {
        LoginPage.assertOnScreen()
        LoginPage.typeLogin("login")
        LoginPage.typePassword("pass")
        LoginPage.tapSignIn()

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val toastText = "Something went wrong. Try again later."

        val toast = device.findObject(UiSelector().text(toastText))

        Assert.assertTrue("Toast not found!", toast.exists())

        onView(withHint("Login")).check(matches(isDisplayed()))
    }

//    @Test
//    fun tc003_signInRefusedShowsToastWhenFieldsEmpty() {
//        LoginPage.assertOnScreen()
//        onView(withHint("Login")).perform(replaceText(""), closeSoftKeyboard())
//        onView(withHint("Password")).perform(replaceText(""), closeSoftKeyboard())
//        LoginPage.tapSignIn()
//
//        forToastDisplayed(
//            text(R.string.empty_login_or_password),
//            ToastMatcher(decorView),
//            timeoutMs = Wait.TIMEOUT_LONG
//        )
//
//        onView(withHint("Login")).check(matches(isDisplayed()))
//    }

    @Test
    fun tc012_logout_returnsToLogin() {
        LoginPage.assertOnScreen()
        LoginPage.typeLogin("login2")
        LoginPage.typePassword("password2")
        LoginPage.tapSignIn()
        MainPage.assertOpened()

        MainPage.logout()

        onView(withHint("Login")).check(matches(isDisplayed()))
    }

    @Test
    fun tc017_reloginAfterLogout_works() {
        LoginPage.assertOnScreen()
        LoginPage.typeLogin("login2")
        LoginPage.typePassword("password2")
        LoginPage.tapSignIn()
        MainPage.assertOpened()

        MainPage.logout()
        onView(withHint("Login")).check(matches(isDisplayed()))

        LoginPage.typeLogin("login2")
        LoginPage.typePassword("password2")
        LoginPage.tapSignIn()
        MainPage.assertOpened()
    }
}