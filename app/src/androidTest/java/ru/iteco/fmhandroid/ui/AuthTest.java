package ru.iteco.fmhandroid.ui;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.iteco.fmhandroid.data.TestData;
import ru.iteco.fmhandroid.page.LoginPage;
import ru.iteco.fmhandroid.page.MainPage;

@RunWith(AndroidJUnit4.class)
public class AuthTest {

    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();

    @Test
    public void tc001_validLogin() {
        loginPage
                .waitForLoginScreen()
                .enterLogin(TestData.VALID_LOGIN)
                .enterPassword(TestData.VALID_PASSWORD)
                .tapLoginButton();
    }

    @Test
    public void tc002_emptyLoginAndPassword() {
        loginPage
                .waitForLoginScreen()
                .enterLogin("")
                .enterPassword("")
                .tapLoginButtonExpectingError();
    }

    @Test
    public void tc003_invalidLogin() {
        loginPage
                .waitForLoginScreen()
                .enterLogin(TestData.INVALID_LOGIN)
                .enterPassword(TestData.VALID_PASSWORD)
                .tapLoginButtonExpectingError();
    }
}