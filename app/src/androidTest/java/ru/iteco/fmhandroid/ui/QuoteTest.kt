package ru.iteco.fmhandroid.ui

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import io.qameta.allure.kotlin.Allure
import io.qameta.allure.kotlin.Epic
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.Story
import io.qameta.allure.kotlin.Description
import io.qameta.allure.kotlin.Severity
import io.qameta.allure.kotlin.SeverityLevel
import io.qameta.allure.kotlin.junit4.DisplayName
import org.hamcrest.Matchers.allOf
import org.junit.*
import org.junit.runner.RunWith
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.page.LoginPage
import ru.iteco.fmhandroid.page.MainPage
import ru.iteco.fmhandroid.utils.Wait
import ru.iteco.fmhandroid.utils.Wait.forAnyDisplayed
import ru.iteco.fmhandroid.utils.nthChildOf

@LargeTest
@Epic("Раздел «Цитаты / Наша миссия»")
@Feature("Просмотр списка и деталей цитат")
@RunWith(AndroidJUnit4::class)
class QuoteTest {

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
            timeoutMs = Wait.TIMEOUT_LONG
        )
        try {
            onView(withHint("Login")).check(matches(isDisplayed()))
            return
        } catch (_: NoMatchingViewException) {
        }

        MainPage.logout()

        forAnyDisplayed(withHint("Login"), timeoutMs = Wait.TIMEOUT_LONG)
    }

    @After
    fun logout() {
        MainPage.logout()
    }

    @Test
    @Story("Открытие списка цитат")
    @DisplayName("TC‑011: Открытие раздела «Наша миссия» показывает список")
    @Description("Пользователь заходит в приложение, открывает раздел «Наша миссия» (Quotes) и видит заголовок и список карточек.")
    @Severity(SeverityLevel.NORMAL)
    fun tc011_openQuotesFromMain() {
        Allure.step("Авторизация") {
            LoginPage.assertOnScreen()
            LoginPage.typeLogin("login2")
            LoginPage.typePassword("password2")
            LoginPage.tapSignIn()
            MainPage.assertOpened()
        }
        Allure.step("Переход к разделу «Наша миссия»") {
            MainPage.openQuotes()
        }
        Allure.step("Проверка отображения заголовка и списка") {
            onView(withId(R.id.our_mission_title_text_view))
                .check(matches(isDisplayed()))
            onView(withId(R.id.our_mission_item_list_recycler_view))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    @Story("Открытие карточки цитаты")
    @DisplayName("TC‑022: Открытие карточки цитаты показывает описание")
    @Description("Пользователь открывает первую карточку цитаты в списке и видит описание внутри.")
    @Severity(SeverityLevel.NORMAL)
    fun tc022_openQuoteCard() {
        Allure.step("Авторизация") {
            LoginPage.assertOnScreen()
            LoginPage.typeLogin("login2")
            LoginPage.typePassword("password2")
            LoginPage.tapSignIn()
            MainPage.assertOpened()
        }
        Allure.step("Переход к разделу «Наша миссия»") {
            MainPage.openQuotes()
        }
        Allure.step("Нажатие на первую карточку") {
            onView(
                allOf(
                    withId(R.id.our_mission_item_open_card_image_button),
                    isDescendantOfA(nthChildOf(withId(R.id.our_mission_item_list_recycler_view), 0)),
                    isDisplayed()
                )
            ).perform(click())
        }
        Allure.step("Проверка отображения текста описания") {
            onView(
                allOf(
                    withId(R.id.our_mission_item_description_text_view),
                    isDisplayed()
                )
            ).check(matches(isDisplayed()))
        }
    }
}





//import android.view.View
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.NoMatchingViewException
//import androidx.test.espresso.action.ViewActions.click
//import androidx.test.espresso.assertion.ViewAssertions.matches
//import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
//import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
//import androidx.test.espresso.matcher.ViewMatchers.withHint
//import androidx.test.espresso.matcher.ViewMatchers.withId
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.filters.LargeTest
//import org.hamcrest.Matchers.allOf
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import ru.iteco.fmhandroid.R
//import ru.iteco.fmhandroid.page.LoginPage
//import ru.iteco.fmhandroid.page.MainPage
//import ru.iteco.fmhandroid.ui.AppActivity
//import ru.iteco.fmhandroid.utils.Wait
//import ru.iteco.fmhandroid.utils.Wait.forAnyDisplayed
//import ru.iteco.fmhandroid.utils.nthChildOf
//
//@LargeTest
//@RunWith(AndroidJUnit4::class)
//class QuoteTestTest {
//
//    @Rule
//    @JvmField
//    var activityRule = ActivityScenarioRule(AppActivity::class.java)
//
//    private lateinit var decorView: View
//
//    @Before
//    fun ensureLoggedOut() {
//        activityRule.scenario.onActivity { decorView = it.window.decorView }
//
//        forAnyDisplayed(
//            withHint("Login"),
//            withId(R.id.authorization_image_button),
//            timeoutMs = Wait.TIMEOUT_LONG
//        )
//        try {
//            onView(withHint("Login")).check(matches(isDisplayed()))
//            return
//        } catch (_: NoMatchingViewException) {
//        }
//
//        MainPage.logout()
//
//        forAnyDisplayed(withHint("Login"), timeoutMs = Wait.TIMEOUT_LONG)
//    }
//
//    //    Не уверен, не излишество ли это, при условии, что @Before делает то же самое
//    @After
//    fun logout() {
//        MainPage.logout()
//    }
//
//    @Test
//    fun tc011_openQuotesFromMain() {
//        LoginPage.assertOnScreen()
//
//        LoginPage.typeLogin("login2")
//        LoginPage.typePassword("password2")
//        LoginPage.tapSignIn()
//        MainPage.assertOpened()
//
//        MainPage.openQuotes()
//        onView(withId(R.id.our_mission_title_text_view)).check(matches(isDisplayed()))
//        onView(withId(R.id.our_mission_item_list_recycler_view)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun tc022_openQuoteCard() {
//        LoginPage.assertOnScreen()
//
//        LoginPage.typeLogin("login2")
//        LoginPage.typePassword("password2")
//        LoginPage.tapSignIn()
//        MainPage.assertOpened()
//
//        MainPage.openQuotes()
//        onView(
//            allOf(
//                withId(R.id.our_mission_item_open_card_image_button),
//                isDescendantOfA(nthChildOf(withId(R.id.our_mission_item_list_recycler_view), 0))
//            )
//        ).perform(click())
//        onView(
//            allOf(
//                withId(R.id.our_mission_item_description_text_view),
//                isDisplayed()
//            )
//        ).check(matches(isDisplayed()))
//    }
//}