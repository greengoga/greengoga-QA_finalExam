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
import ru.iteco.fmhandroid.page.ControlPanelPage;
import ru.iteco.fmhandroid.page.LoginPage;
import ru.iteco.fmhandroid.page.MainPage;
import ru.iteco.fmhandroid.page.NewsPage;

@Epic("News Management")
@Feature("News Feed")
@LargeTest
@RunWith(AndroidJUnit4.class)
public class NewsTest {

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule(ScreenshotRule.Mode.FAILURE, "ss_on_failure");
    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();
    private final NewsPage newsPage = new NewsPage();

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
    @Story("Navigation to News")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the news list is displayed after navigating from the main menu")
    public void tc004_openNewsFromMainMenu() {
        mainPage.goToNews();
        newsPage.checkNewsListDisplayed();
    }

    @Test
    @Story("News Card Expansion")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that clicking on a news item expands it to show the full description")
    public void tc005_openNewsCard() {
        mainPage.goToNews();
        newsPage.checkNewsListDisplayed();
        newsPage.openFirstNewsCard();
    }

    @Test
    @Story("Clear News List And Check For Empty Message")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that deleting all items results in an empty list message")
    public void tc006_emptyNewsList() {
        mainPage.goToNews();

        ControlPanelPage controlPanelPage = newsPage.openControlPanel();

        controlPanelPage
                .deleteAllNews()
                .checkEmptyNewsListMessageDisplayed();
    }
}
