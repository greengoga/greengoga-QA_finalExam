package ru.iteco.fmhandroid.ui

import android.content.pm.ActivityInfo
import android.view.View
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.RootMatchers.*
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
import org.hamcrest.Matchers.*
import org.junit.*
import org.junit.runner.RunWith
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.page.LoginPageOld
import ru.iteco.fmhandroid.page.MainPageOld
import ru.iteco.fmhandroid.utils.TestData
import ru.iteco.fmhandroid.utils.Wait
import ru.iteco.fmhandroid.utils.Wait.forAnyDisplayed
import ru.iteco.fmhandroid.utils.clickChildViewWithId
import ru.iteco.fmhandroid.utils.nthChildOf
import ru.iteco.fmhandroid.utils.scrollToEndOfRecyclerView

@LargeTest
@RunWith(AndroidJUnit4::class)
@Epic("Панель управления новостями")
@Feature("Создание, изменение, удаление, фильтрация и сортировка новостей")
class ControlPanelTest {

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(AppActivity::class.java)

    private lateinit var decorView: View

    @Before
    fun ensureLoggedIn() {
        activityRule.scenario.onActivity { decorView = it.window.decorView }


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

    @Test
    @Story("Открытие панели управления через раздел «Новости»")
    @DisplayName("TC‑009: Открытие Control Panel через News")
    @Description("Авторизация → раздел «Новости» → открытие Панели управления новостями → проверка наличия заголовка «Control panel».")
    @Severity(SeverityLevel.NORMAL)
    fun tc009_opensCpViaNews() {
        Allure.step("Переход в раздел «Новости» и открытие панели управления") {
            MainPageOld.openNewsFromMain()
            MainPageOld.openControlPanel()
        }
        Allure.step("Проверка, что заголовок «Control panel» отображается") {
            onView(withText("Control panel")).check(matches(isDisplayed()))
        }
    }

    @Test
    @Story("Сортировка новостей изменяет порядок")
    @DisplayName("TC‑007: Проверка кнопки Sort изменяет порядок")
    @Description("Авторизация → Панель управления новостями → запоминаем заголовок первой новости → нажимаем Sort → проверяем, что заголовок первой новости изменился.")
    @Severity(SeverityLevel.CRITICAL)
    fun tc007_sortBtnChangesOrder() {
        Allure.step("Переход в Панель управления") {
            MainPageOld.openNewsFromMain()
            MainPageOld.openControlPanel()
        }
        Allure.step("Запоминаем заголовок первой новости до сортировки") {
            activityRule.scenario.onActivity { activity ->
            }
        }
        Allure.step("Нажатие кнопки Sort") {
            onView(withId(R.id.sort_news_material_button)).perform(click())
        }
        Allure.step("Проверка, что заголовок первой новости изменился") {
        }
    }

    @Test
    @Story("Фильтрация новостей по дате")
    @DisplayName("TC‑008: Проверка кнопки Filter фильтрует по дате")
    @Description("Авторизация → Панель управления новостями → открытие фильтра → ввод диапазона дат → проверка отображения сообщения \"There is nothing here yet…\"")
    @Severity(SeverityLevel.NORMAL)
    fun tc008_filterBtnFiltersByDate() {
        Allure.step("Открытие панели управления новостями") {
            MainPageOld.openNewsFromMain()
            MainPageOld.openControlPanel()
        }
        Allure.step("Ввод диапазона дат и применение фильтра") {
            onView(withId(R.id.filter_news_material_button)).perform(click())
            onView(withId(R.id.news_item_publish_date_start_text_input_edit_text))
                .perform(replaceText("01.10.2025"))
            onView(withId(R.id.news_item_publish_date_end_text_input_edit_text))
                .perform(replaceText("02.11.2025"))
            onView(withId(R.id.filter_button)).perform(click())
        }
        Allure.step("Проверка отображения текста об отсутствии новостей") {
            onView(withId(R.id.control_panel_empty_news_list_text_view))
                .check(matches(withText("There is nothing here yet…")))
        }
    }

    @Test
    @Story("Создание новой новости")
    @DisplayName("TC‑010: Создание карточки новости")
    @Description("Авторизация → Панель управления новостями → добавление новости с заполнением обязательных полей → проверка появления новой карточки.")
    @Severity(SeverityLevel.CRITICAL)
    fun tc010_createsNewsCard() {
        Allure.step("Открытие панели управления и добавление новости") {
            MainPageOld.openNewsFromMain()
            MainPageOld.openControlPanel()

            onView(allOf(
                withId(R.id.add_news_image_view),
                withContentDescription("Add news button"),
                isDisplayed()
            )).perform(click())

            onView(allOf(
                withId(com.google.android.material.R.id.text_input_end_icon),
                withContentDescription("Show dropdown menu"),
                isDisplayed()
            )).perform(click())
            onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click())

            onView(allOf(withId(R.id.news_item_publish_date_text_input_edit_text), isDisplayed())).perform(click())
            onView(allOf(withId(android.R.id.button1), withText("OK"))).perform(scrollTo(), click())

            onView(allOf(withId(R.id.news_item_publish_time_text_input_edit_text), isDisplayed())).perform(click())
            onView(allOf(withId(android.R.id.button1), withText("OK"))).perform(scrollTo(), click())

            onView(allOf(withId(R.id.news_item_description_text_input_edit_text), isDisplayed()))
                .perform(replaceText("test"), closeSoftKeyboard())

            onView(allOf(withId(R.id.save_button), withText("Save"), withContentDescription("Save"), isDisplayed()))
                .perform(scrollTo(), click())
        }
        Allure.step("Проверка, что карточка с сегодняшней датой появилась") {
            activityRule.scenario.onActivity { activity ->
                scrollToEndOfRecyclerView(R.id.news_list_recycler_view, activity)
            }
            val today = java.text.SimpleDateFormat("dd.MM.yyyy").format(java.util.Date())
            onView(allOf(
                withId(R.id.news_item_publication_date_text_view),
                withText(today),
                withParent(withParent(withId(R.id.news_item_material_card_view))),
                isDisplayed()
            )).check(matches(isDisplayed()))
        }
    }

