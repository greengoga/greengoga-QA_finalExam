package ru.iteco.fmhandroid.ui;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.iteco.fmhandroid.data.TestData;
import ru.iteco.fmhandroid.page.ControlPanelPage;
import ru.iteco.fmhandroid.page.LoginPage;
import ru.iteco.fmhandroid.page.MainPage;

@RunWith(AndroidJUnit4.class)
public class ControlPanelTest {

    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();
    private final ControlPanelPage controlPanelPage = new ControlPanelPage();

    @Test
    public void tc007_openControlPanel() {
        loginPage
                .waitForLoginScreen()
                .enterLogin(TestData.VALID_LOGIN)
                .enterPassword(TestData.VALID_PASSWORD)
                .tapLoginButton();

        mainPage.goToControlPanel();

        controlPanelPage.checkControlPanelOpened();
    }

    @Test
    public void tc010_createNews() {
        loginPage
                .waitForLoginScreen()
                .enterLogin(TestData.VALID_LOGIN)
                .enterPassword(TestData.VALID_PASSWORD)
                .tapLoginButton();

        mainPage.goToControlPanel();

        controlPanelPage.createNews(
                TestData.NEWS_TITLE,
                TestData.NEWS_DESCRIPTION
        );

        controlPanelPage.checkNewsWithTitleDisplayed(TestData.NEWS_TITLE);
    }
    @Test
    public void tc014_createNewsWithEmptyFields() {
        loginPage
                .waitForLoginScreen()
                .enterLogin(TestData.VALID_LOGIN)
                .enterPassword(TestData.VALID_PASSWORD)
                .tapLoginButton();

        mainPage.goToControlPanel();

        controlPanelPage.tapAddNews()
                         .saveNews();

        controlPanelPage.checkControlPanelOpened();
    }

    @Test
    public void tc015_cancelNewsCreation() {
        loginPage
                .waitForLoginScreen()
                .enterLogin(TestData.VALID_LOGIN)
                .enterPassword(TestData.VALID_PASSWORD)
                .tapLoginButton();

        mainPage.goToControlPanel();

        controlPanelPage.tapAddNews()
                         .fillNewsTitle(TestData.NEWS_TITLE)
                         .fillNewsDescription(TestData.NEWS_DESCRIPTION)
                         .cancelNewsCreation();

        controlPanelPage.checkControlPanelOpened();
    }
}
