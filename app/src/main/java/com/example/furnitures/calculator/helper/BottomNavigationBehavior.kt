package com.example.furnitures.calculator.helper

import android.content.Context
import com.google.android.material.bottomappbar.BottomAppBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import java.lang.Float.max
import java.lang.Float.min

/*
Writing a custom BottomAppBarBehavior Class
https://android.jlelse.eu/scroll-your-bottom-navigation-view-away-with-10-lines-of-code-346f1ed40e9e
+ Give BottomAppBar layout_behavior=".calculator.helper.BottomNavigationBehavior"
+ Give FrameLayout  layout_behavior="@string/appbar_scrolling_view_behavior"
 */

// Instruct the CoordinatorLayout, that we care about VERTICAL SCROLL EVENTS
class BottomNavigationBehavior<V : View>(context: Context, attrs: AttributeSet) : BottomAppBar.Behavior() {
    override fun onStartNestedScroll(coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout, child: BottomAppBar, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    // dy = the scroll change delta
    // void setTranslationY = sets the Y value from max 0,
    // to the childer.getTranslationY Height + scroll change delta
    override fun onNestedPreScroll(coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout, child: BottomAppBar, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        child.translationY = max(0f, min(child.height.toFloat(), child.translationY + dy))
    }
}