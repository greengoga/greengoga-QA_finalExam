package ru.iteco.fmhandroid.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.contrib.RecyclerViewActions;

import io.qameta.allure.Step;
import ru.iteco.fmhandroid.R;

public class NewsPage {

    private final int newsList = R.id.news_list_recycler_view;
    private final int editNewsButton = R.id.edit_news_material_button;

    @Step("Check that news list is displayed")
    public void checkNewsListDisplayed() {
        onView(withId(newsList))
                .check(matches(isDisplayed()));
    }

    @Step("Open Control Panel from News screen")
    public ControlPanelPage openControlPanel() {
        onView(withId(editNewsButton))
                .check(matches(isDisplayed()));
        onView(withId(editNewsButton))
                .perform(click());
        return new ControlPanelPage();
    }

    @Step("Open first news card")
    public NewsPage openFirstNewsCard() {
        onView(withId(newsList))
                .check(matches(isDisplayed()))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition(0, click())
                );
        return this;
    }
}
