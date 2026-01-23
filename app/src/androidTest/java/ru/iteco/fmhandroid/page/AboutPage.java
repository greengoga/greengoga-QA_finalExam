package ru.iteco.fmhandroid.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.assertion.ViewAssertions;

import io.qameta.allure.Step;
import ru.iteco.fmhandroid.R;

public class AboutPage {

    private final int aboutTitle = R.id.about_version_title_text_view;
    private final int versionValue = R.id.about_version_value_text_view;
    private final int privacyPolicyLink = R.id.about_privacy_policy_value_text_view;
    private final int termsOfUseLink = R.id.about_terms_of_use_value_text_view;

    @Step("Check About screen is opened")
    public void checkAboutScreenOpened() {
        onView(withId(aboutTitle))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Step("Check app version is displayed")
    public void checkVersionDisplayed() {
        onView(withId(versionValue))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Step("Check Privacy Policy link is displayed")
    public void checkPrivacyPolicyLinkDisplayed() {
        onView(withId(privacyPolicyLink))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Step("Check Terms of Use link is displayed")
    public void checkTermsOfUseLinkDisplayed() {
        onView(withId(termsOfUseLink))
                .check(ViewAssertions.matches(isDisplayed()));
    }
}
