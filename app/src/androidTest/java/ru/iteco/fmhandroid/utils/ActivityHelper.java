package ru.iteco.fmhandroid.utils;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import java.util.Collection;

public class ActivityHelper {

    public static Activity getCurrentActivity() {
        final Activity[] activity = new Activity[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            Collection<Activity> resumedActivities =
                    ActivityLifecycleMonitorRegistry.getInstance()
                            .getActivitiesInStage(Stage.RESUMED);
            if (!resumedActivities.isEmpty()) {
                activity[0] = resumedActivities.iterator().next();
            }
        });
        return activity[0];
    }
}