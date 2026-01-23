package ru.iteco.fmhandroid.utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.view.View;

import androidx.test.espresso.NoMatchingViewException;

import org.hamcrest.Matcher;

public class Wait {

    public static void waitForView(Matcher<View> matcher, long timeoutMs) {
        long start = System.currentTimeMillis();

        do {
            try {
                onView(matcher).check(matches(isDisplayed()));
                return;
            } catch (NoMatchingViewException | AssertionError e) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {}
            }
        } while (System.currentTimeMillis() - start < timeoutMs);

        throw new AssertionError("View not displayed within timeout");
    }
}