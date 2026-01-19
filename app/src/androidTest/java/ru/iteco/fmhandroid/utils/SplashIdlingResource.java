package ru.iteco.fmhandroid.utils;

import androidx.test.espresso.IdlingResource;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import ru.iteco.fmhandroid.R;

public class SplashIdlingResource implements IdlingResource {

    private ResourceCallback callback;

    @Override
    public String getName() {
        return SplashIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        try {
            onView(withId(R.id.splash_screen_circular_progress_indicator))
                    .check(matches(isDisplayed()));
            return false;
        } catch (Exception e) {
            if (callback != null) {
                callback.onTransitionToIdle();
            }
            return true;
        }
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }
}