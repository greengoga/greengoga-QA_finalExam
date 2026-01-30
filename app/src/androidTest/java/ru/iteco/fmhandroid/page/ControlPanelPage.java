package ru.iteco.fmhandroid.page;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;

import static ru.iteco.fmhandroid.utils.ActivityHelper.getCurrentActivity;
import static ru.iteco.fmhandroid.utils.RecyclerViewChildActions.clickChildViewWithId;
import static ru.iteco.fmhandroid.utils.Wait.waitFor;

import androidx.test.espresso.contrib.RecyclerViewActions;

import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;

public class ControlPanelPage {

    private final int addNewsButton = R.id.add_news_image_view;
    private final int titleField = R.id.news_item_title_text_input_edit_text;
    private final int descriptionField = R.id.news_item_description_text_input_edit_text;
    private final int saveButton = R.id.save_button;
    private final int newsList = R.id.news_list_recycler_view;
    private final int publishDateField = R.id.news_item_publish_date_text_input_edit_text;
    private final int publishTimeField = R.id.news_item_publish_time_text_input_edit_text;
    private final int okButton = android.R.id.button1;
    private final int categoryField = R.id.news_item_category_text_auto_complete_text_view;

    public ControlPanelPage tapAddNews() {
        Allure.step("Tap Add News button");
        onView(withId(addNewsButton)).perform(click());

        return this;
    }

    public ControlPanelPage fillNewsTitle(String title) {
        Allure.step("Fill news title: " + title);
        onView(withId(titleField))
                .perform(replaceText(title), closeSoftKeyboard());

        return this;
    }

    public ControlPanelPage fillNewsDescription(String description) {
        Allure.step("Fill news description");
        onView(withId(descriptionField))
                .perform(replaceText(description), closeSoftKeyboard());

        return this;
    }

    public ControlPanelPage saveNews() {
        Allure.step("Save news");
        onView(withId(saveButton)).perform(click());

        return this;
    }

    public ControlPanelPage createNews(String title, String description) {
        Allure.step("Create news with title and description");
        tapAddNews();
        selectFirstCategory();
        fillNewsTitle(title);
        fillNewsDescription(description);
        confirmPublishDate();
        confirmPublishTime();
        saveNews();

        return this;
    }

    public void checkControlPanelTitle() {
        Allure.step("Check Control Panel title");
        onView(withText(R.string.news_control_panel))
                .check(matches(isDisplayed()));

    }

    public ControlPanelPage confirmPublishDate() {
        Allure.step("Confirm publish date with OK");
        onView(withId(publishDateField)).perform(click());
        onView(withId(okButton)).perform(click());

        return this;
    }

    public ControlPanelPage confirmPublishTime() {
        Allure.step("Confirm publish time with OK");
        onView(withId(publishTimeField)).perform(click());
        onView(withId(okButton)).perform(click());

        return this;
    }

    public void checkEmptyFieldsMessage() {
        Allure.step("Check 'Fill empty fields' toast is shown");
        onView(withText(R.string.empty_fields))
                .inRoot(withDecorView(
                        not(is(getCurrentActivity().getWindow().getDecorView()))
                ))
                .check(matches(isDisplayed()));

    }

    public ControlPanelPage deleteNewsByTitle(String title) {
        Allure.step("Delete news with title");
        onView(withId(newsList))
                .perform(
                        RecyclerViewActions.actionOnItem(
                                hasDescendant(withText(title)),
                                clickChildViewWithId(R.id.delete_news_item_image_view)
                        )
                );
        onView(withText(android.R.string.ok)).inRoot(isDialog()).perform(click());
        return this;
    }

    public void checkNewsWithTitleNotDisplayed(String title) {
        Allure.step("Check news with title is not displayed");
        onView(withText(title))
                .check(doesNotExist());
    }

    public ControlPanelPage selectFirstCategory() {
        Allure.step("Select first news category");
        onView(withId(categoryField))
                .perform(click());

        onData(anything())
                .inRoot(isPlatformPopup())
                .atPosition(0)
                .perform(click());
        return this;
    }

    public ControlPanelPage deleteAllNews() {
        Allure.step("Delete all news items one by one");
        while (true) {
            try {
                onView(withId(newsList))
                        .perform(RecyclerViewActions.actionOnItemAtPosition(
                                0, clickChildViewWithId(R.id.delete_news_item_image_view)));

                onView(isRoot()).perform(waitFor(100));

                onView(withText(android.R.string.ok))
                        .inRoot(isDialog())
                        .perform(click());
            } catch (Exception e) {
                break;
            }
        }
        return this;
    }

    public ControlPanelPage checkEmptyNewsListMessageDisplayed() {
        Allure.step("Check empty news list message is displayed on Control Panel");
        onView(withId(R.id.control_panel_empty_news_list_text_view))
                .check(matches(isDisplayed()));
        return this;
    }

    public void checkNewsWithDescriptionDisplayed(String description) {
        Allure.step("Check created news with description is displayed in list");
        onView(withId(newsList))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(description))))
                .check(matches(hasDescendant(withText(description))));
    }
}
