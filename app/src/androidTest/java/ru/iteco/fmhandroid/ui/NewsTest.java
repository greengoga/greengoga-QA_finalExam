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
public class NewsTest {

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
    public void tc004_openNewsFromMainMenu() {
        mainPage.goToNews();
        newsPage.checkNewsListDisplayed();
    }

    //only makes sense after running the news creation test
    @Test
    public void tc005_openNewsCard() {
        mainPage.goToNews();
        newsPage.checkNewsListDisplayed();
        newsPage.openFirstNewsCard();
    }

    //Presently works only using Thread.sleep()
    @Test
    public void tc006_emptyNewsList() {
        mainPage.goToNews();

        ControlPanelPage controlPanelPage = newsPage.openControlPanel();

        controlPanelPage
                .deleteAllNews()
                .checkEmptyNewsListMessageDisplayed();
    }
}
