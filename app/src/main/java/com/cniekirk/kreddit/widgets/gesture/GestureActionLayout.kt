package com.cniekirk.kreddit.widgets.gesture

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.cniekirk.kreddit.R
import com.cniekirk.kreddit.ui.subreddit.uimodel.GestureSubmissionData
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData

/**
 * Custom view to provide ability to upvote/downvote and perform other actions on a Reddit submission
 *
 * As a side-note it was very well received amongst other developers in the Android developer community:
 * https://www.reddit.com/r/androiddev/comments/atvvc7/messing_about_my_first_custom_view_thoughts/
 */
class GestureActionLayout(context: Context, attributeSet: AttributeSet):
    ConstraintLayout(context, attributeSet) {

    // The overlay to provide gesture based actions
    private var foregroundDrawable: ForegroundDrawable

    private lateinit var gestureActionManager: GestureActionManager

    private lateinit var gestureActionData: GestureActionData

    private lateinit var gestureActionIcons: List<GestureAction>

    private var shouldForegroundBeDrawn = false

    init {

        val viewBounds = Rect()

        foregroundDrawable = ForegroundDrawable(ColorDrawable(Color.DKGRAY))
        foregroundDrawable.alpha = 0
        foregroundDrawable.callback = this

        viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {

            override fun onGlobalLayout() {

                getLocalVisibleRect(viewBounds)
                foregroundDrawable.bounds = viewBounds

                gestureActionData = GestureActionData(
                    initialMeasuredHeight = measuredHeight,
                    layoutWidth = width,
                    layoutHeight = height)

                // Create GestureActionManager with values
                gestureActionManager = GestureActionManager(foregroundDrawable, gestureActionIcons,
                    gestureActionData, onHideAnimationComplete = {

                    shouldForegroundBeDrawn = false
                    invalidate()

                })

                // So only one DrawController instance is created
                viewTreeObserver.removeOnGlobalLayoutListener(this)

            }

        })

    }

    private fun initGestureManager() {

        val viewBounds = Rect()

        gestureActionData = GestureActionData(
            initialMeasuredHeight = measuredHeight,
            layoutWidth = width,
            layoutHeight = height)

        getLocalVisibleRect(viewBounds)
        foregroundDrawable.bounds = viewBounds

        // Create GestureActionManager with values
        gestureActionManager = GestureActionManager(foregroundDrawable, gestureActionIcons,
            gestureActionData, onHideAnimationComplete = {

                shouldForegroundBeDrawn = false
                invalidate()

            })

    }

    fun setGestureActions(gestureActions: List<GestureAction>) {
        this.gestureActionIcons = gestureActions
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        if (shouldForegroundBeDrawn) {

            // Delegate drawing to controller
            gestureActionManager.draw(canvas)

        }
    }

    /**
     * Represents the drawable drawn on top of the content in the layout that
     * provides gesture driven actions
     */
    class ForegroundDrawable(foregroundColourDrawable: ColorDrawable):
        LayerDrawable(arrayOf(foregroundColourDrawable)) {

        private var changeColourAnimator =  ValueAnimator()

        private val foregroundColourDrawable: ColorDrawable
            get() = getDrawable(0) as ColorDrawable

        fun animateColourTransition(colourInt: Int) {

            if (changeColourAnimator.isRunning)
                return

            changeColourAnimator = ValueAnimator.ofArgb(foregroundColourDrawable.color, colourInt)

            with(changeColourAnimator) {

                duration = 200
                interpolator = FastOutSlowInInterpolator()

                changeColourAnimator.addUpdateListener {
                    foregroundColourDrawable.color = it.animatedValue as Int
                }

                start()

            }

        }

    }

    fun displayActions() {

        initGestureManager()

        // Controls the draw calls to only draw to canvas when we need
        shouldForegroundBeDrawn = true
        invalidate()
        gestureActionManager.animateShow()

    }

    // Work in progress
    fun setAction(touchX: Float) {

        if (!shouldForegroundBeDrawn)
            return

        val itemWidth = (measuredWidth / gestureActionIcons.size)

        // Cannot be wider than the device width
        if (touchX > measuredWidth)
            return

        // Animate to the appropriate colour
        foregroundDrawable.animateColourTransition(gestureActionIcons[Math.floor(touchX / itemWidth.toDouble()).toInt()].colour)

    }

    fun hideActions() {

        gestureActionManager.animateHide()

    }

    override fun invalidateDrawable(drawable: Drawable) {
        if (drawable == foregroundDrawable)
            invalidate()
        else
            super.invalidateDrawable(drawable)
    }

}