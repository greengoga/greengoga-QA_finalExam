package ru.iteco.fmhandroid.ui;

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
import ru.iteco.fmhandroid.data.TestData;
import ru.iteco.fmhandroid.page.LoginPage;
import ru.iteco.fmhandroid.page.MainPage;
import ru.iteco.fmhandroid.page.QuotePage;

@Epic("App Content")
@Feature("Mission Quotes")
@LargeTest
@RunWith(AndroidJUnit4.class)
public class QuoteTest {

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule(ScreenshotRule.Mode.FAILURE, "ss_on_failure");
    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();
    private final QuotePage quotePage = new QuotePage();

    @Before
    public void loginIfNeeded() {
        if (loginPage.waitForLoginScreenOrMainScreen()) {
            loginPage
                    .enterLogin(TestData.VALID_LOGIN)
                    .enterPassword(TestData.VALID_PASSWORD)
                    .tapLoginButton();
        }
    }

    @Test
    @Story("Expand Mission Quotes")
    @Severity(SeverityLevel.NORMAL)
    @Description(value = "Verify that the mission quotes can be expanded to show full motivational text")
    public void tc011_openQuoteScreen() {
        mainPage.goToQuotes();

        quotePage.expandFirstQuote();
        quotePage.checkFirstQuoteDescriptionDisplayed();
    }
}
