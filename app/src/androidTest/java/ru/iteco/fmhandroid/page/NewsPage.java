package ru.iteco.fmhandroid.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.contrib.RecyclerViewActions;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;

public class NewsPage {

    private final int newsList = R.id.news_list_recycler_view;
    private final int editNewsButton = R.id.edit_news_material_button;

    public void checkNewsListDisplayed() {
        Allure.step("Check that news list is displayed");
        onView(withId(newsList))
                .check(matches(isDisplayed()));
    }

    public ControlPanelPage openControlPanel() {
        Allure.step("Open Control Panel from News screen");
        onView(withId(editNewsButton))
                .check(matches(isDisplayed()));
        onView(withId(editNewsButton))
                .perform(click());
        return new ControlPanelPage();
    }

    public NewsPage openFirstNewsCard() {
        Allure.step("Open first news card");
        onView(withId(newsList))
                .check(matches(isDisplayed()))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition(0, click())
                );
        return this;
    }
}
