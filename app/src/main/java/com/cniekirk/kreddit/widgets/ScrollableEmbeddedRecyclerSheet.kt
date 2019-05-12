package com.cniekirk.kreddit.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.NestedScrollingParent2
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.cniekirk.kreddit.widgets.ScrollableEmbeddedRecyclerSheet.SheetScrollChangeListener



class ScrollableEmbeddedRecyclerSheet(context: Context, attributeSet: AttributeSet):
    FrameLayout(context, attributeSet), NestedScrollingParent2 {

    private lateinit var childRecyclerView: RecyclerView
    private lateinit var currentState: ViewState
    private lateinit var scrollingAnimator: ValueAnimator
    private lateinit var scrollChangeListener: SheetScrollChangeListener
    var scrollingEnabled: Boolean = true
    var maxScrollY: Int = 0

    enum class ViewState {
        EXPANDED,
        DRAGGING,
        AT_MAX_SCROLL_Y
    }

    interface SheetScrollChangeListener {
        fun onScrollChange(newScrollY: Float)
    }

    init {

        if (translationY <= 0)
            currentState = ViewState.EXPANDED

        scrollingEnabled = true

    }

    fun addOnSheetScrollChangeListener(listener: SheetScrollChangeListener) {
        scrollChangeListener = listener
    }

    fun canScrollDownFurther(): Boolean {

        val canSheetScroll = translationY < maxScrollY
        val canRecyclerScroll = childRecyclerView.canScrollVertically(-1)

        return when (scrollingEnabled) {
            true -> canSheetScroll || canRecyclerScroll
            else -> canRecyclerScroll
        }

    }

    fun scrollTo(y: Int) {
        tryConsumeScrollY(translationY - y)
    }

    override fun scrollTo(x: Int, y: Int) {
        scrollTo(y)
    }

    fun smoothScrollTo(y: Int) {

        if (isSmoothScrollingNow())
            scrollingAnimator.cancel()

        if (translationY == y.toFloat())
            return

        scrollingAnimator = ValueAnimator.ofFloat(translationY, y.toFloat())
        scrollingAnimator.interpolator = FastOutSlowInInterpolator()
        scrollingAnimator.addUpdateListener {
            tryConsumeScrollY(translationY - it.animatedValue as Float)
        }
        scrollingAnimator.start()

    }

    fun scrollTo(y: Int, smoothScroll: Boolean) {

        when (smoothScroll) {
            true -> smoothScrollTo(y)
            false -> scrollTo(y)
        }

    }

    fun isSmoothScrollingNow(): Boolean {
        return scrollingAnimator.isStarted
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {

        if (childCount != 0) {
            throw AssertionError("Can only have 1 RecyclerView")
        }
        if (child !is RecyclerView) {
            throw AssertionError("Can only hold RecyclerView")
        }

        super.addView(child, index, params)

        childRecyclerView = child
        childRecyclerView.overScrollMode = View.OVER_SCROLL_NEVER
    }

    fun canScrollUpFurther(): Boolean {
        return translationY != 0f || childRecyclerView.canScrollVertically(1)
    }

    fun hasReachedTop(): Boolean {
        return translationY <= 0
    }

    fun tryConsumeScrollY(dy: Float): Float {

        val isDownwards = dy > 0f
        if (isDownwards) {

            if (!hasReachedTop()) {

                var adjusted = dy
                if (translationY - dy < 0) {

                    adjusted = translationY

                }

                adjustOffsetby(adjusted)
                return adjusted

            }

        } else {

            val canRecyclerScrollDown = childRecyclerView.canScrollVertically(-1)

            if (translationY < maxScrollY && !canRecyclerScrollDown) {

                var adjusted = dy

                if (translationY - dy > maxScrollY) {

                    adjusted = translationY - maxScrollY

                }

                adjustOffsetby(adjusted)
                return adjusted

            }

        }

        return 0f

    }

    private fun adjustOffsetby(dy: Float) {

        val transY = translationY - dy
        translationY = transY

        val state = if (!canScrollDownFurther()) {
            ViewState.AT_MAX_SCROLL_Y
        } else if (hasReachedTop()) {
            ViewState.EXPANDED
        } else {
            ViewState.DRAGGING
        }

        if (state != currentState)
            currentState = state

        scrollChangeListener.onScrollChange(translationY)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {

    }

    override fun onStopNestedScroll(target: View, type: Int) {

    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return onStartNestedScroll(child, target, axes)
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {

    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return scrollingEnabled
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        val consumedY = tryConsumeScrollY(dy.toFloat())
        consumed[1] = consumedY.toInt()
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {


    }


}