package com.cniekirk.kreddit.widgets.gesture

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.cniekirk.kreddit.R
import com.cniekirk.kreddit.ui.subreddit.uimodel.GestureSubmissionData
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData

class GestureActionLayout(context: Context, attributeSet: AttributeSet):
    FrameLayout(context, attributeSet) {

    // The actual content being displayed
    private lateinit var gestureViewChild: View
    // The overlay to provide gesture based actions
    private var foregroundDrawable: ForegroundDrawable

    private lateinit var gestureSubmissionData: GestureSubmissionData

    private lateinit var gestureActionManager: GestureActionManager

    private lateinit var gestureActionData: GestureActionData

    private lateinit var gestureActionIcons: List<GestureAction>

    private var foregroundDrawableHeightRatio = 1.0f

    private var shouldForegroundBeDrawn = false

    init {

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.GestureActionLayout)
        foregroundDrawableHeightRatio = typedArray.getFloat(R.styleable.GestureActionLayout_item_expand_ratio, 1.0f)
        typedArray.recycle()

        val positiveColour = ResourcesCompat.getColor(resources, R.color.colorTeal, null)
        val negativeColour = ResourcesCompat.getColor(resources, R.color.colorRed, null)

        foregroundDrawable = ForegroundDrawable(
            ColorDrawable(Color.DKGRAY),
            positiveColour,
            negativeColour
        )
        foregroundDrawable.alpha = 0
        foregroundDrawable.bounds = Rect(left, top, right, bottom)
        foregroundDrawable.callback = this

        val viewBounds = Rect()

        viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {

            override fun onGlobalLayout() {

                gestureActionData = GestureActionData(
                    initialMeasuredHeight = measuredHeight,
                    layoutWidth = width,
                    layoutHeight = height,
                    gestureSubmissionData = null,
                    foregroundDrawableHeightRatio = foregroundDrawableHeightRatio)

                gestureActionData.gestureSubmissionData = gestureSubmissionData

                // Create GestureActionManager with values
                gestureActionManager = GestureActionManager(foregroundDrawable, gestureActionIcons,
                    gestureActionData, onShowAnimationUpdate = {

                    // On show animation updated
                    val update = it.getAnimatedValue("VIEW_HEIGHT") as Float
                    val layoutParams = this@GestureActionLayout.layoutParams
                    layoutParams.height = update.toInt()
                    this@GestureActionLayout.layoutParams = layoutParams
                    getLocalVisibleRect(viewBounds)
                    foregroundDrawable.bounds = viewBounds
                    // Maybe need to call invalidate()

                }, onHideAnimationUpdate = {

                    // On hide animation updated
                    val update = it.getAnimatedValue("VIEW_HEIGHT") as Float
                    val layoutParams = this@GestureActionLayout.layoutParams
                    layoutParams.height = update.toInt()
                    this@GestureActionLayout.layoutParams = layoutParams
                    getLocalVisibleRect(viewBounds)
                    foregroundDrawable.bounds = viewBounds

                }, onHideAnimationComplete = {

                    shouldForegroundBeDrawn = false
                    invalidate()

                })

                // So only one DrawController instance is created
                viewTreeObserver.removeOnGlobalLayoutListener(this)

            }

        })

    }

    fun setSubmissionData(gestureSubmissionData: GestureSubmissionData) {
        this.gestureSubmissionData = gestureSubmissionData
    }

    fun setActionIcons(actionIcons: List<GestureAction>) {
        this.gestureActionIcons = actionIcons
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        if (shouldForegroundBeDrawn) {

            // Delegate drawing to controller
            gestureActionManager.drawManager.draw(canvas)

        }
    }

    /**
     * Represents the drawable drawn on top of the content in the layout that
     * provides gesture driven actions
     */
    class ForegroundDrawable(foregroundColourDrawable: ColorDrawable,
                             private val positiveColour: Int,
                             private val negativeColour: Int):
        LayerDrawable(arrayOf(foregroundColourDrawable)) {

        private var changeColourAnimator =  ValueAnimator()
        private var alphaColourAnimator =  ValueAnimator()

        private val foregroundColourDrawable: ColorDrawable
            get() = getDrawable(0) as ColorDrawable

        fun animateColourTransition(colourInt: Int) {

            if (changeColourAnimator.isRunning)
                return

            changeColourAnimator = ValueAnimator.ofArgb(foregroundColourDrawable.color, colourInt)
            changeColourAnimator.addUpdateListener {
                foregroundColourDrawable.color = it.animatedValue as Int
            }
            changeColourAnimator.duration = 130
            changeColourAnimator.interpolator = FastOutSlowInInterpolator()
            changeColourAnimator.start()

        }

    }

    fun displayActions() {

        shouldForegroundBeDrawn = true
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

        // Refactor to use list size instead of assuming 4
        if (touchX > 0 && touchX <= itemWidth) {

            foregroundDrawable.animateColourTransition(gestureActionIcons[0].colour)

        } else if (touchX > itemWidth && touchX <= (itemWidth * 2)) {

            foregroundDrawable.animateColourTransition(gestureActionIcons[1].colour)

        } else if (touchX > (itemWidth * 2) && touchX <= (itemWidth * 3)) {

            foregroundDrawable.animateColourTransition(gestureActionIcons[2].colour)

        } else if (touchX > (itemWidth * 3) && touchX <= (itemWidth * 4)) {

            foregroundDrawable.animateColourTransition(gestureActionIcons[3].colour)

        }

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