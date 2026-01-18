package ru.iteco.fmhandroid.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import io.qameta.allure.android.runners.AllureAndroidJUnit4
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
import ru.iteco.fmhandroid.page.LoginPageOld
import ru.iteco.fmhandroid.page.MainPageOld
import ru.iteco.fmhandroid.utils.TestData
import ru.iteco.fmhandroid.utils.Wait
import ru.iteco.fmhandroid.utils.Wait.forAnyDisplayed
import ru.iteco.fmhandroid.utils.nthChildOf

@LargeTest
@RunWith(AllureAndroidJUnit4::class)
@Epic("Раздел «Цитаты / Наша миссия»")
@Feature("Просмотр списка и деталей цитат")
class QuoteTest {

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
            LoginPageOld.typeLogin(TestData.LOGIN)
            LoginPageOld.typePassword(TestData.PASSWORD)
            LoginPageOld.tapSignIn()
            MainPageOld.assertOpened()
        } catch (_: NoMatchingViewException) {
            MainPageOld.assertOpened()
        }
    }

    @After
    fun logout() {
        MainPageOld.logout()
    }

    @Test
    @Story("Открытие списка цитат")
    @DisplayName("TC‑011: Открытие раздела «Наша миссия» показывает список")
    @Description("Пользователь заходит в приложение, открывает раздел «Наша миссия» (Quotes) и видит заголовок и список карточек.")
    @Severity(SeverityLevel.NORMAL)
    fun tc011_openQuotesFromMain() {
        Allure.step("Переход к разделу «Наша миссия»") {
            MainPageOld.openQuotes()
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
        Allure.step("Переход к разделу «Наша миссия»") {
            MainPageOld.openQuotes()
        }
        Allure.step("Нажатие на первую карточку") {
            onView(
                allOf(
                    withId(R.id.our_mission_item_open_card_image_button),
                    isDescendantOfA(
                        nthChildOf(
                            withId(R.id.our_mission_item_list_recycler_view),
                            0
                        )
                    ),
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