package ru.iteco.fmhandroid.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import io.qameta.allure.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.utils.Wait;

public class MainPage {

    private final int menuButton = R.id.main_menu_image_button;
    private final int quoteMenuItem = R.id.our_mission_image_button;

    public boolean isMainScreenDisplayed() {
        try {
            Wait.waitForView(withId(menuButton), 5000);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    @Step("Open main menu")
    public MainPage openMenu() {
        Wait.waitForView(withId(menuButton), 5000);
        onView(withId(menuButton))
                .check(matches(isDisplayed()))
                .perform(click());
        return this;
    }

    @Step("Go to News")
    public NewsPage goToNews() {
        openMenu();
        onView(withText(R.string.news)).perform(click());
        return new NewsPage();
    }

    @Step("Go to About")
    public AboutPage goToAbout() {
        openMenu();
        onView(withText(R.string.about)).perform(click());
        return new AboutPage();
    }

    @Step("Go to Quotes")
    public QuotePage goToQuotes() {
        onView(withId(quoteMenuItem)).perform(click());
        return new QuotePage();
    }

    @Step("Logout")
    public void logout() {
            Wait.waitForView(withId(R.id.authorization_image_button), 5000);
            onView(withId(R.id.authorization_image_button)).perform(click());
            onView(withText(R.string.log_out)).perform(click());
    }
}