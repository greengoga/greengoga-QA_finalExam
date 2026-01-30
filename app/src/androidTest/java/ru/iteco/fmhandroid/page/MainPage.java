package ru.iteco.fmhandroid.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import io.qameta.allure.kotlin.Allure;
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

    public MainPage openMenu() {
        Allure.step("Open main menu");
        Wait.waitForView(withId(menuButton), 5000);
        onView(withId(menuButton))
                .check(matches(isDisplayed()))
                .perform(click());
        return this;
    }

    public NewsPage goToNews() {
        Allure.step("Go to News");
        openMenu();
        onView(withText(R.string.news)).perform(click());
        return new NewsPage();
    }

    public AboutPage goToAbout() {
        Allure.step("Go to About");
        openMenu();
        onView(withText(R.string.about)).perform(click());
        return new AboutPage();
    }

    public QuotePage goToQuotes() {
        Allure.step("Go to Quotes");
        onView(withId(quoteMenuItem)).perform(click());
        return new QuotePage();
    }

    public void logout() {
        Allure.step("Logout");
        Wait.waitForView(withId(R.id.authorization_image_button), 5000);
        onView(withId(R.id.authorization_image_button)).perform(click());
        onView(withText(R.string.log_out)).perform(click());
    }
}