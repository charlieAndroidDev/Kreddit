package com.cniekirk.kreddit.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.core.view.NestedScrollingChild2
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView


class RecyclerViewWithNestedProgrammaticScrolling(context: Context, @Nullable attrs: AttributeSet) :
    RecyclerView(context, attrs), NestedScrollingChild2 {

    override fun smoothScrollBy(dx: Int, dy: Int) {
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH)
        super.smoothScrollBy(dx, dy)
    }

    override fun smoothScrollBy(dx: Int, dy: Int, interpolator: android.view.animation.Interpolator?) {
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH)
        super.smoothScrollBy(dx, dy, interpolator)
    }

    override fun smoothScrollToPosition(position: Int) {
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH)
        super.smoothScrollToPosition(position)
    }

    override fun scrollBy(x: Int, y: Int) {
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH)
        super.scrollBy(x, y)
    }

    override fun scrollTo(x: Int, y: Int) {
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH)
        super.scrollTo(x, y)
    }

    override fun scrollToPosition(position: Int) {
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH)
        super.scrollToPosition(position)
    }
}