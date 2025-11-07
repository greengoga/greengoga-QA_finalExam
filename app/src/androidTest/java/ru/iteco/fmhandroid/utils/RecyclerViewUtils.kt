package ru.iteco.fmhandroid.utils

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Matcher

fun getTextFromRecyclerViewItem(
    activity: Activity,
    recyclerViewId: Int,
    textViewId: Int,
    position: Int
): String {
    var text = ""
    activity.runOnUiThread {
        val recyclerView = activity.findViewById<RecyclerView>(recyclerViewId)
        val holder = recyclerView.findViewHolderForAdapterPosition(position)
        val textView = holder?.itemView?.findViewById<TextView>(textViewId)
        text = textView?.text?.toString() ?: ""
    }
    return text
}

fun scrollToEndOfRecyclerView(recyclerViewId: Int, activity: Activity) {
    activity.runOnUiThread {
        val recyclerView = activity.findViewById<RecyclerView>(recyclerViewId)
        recyclerView.scrollToPosition(recyclerView.adapter?.itemCount?.minus(1) ?: 0)
    }
}