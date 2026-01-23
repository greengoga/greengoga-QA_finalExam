package ru.iteco.fmhandroid.ui;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.iteco.fmhandroid.data.TestData;
import ru.iteco.fmhandroid.page.ControlPanelPage;
import ru.iteco.fmhandroid.page.LoginPage;
import ru.iteco.fmhandroid.page.MainPage;
import ru.iteco.fmhandroid.page.NewsPage;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class ControlPanelTest {

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
    public void tc007_openControlPanel() {
            MainPage mainPage = new MainPage();
        mainPage.goToNews();

           NewsPage newsPage = new NewsPage();
        ControlPanelPage controlPanelPage = newsPage.openControlPanel();
        controlPanelPage.checkControlPanelTitle();
    }

    @Test
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