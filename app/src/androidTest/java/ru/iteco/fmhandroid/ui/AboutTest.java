package ru.iteco.fmhandroid.ui;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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
import ru.iteco.fmhandroid.page.AboutPage;
import ru.iteco.fmhandroid.page.LoginPage;
import ru.iteco.fmhandroid.page.MainPage;

@Epic("General App Information")
@Feature("About Screen")
@RunWith(AndroidJUnit4.class)
public class AboutTest {

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule(ScreenshotRule.Mode.FAILURE, "ss_on_failure");
    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();
    private final AboutPage aboutPage = new AboutPage();

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
    @Story("View Application Information")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the About screen displays the version, privacy policy, and terms of use")
    public void tc009_openAboutScreen() {
        mainPage.goToAbout();

        aboutPage.checkAboutScreenOpened();
        aboutPage.checkVersionDisplayed();
        aboutPage.checkPrivacyPolicyLinkDisplayed();
        aboutPage.checkTermsOfUseLinkDisplayed();
    }
}