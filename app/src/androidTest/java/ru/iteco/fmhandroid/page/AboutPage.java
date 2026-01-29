package ru.iteco.fmhandroid.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.assertion.ViewAssertions;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;

public class AboutPage {

    private final int aboutTitle = R.id.about_version_title_text_view;
    private final int versionValue = R.id.about_version_value_text_view;
    private final int privacyPolicyLink = R.id.about_privacy_policy_value_text_view;
    private final int termsOfUseLink = R.id.about_terms_of_use_value_text_view;

    public void checkAboutScreenOpened() {
        Allure.step("Check About screen is opened");
            onView(withId(aboutTitle))
                    .check(ViewAssertions.matches(isDisplayed()));
    }

    public void checkVersionDisplayed() {
        Allure.step("Check app version is displayed");
            onView(withId(versionValue))
                    .check(ViewAssertions.matches(isDisplayed()));
    }

    public void checkPrivacyPolicyLinkDisplayed() {
        Allure.step("Check Privacy Policy link is displayed");
            onView(withId(privacyPolicyLink))
                    .check(ViewAssertions.matches(isDisplayed()));
    }

    public void checkTermsOfUseLinkDisplayed() {
        Allure.step("Check Terms of Use link is displayed");
            onView(withId(termsOfUseLink))
                    .check(ViewAssertions.matches(isDisplayed()));
    }
}
