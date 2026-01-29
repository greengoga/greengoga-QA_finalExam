package ru.iteco.fmhandroid.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.widget.EditText;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static org.hamcrest.Matchers.allOf;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.utils.Wait;

import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import android.view.View;

public class LoginPage {

    private final int loginField = R.id.login_text_input_layout;
    private final int passwordField = R.id.password_text_input_layout;
    private final int loginButton = R.id.enter_button;
    private View decorView;

    public LoginPage waitForLoginScreen() {
        Allure.step("Wait for login screen to be displayed");
            Wait.waitForView(withId(loginField), 10_000);
        return this;
    }

    public LoginPage enterLogin(String login) {
        Allure.step("Enter login: " + login);
            onView(allOf(
                    isDescendantOfA(withId(loginField)),
                    isAssignableFrom(EditText.class)
            )).perform(replaceText(login), closeSoftKeyboard());

        return this;
    }

    public LoginPage enterPassword(String password) {
        Allure.step("Enter password");
            onView(allOf(
                    isDescendantOfA(withId(passwordField)),
                    isAssignableFrom(EditText.class)
            )).perform(replaceText(password), closeSoftKeyboard());
        return this;
    }

    public MainPage tapLoginButton() {
        Allure.step("Tap Login button");
            onView(withId(loginButton)).perform(click());
        return new MainPage();
    }

    public LoginPage tapLoginButtonExpectingError() {
        Allure.step("Tap Login button (expect error)");
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

    public LoginPage initDecorView(View decorView) {
        this.decorView = decorView;
        return this;
    }

    public LoginPage checkEmptyLoginOrPasswordToast() {
        onView(withText(R.string.empty_login_or_password))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
        return this;
    }

    public LoginPage checkLoginErrorToast() {
        onView(withText(R.string.error))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));
        return this;
    }
}