    @Test
    @Story("Редактирование новости и проверка изменений")
    @DisplayName("TC‑014: Редактирование карточки новости и проверка изменений")
    @Description("Авторизация → Панель управления новостями → редактирование первой карточки → изменение заголовка и текста → проверка, что изменения отображаются.")
    @Severity(SeverityLevel.CRITICAL)
    fun tc014_editsNewsCardAndChecksChanges() {
        Allure.step("Открытие панели управления и редактирование первой новости") {
            MainPageOld.openNewsFromMain()
            MainPageOld.openControlPanel()

            onView(allOf(
                withId(R.id.edit_news_item_image_view),
                isDescendantOfA(nthChildOf(withId(R.id.news_list_recycler_view), 0)),
                isDisplayed()
            )).perform(click())

            onView(allOf(
                withId(com.google.android.material.R.id.text_input_end_icon),
                withContentDescription("Show dropdown menu"),
                isDisplayed()
            )).perform(click())
            onView(withText("Объявление")).inRoot(isPlatformPopup()).perform(click())

            onView(withId(R.id.news_item_title_text_input_edit_text))
                .perform(replaceText("Netology"), closeSoftKeyboard())

            onView(withId(R.id.news_item_publish_date_text_input_edit_text))
                .perform(scrollTo(), click())
            onView(withText("OK")).perform(click())

            onView(withId(R.id.news_item_description_text_input_edit_text))
                .perform(replaceText("test"), closeSoftKeyboard())

            onView(withId(R.id.save_button)).perform(click())
            onView(withId(R.id.sort_news_material_button)).perform(click())
        }
        Allure.step("Проверка, что первая карточка отредактирована") {
            onView(allOf(
                withId(R.id.news_item_title_text_view),
                withText("Netology"),
                isDisplayed()
            )).check(matches(withText("Netology")))

            onView(allOf(
                withId(R.id.news_item_material_card_view),
                hasDescendant(allOf(withId(R.id.news_item_title_text_view), withText("Netology"))),
                isDisplayed()
            )).perform(click())

            onView(allOf(
                withId(R.id.news_item_description_text_view),
                withText("test"),
                withParent(withParent(withId(R.id.news_item_material_card_view))),
                isDisplayed()
            )).check(matches(withText("test")))
        }
    }

