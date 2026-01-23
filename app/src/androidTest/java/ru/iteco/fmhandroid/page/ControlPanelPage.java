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

import androidx.test.espresso.contrib.RecyclerViewActions;

import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.util.HumanReadables;

import org.hamcrest.Matcher;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import io.qameta.allure.Step;
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

    @Step("Tap Add News button")
    public ControlPanelPage tapAddNews() {
        onView(withId(addNewsButton)).perform(click());
        return this;
    }

    @Step("Fill news title: {title}")
    public ControlPanelPage fillNewsTitle(String title) {
        onView(withId(titleField))
                .perform(replaceText(title), closeSoftKeyboard());
        return this;
    }

    @Step("Fill news description")
    public ControlPanelPage fillNewsDescription(String description) {
        onView(withId(descriptionField))
                .perform(replaceText(description), closeSoftKeyboard());
        return this;
    }

    @Step("Save news")
    public ControlPanelPage saveNews() {
        onView(withId(saveButton)).perform(click());
        return this;
    }

    @Step("Create news with title and description")
    public ControlPanelPage createNews(String title, String description) {
        tapAddNews();
        selectFirstCategory();
        fillNewsTitle(title);
        fillNewsDescription(description);
        confirmPublishDate();
        confirmPublishTime();
        saveNews();
        return this;
    }

    @Step("Check created news with title is displayed in list")
    public void checkNewsWithTitleDisplayed(String title) {
        onView(withId(newsList))
                .perform(
                        RecyclerViewActions.scrollTo(
                                hasDescendant(withText(title))
                        )
                )
                .check(matches(hasDescendant(withText(title))));
    }

    @Step("Check Control Panel title")
    public void checkControlPanelTitle() {
        onView(withText(R.string.news_control_panel))
                .check(matches(isDisplayed()));
    }

    @Step("Confirm publish date with OK")
    public ControlPanelPage confirmPublishDate() {
        onView(withId(publishDateField)).perform(click());
        onView(withId(okButton)).perform(click());
        return this;
    }

    @Step("Confirm publish time with OK")
    public ControlPanelPage confirmPublishTime() {
        onView(withId(publishTimeField)).perform(click());
        onView(withId(okButton)).perform(click());
        return this;
    }

    @Step("Check 'Fill empty fields' toast is shown")
    public void checkEmptyFieldsMessage() {
        onView(withText(R.string.empty_fields))
                .inRoot(withDecorView(
                        not(is(getCurrentActivity().getWindow().getDecorView()))
                ))
                .check(matches(isDisplayed()));
    }

    @Step("Delete news with title")
    public ControlPanelPage deleteNewsByTitle(String title) {
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

    @Step("Check news with title is not displayed")
    public void checkNewsWithTitleNotDisplayed(String title) {
        onView(withText(title))
                .check(doesNotExist());
    }

    @Step("Select first news category")
    public ControlPanelPage selectFirstCategory() {
        onView(withId(categoryField))
                .perform(click());

        onData(anything())
                .inRoot(isPlatformPopup())
                .atPosition(0)
                .perform(click());

        return this;
    }

    @Step("Delete all news items one by one")
    private int getNewsListItemCount() {
        AtomicInteger count = new AtomicInteger(0);
        onView(withId(newsList)).check((view, noViewFoundException) -> {
            if (noViewFoundException != null) throw noViewFoundException;
            RecyclerView rv = (RecyclerView) view;
            RecyclerView.Adapter<?> adapter = rv.getAdapter();
            count.set(adapter == null ? 0 : adapter.getItemCount());
        });
        return count.get();
    }

    private static ViewAction waitForRecyclerViewItemCount(int recyclerViewId, int expectedCount, long timeoutMs) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait up to " + timeoutMs + " ms for RecyclerView(" + recyclerViewId + ") itemCount == " + expectedCount;
            }

            @Override
            public void perform(UiController uiController, View view) {
                long start = System.currentTimeMillis();
                long end = start + timeoutMs;

                do {
                    RecyclerView rv = view.getRootView().findViewById(recyclerViewId);
                    if (rv != null && rv.getAdapter() != null && rv.getAdapter().getItemCount() == expectedCount) {
                        return;
                    }
                    uiController.loopMainThreadForAtLeast(50);
                } while (System.currentTimeMillis() < end);

                throw new PerformException.Builder()
                        .withActionDescription(getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }

    @Step("Delete all news items one by one")
    public ControlPanelPage deleteAllNews() {
        while (true) {
            try {
                onView(withId(newsList))
                        .perform(
                                RecyclerViewActions.actionOnItemAtPosition(
                                        0,
                                        clickChildViewWithId(R.id.delete_news_item_image_view)
                                )
                        );
                //костыль
                Thread.sleep(100);
                onView(withText(android.R.string.ok))
                        .inRoot(isDialog())
                        .perform(click());
            } catch (Exception e) {
                break;
            }
        }
        return this;
    }

    @Step("Check empty news list message is displayed on Control Panel")
    public ControlPanelPage checkEmptyNewsListMessageDisplayed() {
        onView(withId(R.id.control_panel_empty_news_list_text_view))
                .check(matches(isDisplayed()));
        return this;
    }

    @Step("Check created news with description is displayed in list")
    public void checkNewsWithDescriptionDisplayed(String description) {
        onView(withId(newsList))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(description))))
                .check(matches(hasDescendant(withText(description))));
    }
}
