package ru.iteco.fmhandroid.utils

import android.view.View
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import android.view.WindowManager
import android.os.IBinder

class ToastMatcher(private val decorView: View?) : TypeSafeMatcher<Root>() {

    override fun describeTo(description: Description) {
        description.appendText("is toast")
    }

    override fun matchesSafely(root: Root): Boolean {
        val type: Int = root.windowLayoutParams.get().type
        if (type == WindowManager.LayoutParams.TYPE_APPLICATION) {
            val windowToken: IBinder = root.decorView.windowToken
            val appToken: IBinder = root.decorView.applicationWindowToken
            if (windowToken === appToken) {
                if (decorView != null && root.decorView !== decorView) {
                    return true
                }
            }
        }
        return false
    }
}