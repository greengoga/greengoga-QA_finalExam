package ru.iteco.fmhandroid.page;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.action.ViewActions;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import io.qameta.allure.Step;
import ru.iteco.fmhandroid.R;

public class QuotePage {

    private final int recyclerView = R.id.our_mission_item_list_recycler_view;
    private final int descriptionText = R.id.our_mission_item_description_text_view;

    @Step("Expand first quote card")
    public void expandFirstQuote() {
        onView(withId(recyclerView))
                .perform(actionOnItemAtPosition(0, ViewActions.click()));
    }

    @Step("Check description of first quote is displayed")
    public void checkFirstQuoteDescriptionDisplayed() {
        onView(withId(recyclerView))
                .check(matches(isDisplayed()));

        onView(
                withRecyclerView(recyclerView)
                        .atPositionOnView(0, descriptionText)
        ).check(matches(isDisplayed()));
    }

    public static RecyclerViewMatcher withRecyclerView(int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static class RecyclerViewMatcher {
        private final int recyclerViewId;

        public RecyclerViewMatcher(int recyclerViewId) {
            this.recyclerViewId = recyclerViewId;
        }

        public Matcher<View> atPositionOnView(int position, int targetViewId) {
            return new TypeSafeMatcher<View>() {
                View childView;

                @Override
                public void describeTo(Description description) {
                    description.appendText("RecyclerView(" + recyclerViewId + ") at position " + position);
                }

                @Override
                protected boolean matchesSafely(View view) {
                    if (childView == null) {
                        View rootView = view.getRootView();
                        RecyclerView recyclerView = rootView.findViewById(recyclerViewId);
                        if (recyclerView == null) return false;

                        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                        if (viewHolder == null) return false;

                        View itemView = viewHolder.itemView;
                        childView = (targetViewId == -1) ? itemView : itemView.findViewById(targetViewId);
                    }
                    return view == childView;
                }
            };
        }
    }
}
