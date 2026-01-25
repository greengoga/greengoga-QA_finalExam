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
@Feature("Control Panel")
@LargeTest
@RunWith(AndroidJUnit4.class)
public class ControlPanelTest {


    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule(ScreenshotRule.Mode.FAILURE, "ss_on_failure");
    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    private final LoginPage loginPage = new LoginPage();

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
    @Story("Open Control Panel")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify navigation to the News Control Panel from the News screen")
    public void tc007_openControlPanel() {
            MainPage mainPage = new MainPage();
        mainPage.goToNews();

           NewsPage newsPage = new NewsPage();
        ControlPanelPage controlPanelPage = newsPage.openControlPanel();
        controlPanelPage.checkControlPanelTitle();
    }

    @Test
    @Story("Create News Item")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a user can create a new news item with a unique title and description")
    public void tc010_createNews() {
        String uniqueTitle = TestData.NEWS_TITLE + "_" + System.currentTimeMillis();
        String uniqueDescription = TestData.NEWS_DESCRIPTION + "_" + System.currentTimeMillis();

        MainPage mainPage = new MainPage();
        mainPage.goToNews();

        ControlPanelPage controlPanelPage = new NewsPage()
                .openControlPanel()
                .createNews(uniqueTitle, uniqueDescription);

        controlPanelPage.checkNewsWithDescriptionDisplayed(uniqueDescription);
    }

    @Test
    @Story("Form Validation")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify validation error when trying to save news with empty mandatory fields")
    public void tc014_createNewsWithEmptyFields() {
        MainPage mainPage = new MainPage();
        mainPage.goToNews();

        NewsPage newsPage = new NewsPage();
        ControlPanelPage controlPanelPage = newsPage.openControlPanel();

        controlPanelPage.tapAddNews()
                .saveNews();

        controlPanelPage.checkEmptyFieldsMessage();
    }

    @Test
    @Story("Delete Specific News")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that a specific news item can be deleted by its title")
    public void tc015_deleteNews() {
        String uniqueTitle = TestData.NEWS_TITLE + "_" + System.currentTimeMillis();

        ControlPanelPage controlPanelPage = new MainPage()
                .goToNews()
                .openControlPanel();
        controlPanelPage.createNews(uniqueTitle, TestData.NEWS_DESCRIPTION);

        controlPanelPage.deleteNewsByTitle(uniqueTitle);
        controlPanelPage.checkNewsWithTitleNotDisplayed(uniqueTitle);
    }
}