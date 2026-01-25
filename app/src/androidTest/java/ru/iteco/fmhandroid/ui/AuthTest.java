package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.android.rules.ScreenshotRule;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.data.TestData;
import ru.iteco.fmhandroid.page.LoginPage;
import ru.iteco.fmhandroid.page.MainPage;

@Epic("User Management")
@Feature("Authentication")
@LargeTest
@RunWith(AndroidJUnit4.class)
public class AuthTest {

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule(ScreenshotRule.Mode.FAILURE, "ss_on_failure");
    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Before
    public void logoutIfNeeded() {
        if (mainPage.isMainScreenDisplayed()) {
            mainPage.logout();
        }
    }

    @Before
    public void setUp() {
        activityRule.getScenario().onActivity(activity ->
                decorView = activity.getWindow().getDecorView()
        );
    }

    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();
    private View decorView;

    @Test
    @Story("Login with valid credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a user can successfully log in with valid login and password")
    public void tc001_validLogin() {
        loginPage
                .waitForLoginScreen()
                .enterLogin(TestData.VALID_LOGIN)
                .enterPassword(TestData.VALID_PASSWORD)
                .tapLoginButton();
    }

    @Test
    @Story("Login with empty fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify the error message when login and password fields are left blank")
    public void tc002_emptyLoginAndPassword() {
        loginPage
                .waitForLoginScreen()
                .enterLogin("")
                .enterPassword("")
                .tapLoginButtonExpectingError();

        onView(withText(R.string.empty_login_or_password))
                .inRoot(withDecorView(not(is(decorView))))
                .check(matches(isDisplayed()));
    }

    @Test
    @Story("Login with invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the error message when entering non-existent user data")
    public void tc003_invalidLogin() {
        loginPage
                .waitForLoginScreen()
                .enterLogin(TestData.INVALID_LOGIN)
                .enterPassword(TestData.VALID_PASSWORD)
                .tapLoginButtonExpectingError();

        onView(withText(R.string.error))
                .inRoot(withDecorView(not(is(decorView))))
                .check(matches(isDisplayed()));
    }
}