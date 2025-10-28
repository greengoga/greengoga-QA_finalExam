package ru.iteco.fmhandroid.utils

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.IdlingResource
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.Matcher

class IdlingResources(
    private val matcher: Matcher<View>
) : IdlingResource {

    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null

    override fun getName(): String = "ViewAppearedIdlingResource"

    override fun isIdleNow(): Boolean {
        val activity = getCurrentActivity() ?: return false
        val rootView = activity.window.decorView.rootView

        val match = breadthFirstSearch(rootView, matcher)
        if (match) {
            callback?.onTransitionToIdle()
            return true
        }
        return false
    }

    override fun registerIdleTransitionCallback(cb: IdlingResource.ResourceCallback) {
        this.callback = cb
    }

    private fun getCurrentActivity(): Activity? {
        val activities = ActivityLifecycleMonitorRegistry.getInstance()
            .getActivitiesInStage(Stage.RESUMED)
        return activities.firstOrNull()
    }

    private fun breadthFirstSearch(root: View, matcher: Matcher<View>): Boolean {
        val queue: ArrayDeque<View> = ArrayDeque()
        queue.add(root)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (matcher.matches(current)) return true
            if (current is ViewGroup) {
                for (i in 0 until current.childCount) {
                    queue.add(current.getChildAt(i))
                }
            }
        }
        return false
    }
}