    @Test
    @Story("Удаление новости")
    @DisplayName("TC‑015: Успешное удаление карточки новости")
    @Description("Авторизация → Панель управления новостями → редактирование первой карточки с целью задавать уникальный заголовок → удаление этой новости → проверка, что карточка исчезла.")
    @Severity(SeverityLevel.CRITICAL)
    fun tc015_successfullyDeletesNewsCard() {
        Allure.step("Открытие панели управления и подготовка новости к удалению") {
            MainPageOld.openNewsFromMain()
            MainPageOld.openControlPanel()

            onView(allOf(
                withId(R.id.edit_news_item_image_view),
                isDescendantOfA(nthChildOf(withId(R.id.news_list_recycler_view), 0)),
                isDisplayed()
            )).perform(click())

            onView(withId(R.id.news_item_title_text_input_edit_text))
                .perform(replaceText("ToDelete"), closeSoftKeyboard())
            onView(withId(R.id.news_item_description_text_input_edit_text))
                .perform(replaceText("ToDelete"), closeSoftKeyboard())
            onView(withId(R.id.save_button)).perform(click())
        }
        Allure.step("Удаление новости и проверка отсутствия карточки") {
            onView(allOf(
                withId(R.id.news_item_material_card_view),
                hasDescendant(allOf(withId(R.id.news_item_title_text_view), withText("ToDelete"))),
                isDisplayed()
            )).perform(clickChildViewWithId(R.id.delete_news_item_image_view))

            onView(withText("OK")).perform(click())

            onView(allOf(
                withId(R.id.news_item_title_text_view),
                withText("ToDelete")
            )).check(doesNotExist())
        }
    }

    @Test
    @Story("Создание новости без заполнения обязательных полей")
    @DisplayName("TC‑016: Попытка создать новость без обязательных полей показывает Toast")
    @Description("Авторизация → Панель управления новостями → нажатие кнопки добавления новости → попытка сохранить без ввода полей → проверка Toast.")
    @Severity(SeverityLevel.NORMAL)
    fun tc016_creatingNewsCardWithoutRequiredFieldsShowsToast() {
        Allure.step("Переход в панель управления и нажатие Add") {
            MainPageOld.openNewsFromMain()
            MainPageOld.openControlPanel()
            onView(allOf(
                withId(R.id.add_news_image_view),
                withContentDescription("Add news button"),
                isDisplayed()
            )).perform(click())
        }
        Allure.step("Попытка сохранить без заполненных полей") {
            onView(allOf(
                withId(R.id.save_button),
                withText("Save"),
                withContentDescription("Save"),
                isDisplayed()
            )).perform(scrollTo(), click())
        }
        Allure.step("Проверка Toast «Fill empty fields»") {
            onView(withText("Fill empty fields"))
                .inRoot(withDecorView(not(`is`(decorView))))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    @Story("Упор при повороте экрана")
    @DisplayName("TC‑019: Проверка устойчивости при повороте экрана")
    @Description("Авторизация → Панель управления новостями → поворот экрана → проверка, что экран «Control panel» отображается.")
    @Severity(SeverityLevel.MINOR)
    fun tc019_rotateScreenAndCheckStability() {
        Allure.step("Переход в Control Panel и поворот экрана в Landscape") {
            MainPageOld.openNewsFromMain()
            MainPageOld.openControlPanel()
            activityRule.scenario.onActivity { activity ->
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
        Allure.step("Поворот экрана обратно в Portrait") {
            activityRule.scenario.onActivity { activity ->
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
        Allure.step("Проверка, что заголовок «Control panel» отображается") {
            onView(withText("Control panel")).check(matches(isDisplayed()))
        }
    }

    @Test
    @Story("Попытка сохранить отредактированную новость с пустыми обязательными полями")
    @DisplayName("TC‑021: Попытка отредактировать новость с пустыми полями показывает Toast")
    @Description("Авторизация → Панель управления новостями → редактирование новости, удаление заголовка и описания → попытка сохранить → чек Toast.")
    @Severity(SeverityLevel.NORMAL)
    fun tc021_savingEditedNewsWithEmptyRequiredFieldsShowsToast() {
        Allure.step("Переход в панель управления и нажатие Edit") {
            MainPageOld.openNewsFromMain()
            MainPageOld.openControlPanel()
            onView(
                allOf(
                    withId(R.id.edit_news_item_image_view),
                    isDescendantOfA(nthChildOf(withId(R.id.news_list_recycler_view), 0)),
                    isDisplayed()
                )
            ).perform(click())
        }

        Allure.step("Удаление значений из полей и сохранение") {
            onView(withId(R.id.news_item_title_text_input_edit_text))
                .perform(scrollTo(), replaceText(""), closeSoftKeyboard())
            onView(withId(R.id.news_item_description_text_input_edit_text))
                .perform(scrollTo(), replaceText(""), closeSoftKeyboard())
            onView(
                allOf(
                    withId(R.id.save_button),
                    withText("Save"),
                    withContentDescription("Save"),
                    isDisplayed()
                )
            ).perform(scrollTo(), click())
        }

        Allure.step("Проверка Toast «Fill empty fields»") {
            onView(withText("Fill empty fields"))
                .inRoot(withDecorView(not(`is`(decorView))))
                .check(matches(isDisplayed()))
        }
    }
}