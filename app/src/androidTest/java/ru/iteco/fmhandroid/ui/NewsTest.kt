package ru.iteco.fmhandroid.ui

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
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
import org.junit.*
import org.junit.runner.RunWith
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.page.LoginPage
import ru.iteco.fmhandroid.page.MainPage
import ru.iteco.fmhandroid.utils.Wait
import ru.iteco.fmhandroid.utils.Wait.forAnyDisplayed

@LargeTest
@Epic("Раздел «Новости»")
@Feature("Открытие и проверка списка новостей")
@RunWith(AndroidJUnit4::class)
class NewsTest {

    @get:Rule
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
    @Story("Открытие всех новостей с главного экрана")
    @DisplayName("TC‑004: Открытие раздела «Новости» с главного экрана")
    @Description("Проверяется, что после входа пользователь может открыть раздел «Новости» и видит кнопки сортировки, фильтрации и редактирования.")
    @Severity(SeverityLevel.NORMAL)
    fun tc004_openAllNewsFromMain() {
        Allure.step("Авторизация") {
            LoginPage.assertOnScreen()
            LoginPage.typeLogin("login2")
            LoginPage.typePassword("password2")
            LoginPage.tapSignIn()
            MainPage.assertOpened()
        }
        Allure.step("Открытие раздела «Новости»") {
            MainPage.openNews()
        }
        Allure.step("Проверка элементов управления") {
            onView(withId(R.id.sort_news_material_button)).check(matches(isDisplayed()))
            onView(withId(R.id.filter_news_material_button)).check(matches(isDisplayed()))
            onView(withId(R.id.edit_news_material_button)).check(matches(isDisplayed()))
        }
    }

    @Test
    @Story("Открытие новостей через меню")
    @DisplayName("TC‑005: Открытие раздела «Новости» через меню")
    @Description("Проверяется, что пользователь может зайти в раздел «Новости» через меню навигации и видит те же элементы управления.")
    @Severity(SeverityLevel.NORMAL)
    fun tc005_openNewsFromMenu() {
        Allure.step("Авторизация") {
            LoginPage.assertOnScreen()
            LoginPage.typeLogin("login2")
            LoginPage.typePassword("password2")
            LoginPage.tapSignIn()
            MainPage.assertOpened()
        }
        Allure.step("Открытие раздела «Новости» через меню") {
            MainPage.openNewsMenuBtn()
        }
        Allure.step("Проверка элементов управления") {
            onView(withId(R.id.sort_news_material_button)).check(matches(isDisplayed()))
            onView(withId(R.id.filter_news_material_button)).check(matches(isDisplayed()))
            onView(withId(R.id.edit_news_material_button)).check(matches(isDisplayed()))
        }
    }

    @Test
    @Story("Проверка пустого списка новостей")
    @DisplayName("TC‑006: При отсутствии новостей отображается пустой список")
    @Description("Проверяется, что если в разделе «Новости» нет записей, отображаются иконка, текст уведомления и кнопка «Обновить».")
    @Severity(SeverityLevel.NORMAL)
    fun tc006_checkForEmptyNewsList() {
        Allure.step("Авторизация") {
            LoginPage.assertOnScreen()
            LoginPage.typeLogin("login2")
            LoginPage.typePassword("password2")
            LoginPage.tapSignIn()
            MainPage.assertOpened()
        }
        Allure.step("Открытие раздела «Новости»") {
            MainPage.openNewsMenuBtn()
        }
        Allure.step("Проверка состояния пустого списка") {
            onView(withId(R.id.empty_news_list_image_view)).check(matches(isDisplayed()))
            onView(withId(R.id.empty_news_list_text_view)).check(matches(isDisplayed()))
            onView(withId(R.id.news_retry_material_button)).check(matches(isDisplayed()))
        }
    }
}




//package ru.iteco.fmhandroid.ui
//
//
//import android.view.View
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.NoMatchingViewException
//import androidx.test.espresso.assertion.ViewAssertions.matches
//import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
//import androidx.test.espresso.matcher.ViewMatchers.withHint
//import androidx.test.espresso.matcher.ViewMatchers.withId
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.filters.LargeTest
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import ru.iteco.fmhandroid.R
//import ru.iteco.fmhandroid.page.LoginPage
//import ru.iteco.fmhandroid.page.MainPage
//import ru.iteco.fmhandroid.utils.Wait
//import ru.iteco.fmhandroid.utils.Wait.forAnyDisplayed
//
//@LargeTest
//@RunWith(AndroidJUnit4::class)
//class NewsTest {
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
//    //Не уверен, не излишество ли это, при условии, что @Before делает то же самое
//    @After
//    fun logout() {
//        MainPage.logout()
//    }
//
//    @Test
//    fun tc004_openAllNewsFromMain() {
//        LoginPage.assertOnScreen()
//
//        LoginPage.typeLogin("login2")
//        LoginPage.typePassword("password2")
//        LoginPage.tapSignIn()
//        MainPage.assertOpened()
//
//        MainPage.openNews()
//        onView(withId(R.id.sort_news_material_button)).check(matches(isDisplayed()))
//        onView(withId(R.id.filter_news_material_button)).check(matches(isDisplayed()))
//        onView(withId(R.id.edit_news_material_button)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun tc005_openNewsFromMenu() {
//        LoginPage.assertOnScreen()
//
//        LoginPage.typeLogin("login2")
//        LoginPage.typePassword("password2")
//        LoginPage.tapSignIn()
//        MainPage.assertOpened()
//
//        MainPage.openNewsMenuBtn()
//        onView(withId(R.id.sort_news_material_button)).check(matches(isDisplayed()))
//        onView(withId(R.id.filter_news_material_button)).check(matches(isDisplayed()))
//        onView(withId(R.id.edit_news_material_button)).check(matches(isDisplayed()))
//    }
//
//    @Test
//    fun tc006_checkForEmptyNewsList() {
//        LoginPage.assertOnScreen()
//
//        LoginPage.typeLogin("login2")
//        LoginPage.typePassword("password2")
//        LoginPage.tapSignIn()
//        MainPage.assertOpened()
//
//        MainPage.openNewsMenuBtn()
//        onView(withId(R.id.empty_news_list_image_view)).check(matches(isDisplayed()))
//        onView(withId(R.id.empty_news_list_text_view)).check(matches(isDisplayed()))
//        onView(withId(R.id.news_retry_material_button)).check(matches(isDisplayed()))
//    }
//}
