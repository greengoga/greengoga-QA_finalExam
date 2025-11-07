package ru.iteco.fmhandroid.ui

import android.content.pm.ActivityInfo
import android.view.View
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.*
import org.junit.runner.RunWith
import ru.iteco.fmhandroid.R
import ru.iteco.fmhandroid.page.LoginPage
import ru.iteco.fmhandroid.page.MainPage
import ru.iteco.fmhandroid.utils.Wait.forAnyDisplayed
import ru.iteco.fmhandroid.utils.clickChildViewWithId
import ru.iteco.fmhandroid.utils.getTextFromRecyclerViewItem
import ru.iteco.fmhandroid.utils.nthChildOf
import ru.iteco.fmhandroid.utils.scrollToEndOfRecyclerView

@LargeTest
@RunWith(AndroidJUnit4::class)
class ControlPanelTest {

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
    fun tc009_opensCpViaNews() {
        LoginPage.assertOnScreen()

        LoginPage.typeLogin("login2")
        LoginPage.typePassword("password2")
        LoginPage.tapSignIn()
        MainPage.assertOpened()

        MainPage.openNews()
        MainPage.openControlPanel()

        onView(withText("Control panel"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun tc007_sortBtnChangesOrder() {
        LoginPage.assertOnScreen()

        LoginPage.typeLogin("login2")
        LoginPage.typePassword("password2")
        LoginPage.tapSignIn()
        MainPage.assertOpened()

        MainPage.openNews()
        MainPage.openControlPanel()

        var firstTitleBefore = ""
        var firstTitleAfter = ""

        activityRule.scenario.onActivity { activity ->
            firstTitleBefore = getTextFromRecyclerViewItem(
                activity,
                R.id.news_list_recycler_view,
                R.id.news_item_title_text_view,
                0
            )
        }
        onView(withId(R.id.sort_news_material_button)).perform(click())
        activityRule.scenario.onActivity { activity ->
            firstTitleAfter = getTextFromRecyclerViewItem(
                activity,
                R.id.news_list_recycler_view,
                R.id.news_item_title_text_view,
                0
            )
        }
        assert(firstTitleBefore != firstTitleAfter) {
            "Сортировка не изменила порядок элементов: '$firstTitleBefore'"
        }
    }

    @Test
    fun tc008_filterBtnFiltersByDate() {
        LoginPage.assertOnScreen()

        LoginPage.typeLogin("login2")
        LoginPage.typePassword("password2")
        LoginPage.tapSignIn()
        MainPage.assertOpened()

        MainPage.openNews()
        MainPage.openControlPanel()

        onView(withId(R.id.filter_news_material_button)).perform(click())

        onView(withId(R.id.news_item_publish_date_start_text_input_edit_text)).perform(replaceText("01.10.2025"))
        onView(withId(R.id.news_item_publish_date_end_text_input_edit_text)).perform(replaceText("02.11.2025"))
        onView(withId(R.id.filter_button)).perform(click())

        onView(withId(R.id.control_panel_empty_news_list_text_view))
            .check(matches(withText("There is nothing here yet…")))
    }

    @Test
    fun tc010_createsNewsCard() {
        LoginPage.assertOnScreen()

        LoginPage.typeLogin("login2")
        LoginPage.typePassword("password2")
        LoginPage.tapSignIn()
        MainPage.assertOpened()

        MainPage.openNews()
        MainPage.openControlPanel()

        onView(
            allOf(
                withId(R.id.add_news_image_view),
                withContentDescription("Add news button"),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(com.google.android.material.R.id.text_input_end_icon),
                withContentDescription("Show dropdown menu"),
                isDisplayed()
            )
        ).perform(click())
        onData(anything())
            .atPosition(0)
            .inRoot(isPlatformPopup())
            .perform(click())

        onView(allOf(withId(R.id.news_item_publish_date_text_input_edit_text), isDisplayed()))
            .perform(click())
        onView(allOf(withId(android.R.id.button1), withText("OK")))
            .perform(scrollTo(), click())

        onView(allOf(withId(R.id.news_item_publish_time_text_input_edit_text), isDisplayed()))
            .perform(click())
        onView(allOf(withId(android.R.id.button1), withText("OK")))
            .perform(scrollTo(), click())

        onView(allOf(withId(R.id.news_item_description_text_input_edit_text), isDisplayed()))
            .perform(replaceText("test"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.save_button),
                withText("Save"),
                withContentDescription("Save"),
                isDisplayed()
            )
        ).perform(scrollTo(), click())

        activityRule.scenario.onActivity { activity ->
            scrollToEndOfRecyclerView(R.id.news_list_recycler_view, activity)
        }

        onView(
            allOf(
                withId(R.id.news_item_title_text_view),
                withText("Объявление"),
                withParent(withParent(withId(R.id.news_item_material_card_view))),
                isDisplayed()
            )
        ).check(matches(isDisplayed()))

        /* Проверяем конкретную дату
        onView(
            allOf(
                withId(R.id.news_item_publication_date_text_view),
                withText("03.11.2025"),
                withParent(withParent(withId(R.id.news_item_material_card_view))),
                isDisplayed()
            )
        ).check(matches(isDisplayed()))
*/

        val today = java.text.SimpleDateFormat("dd.MM.yyyy").format(java.util.Date())

        onView(
            allOf(
                withId(R.id.news_item_publication_date_text_view),
                withText(today),
                withParent(withParent(withId(R.id.news_item_material_card_view))),
                isDisplayed()
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun tc014_editsNewsCardAndChecksChanges() {
        LoginPage.assertOnScreen()

        LoginPage.typeLogin("login2")
        LoginPage.typePassword("password2")
        LoginPage.tapSignIn()
        MainPage.assertOpened()

        MainPage.openNews()
        MainPage.openControlPanel()

        onView(
            allOf(
                withId(R.id.edit_news_item_image_view),
                isDescendantOfA(
                    nthChildOf(withId(R.id.news_list_recycler_view), 0)
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(com.google.android.material.R.id.text_input_end_icon),
                withContentDescription("Show dropdown menu"),
                isDisplayed()
            )
        ).perform(click())

        onView(withText("Объявление"))
            .inRoot(isPlatformPopup())
            .perform(click())

        onView(withId(R.id.news_item_title_text_input_edit_text))
            .perform(replaceText("Netology"), closeSoftKeyboard())

        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(
            scrollTo(),
            click()
        )
        onView(withText("OK")).perform(click())

        onView(withId(R.id.news_item_description_text_input_edit_text))
            .perform(replaceText("test"), closeSoftKeyboard())

        onView(withId(R.id.save_button)).perform(click())

        onView(withId(R.id.sort_news_material_button)).perform(click())

        onView(
            allOf(
                withId(R.id.news_item_title_text_view),
                withText("Netology"),
                isDisplayed()
            )
        ).check(matches(withText("Netology")))

        onView(
            allOf(
                withId(R.id.news_item_material_card_view),
                hasDescendant(allOf(withId(R.id.news_item_title_text_view), withText("Netology"))),
                isDisplayed()
            )
        ).perform(click())

        val textView = onView(
            allOf(
                withId(R.id.news_item_description_text_view), withText("test"),
                withParent(withParent(withId(R.id.news_item_material_card_view))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("test")))
    }

    @Test
    fun tc015_successfullyDeletesNewsCard() {
        LoginPage.assertOnScreen()

        LoginPage.typeLogin("login2")
        LoginPage.typePassword("password2")
        LoginPage.tapSignIn()
        MainPage.assertOpened()

        MainPage.openNews()
        MainPage.openControlPanel()

        onView(
            allOf(
                withId(R.id.edit_news_item_image_view),
                isDescendantOfA(nthChildOf(withId(R.id.news_list_recycler_view), 0)),
                isDisplayed()
            )
        ).perform(click())

        onView(withId(R.id.news_item_title_text_input_edit_text))
            .perform(replaceText("ToDelete"), closeSoftKeyboard())

        onView(withId(R.id.news_item_description_text_input_edit_text))
            .perform(replaceText("ToDelete"), closeSoftKeyboard())

        onView(withId(R.id.save_button)).perform(click())

        onView(
            allOf(
                withId(R.id.news_item_material_card_view),
                hasDescendant(allOf(withId(R.id.news_item_title_text_view), withText("ToDelete"))),
                isDisplayed()
            )
        ).perform(
            clickChildViewWithId(R.id.delete_news_item_image_view)
        )

        onView(withText("OK")).perform(click())

        onView(
            allOf(
                withId(R.id.news_item_title_text_view),
                withText("ToDelete")
            )
        ).check(doesNotExist())
    }

    @Test
    fun tc016_creatingNewsCardWithoutRequiredFieldsShowsToast() {
        LoginPage.assertOnScreen()

        LoginPage.typeLogin("login2")
        LoginPage.typePassword("password2")
        LoginPage.tapSignIn()
        MainPage.assertOpened()

        MainPage.openNews()
        MainPage.openControlPanel()

        onView(
            allOf(
                withId(R.id.add_news_image_view),
                withContentDescription("Add news button"),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.save_button),
                withText("Save"),
                withContentDescription("Save"),
                isDisplayed()
            )
        ).perform(scrollTo(), click())

        onView(withText("Fill empty fields"))
            .inRoot(withDecorView(not(`is`(decorView))))
            .check(matches(isDisplayed()))
    }


    @Test
    fun tc019_rotateScreenAndCheckStability() {
        LoginPage.assertOnScreen()

        LoginPage.typeLogin("login2")
        LoginPage.typePassword("password2")
        LoginPage.tapSignIn()
        MainPage.assertOpened()

        MainPage.openNews()
        MainPage.openControlPanel()

        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        onView(withText("Control panel"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun tc021_creatingNewsCardWithoutTitleShowsToast() {
        LoginPage.assertOnScreen()
        LoginPage.typeLogin("login2")
        LoginPage.typePassword("password2")
        LoginPage.tapSignIn()
        MainPage.assertOpened()

        MainPage.openNews()
        MainPage.openControlPanel()

        onView(
            allOf(
                withId(R.id.add_news_image_view),
                withContentDescription("Add news button"),
                isDisplayed()
            )
        ).perform(click())

        onView(withId(R.id.news_item_description_text_input_edit_text))
            .perform(replaceText("Some description"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.save_button),
                withText("Save"),
                withContentDescription("Save"),
                isDisplayed()
            )
        ).perform(scrollTo(), click())

        onView(withText("Fill empty fields"))
            .inRoot(withDecorView(not(`is`(decorView))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun tc021_savingEditedNewsWithEmptyRequiredFieldsShowsToast() {
        LoginPage.assertOnScreen()

        LoginPage.typeLogin("login2")
        LoginPage.typePassword("password2")
        LoginPage.tapSignIn()
        MainPage.assertOpened()

        MainPage.openNews()
        MainPage.openControlPanel()

        onView(
            allOf(
                withId(R.id.edit_news_item_image_view),
                isDescendantOfA(nthChildOf(withId(R.id.news_list_recycler_view), 0)),
                isDisplayed()
            )
        ).perform(click())

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

        onView(withText("Fill empty fields"))
            .inRoot(withDecorView(not(`is`(decorView))))
            .check(matches(isDisplayed()))
    }
}
