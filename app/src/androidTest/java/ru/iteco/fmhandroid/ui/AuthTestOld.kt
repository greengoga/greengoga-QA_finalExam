package ru.iteco.fmhandroid.ui

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
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
import org.hamcrest.Matchers.`is`
import org.hamcrest.core.IsNot.not
import org.junit.*
import org.junit.runner.RunWith
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.page.LoginPageOld
import ru.iteco.fmhandroid.page.MainPageOld
import ru.iteco.fmhandroid.utils.TestData
import ru.iteco.fmhandroid.utils.Wait.forAnyDisplayed

@LargeTest
@Epic("Аутентификация")
@Feature("Логин / Логаут")
@RunWith(AndroidJUnit4::class)
class AuthTest {

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(AppActivity::class.java)

    @Before
    fun ensureLoggedOut() {
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

        MainPageOld.logout()

        forAnyDisplayed(withHint("Login"), timeoutMs = ru.iteco.fmhandroid.utils.Wait.TIMEOUT_LONG)
    }

    @Test
    @Story("Вход с корректными данными")
    @DisplayName("TC‑001: Успешный вход показывает главный экран")
    @Description("Пользователь вводит валидный логин и пароль, нажимает «Sign In». Затем проверяется открытие главного экрана.")
    @Severity(SeverityLevel.CRITICAL)
    fun tc001_successfulLoginShowsMainScreen() {
        Allure.step("Ожидание экрана входа") {
            LoginPageOld.assertOnScreen()
        }
        Allure.step("Ввод логина и пароля") {
            LoginPageOld.typeLogin(TestData.LOGIN)
            LoginPageOld.typePassword(TestData.PASSWORD)
        }
        Allure.step("Нажатие Sign In") {
            LoginPageOld.tapSignIn()
        }
        Allure.step("Проверка, что главный экран открыт") {
            MainPageOld.assertOpened()
        }
    }

    @Test
    @Story("Вход с неверными данными")
    @DisplayName("TC‑002: Вход с некорректными учётными данными показывает Toast")
    @Description("Пользователь вводит неверные логин и пароль, нажимает «Sign In». Проверяется, что появляется сообщение об ошибке.")
    @Severity(SeverityLevel.NORMAL)
    fun tc002_loginWithInvalidCredentialsShowsToast() {
        Allure.step("Ожидание экрана входа") {
            LoginPageOld.assertOnScreen()
        }
        Allure.step("Ввод неверных логина и пароля") {
            LoginPageOld.typeLogin("login")
            LoginPageOld.typePassword("pass")
        }
        Allure.step("Нажатие Sign In") {
            LoginPageOld.tapSignIn()
        }
        Allure.step("Проверка появления Toast с ошибкой") {
            var currentDecorView: View? = null
            activityRule.scenario.onActivity {
                currentDecorView = it.window.decorView
            }
            onView(withText("Something went wrong. Try again later."))
                .inRoot(withDecorView(not(`is`(currentDecorView))))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    @Story("Вход без логина и пароля")
    @DisplayName("TC‑003: Попытка входа с пустыми полями показывает Toast")
    @Description("Пользователь оставляет поля логина и пароля пустыми и нажимает «Sign In». Проверяется сообщение об ошибке.")
    @Severity(SeverityLevel.NORMAL)
    fun tc003_signInRefusedShowsToastWhenFieldsEmpty() {
        Allure.step("Ожидание экрана входа") {
            LoginPageOld.assertOnScreen()
        }
        Allure.step("Очистка полей логина и пароля") {
            onView(withHint("Login")).perform(replaceText(""))
            onView(withHint("Password")).perform(replaceText(""))
        }
        Allure.step("Нажатие Sign In") {
            LoginPageOld.tapSignIn()
        }
        Allure.step("Проверка появления Toast с текстом об обязательных полях") {
            var currentDecorView: View? = null
            activityRule.scenario.onActivity {
                currentDecorView = it.window.decorView
            }
            onView(withText("Login and password cannot be empty"))
                .inRoot(withDecorView(not(`is`(currentDecorView))))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    @Story("Выход из системы")
    @DisplayName("TC‑013: Логаут возвращает на экран входа")
    @Description("Пользователь входит в систему, затем выполняет логаут и проверяет, что появляется экран входа.")
    @Severity(SeverityLevel.NORMAL)
    fun tc013_logoutReturnsToLogin() {
        Allure.step("Ожидание экрана входа") {
            LoginPageOld.assertOnScreen()
        }
        Allure.step("Ввод валидных данных и вход") {
            LoginPageOld.typeLogin(TestData.LOGIN)
            LoginPageOld.typePassword(TestData.PASSWORD)
            LoginPageOld.tapSignIn()
            MainPageOld.assertOpened()
        }
        Allure.step("Нажатие кнопки логаута") {
            MainPageOld.logout()
        }
        Allure.step("Проверка, что экран входа отображается") {
            onView(withHint("Login")).check(matches(isDisplayed()))
        }
    }

    @Test
    @Story("Повторный вход после выхода")
    @DisplayName("TC‑018: Успешный вход после логаута")
    @Description("Пользователь входит, выходит из системы и снова входит с теми же учётными данными. Проверяется главный экран.")
    @Severity(SeverityLevel.MINOR)
    fun tc018_successfulLoginAfterLogout() {
        Allure.step("Ожидание экрана входа") {
            LoginPageOld.assertOnScreen()
        }
        Allure.step("Ввод данных и вход") {
            LoginPageOld.typeLogin(TestData.LOGIN)
            LoginPageOld.typePassword(TestData.PASSWORD)
            LoginPageOld.tapSignIn()
            MainPageOld.assertOpened()
        }
        Allure.step("Логаут") {
            MainPageOld.logout()
        }
        Allure.step("Возврат на экран входа") {
            onView(withHint("Login")).check(matches(isDisplayed()))
        }
        Allure.step("Ввод данных и вход снова") {
            LoginPageOld.typeLogin(TestData.LOGIN)
            LoginPageOld.typePassword(TestData.PASSWORD)
            LoginPageOld.tapSignIn()
            MainPageOld.assertOpened()
        }
    }
}