package ru.iteco.fmhandroid.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.widget.EditText;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static org.hamcrest.Matchers.allOf;

import io.qameta.allure.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.utils.Wait;

import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import android.app.Activity;
import androidx.test.platform.app.InstrumentationRegistry;

public class LoginPage {

    private final int loginField = R.id.login_text_input_layout;
    private final int passwordField = R.id.password_text_input_layout;
    private final int loginButton = R.id.enter_button;

    @Step("Wait for login screen to be displayed")
    public LoginPage waitForLoginScreen() {
        Wait.waitForView(withId(loginField), 10_000);
        return this;
    }

    @Step("Enter login: {login}")
    public LoginPage enterLogin(String login) {
        onView(allOf(
                isDescendantOfA(withId(loginField)),
                isAssignableFrom(EditText.class)
        )).perform(replaceText(login), closeSoftKeyboard());
        return this;
    }

    @Step("Enter password: {password}")
    public LoginPage enterPassword(String password) {
        onView(allOf(
                isDescendantOfA(withId(passwordField)),
                isAssignableFrom(EditText.class)
        )).perform(replaceText(password), closeSoftKeyboard());
        return this;
    }

    @Step("Tap Login button")
    public MainPage tapLoginButton() {
        onView(withId(loginButton)).perform(click());
        return new MainPage();
    }

    @Step("Tap Login button (expect error)")
    public LoginPage tapLoginButtonExpectingError() {
        onView(withId(loginButton)).perform(click());
        return this;
    }

    public boolean waitForLoginScreenOrMainScreen() {
        try {
            Wait.waitForView(withId(loginField), 5_000);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }
}