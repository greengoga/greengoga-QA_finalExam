package ru.iteco.fmhandroid.ui

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
import ru.iteco.fmhandroid.utils.TestData
import ru.iteco.fmhandroid.utils.Wait
import ru.iteco.fmhandroid.utils.Wait.forAnyDisplayed

@LargeTest
@Epic("Навигация приложения")
@Feature("Страница «О приложении»")
@RunWith(AndroidJUnit4::class)
class AboutTest {

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(AppActivity::class.java)

    @Before
    fun ensureLoggedIn() {
        forAnyDisplayed(
            withHint("Login"),
            withId(R.id.authorization_image_button),
            timeoutMs = Wait.TIMEOUT_LONG
        )
        try {
            onView(withHint("Login")).check(matches(isDisplayed()))
            LoginPage.typeLogin(TestData.LOGIN)
            LoginPage.typePassword(TestData.PASSWORD)
            LoginPage.tapSignIn()
            MainPage.assertOpened()
        } catch (_: NoMatchingViewException) {
            MainPage.assertOpened()
        }
    }

    @Test
    @Story("Открытие страницы About")
    @DisplayName("TC‑009: Страница «О приложении» открывается и содержит корректные данные")
    @Description("После успешного входа пользователь открывает экран «О приложении» и проверяет версию, политику конфиденциальности и условия использования.")
    @Severity(SeverityLevel.MINOR)
    fun tc009_opensAboutPage() {
        Allure.step("Переход на страницу About") {
            MainPage.openAbout()
        }
        Allure.step("Проверка отображения версии приложения") {
            onView(withId(R.id.about_version_title_text_view))
                .check(matches(withText("Version:")))
        }
        Allure.step("Проверка URL политики конфиденциальности") {
            onView(withId(R.id.about_privacy_policy_value_text_view))
                .check(matches(withText("https://vhospice.org/#/privacy-policy/")))
        }
        Allure.step("Проверка URL условий использования") {
            onView(withId(R.id.about_terms_of_use_value_text_view))
                .check(matches(withText("https://vhospice.org/#/terms-of-use")))
        }
    }
}