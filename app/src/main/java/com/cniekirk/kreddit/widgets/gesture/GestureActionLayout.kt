package com.cniekirk.kreddit.widgets.gesture

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.content.res.ResourcesCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.cniekirk.kreddit.R
import com.cniekirk.kreddit.ui.subreddit.uimodel.GestureSubmissionData
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData

class GestureActionLayout(context: Context, attributeSet: AttributeSet):
    FrameLayout(context, attributeSet), GestureActionManager.AnimationListener {

    private val initialMeasuredHeight: Int by lazy { measuredHeight }
    private val paint = Paint()

    // The actual content being displayed
    private lateinit var gestureViewChild: View
    // The overlay to provide gesture based actions
    private var foregroundDrawable: ForegroundDrawable

    private lateinit var gestureSubmissionData: GestureSubmissionData

    private var gestureActionManager: GestureActionManager

    private var gestureActionData: GestureActionData

    private var foregroundDrawableHeightRatio = 1.0f

    private var revealAlpha = 0

    private var textBackgroundAlpha = 0

    private var textRevealHeight = 0

    private var shouldForegroundBeDrawn = false

    init {

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.GestureActionLayout)
        foregroundDrawableHeightRatio = typedArray.getFloat(R.styleable.GestureActionLayout_item_expand_ratio, 1.0f)
        typedArray.recycle()

        val positiveColour = ResourcesCompat.getColor(resources, R.color.colorTeal, null)
        val negativeColour = ResourcesCompat.getColor(resources, R.color.colorRed, null)

        foregroundDrawable = ForegroundDrawable(
            ColorDrawable(positiveColour),
            ColorDrawable(Color.DKGRAY),
            positiveColour,
            negativeColour
        )
        foregroundDrawable.alpha = 0
        foregroundDrawable.bounds = Rect(left, top, right, bottom)
        foregroundDrawable.callback = this

        gestureActionData = GestureActionData(variableTextAlpha = 0,
            variableBackgroundAlpha = 0, variableTextRevealHeight = 0,
            initialMeasuredHeight = initialMeasuredHeight,
            layoutWidth = width, layoutHeight = height, gestureSubmissionData = null)

        // Create GestureActionManager with values
        gestureActionManager = GestureActionManager(context, this, foregroundDrawable, gestureActionData)

    }

    fun setSubmissionData(gestureSubmissionData: GestureSubmissionData) {
        this.gestureSubmissionData = gestureSubmissionData
        // Refactored
        gestureActionData.gestureSubmissionData = gestureSubmissionData
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        // Delegate drawing to controller
        gestureActionManager.drawManager.draw(canvas)

        if (shouldForegroundBeDrawn) {

            foregroundDrawable.draw(canvas)

            val clipBounds = canvas.clipBounds
            clipBounds.inset(0, -initialMeasuredHeight)
            canvas.clipRect(clipBounds)

            paint.color = Color.WHITE
            paint.alpha = textBackgroundAlpha
            paint.flags = Paint.ANTI_ALIAS_FLAG

            canvas.drawRect(0f, -initialMeasuredHeight.toFloat(), width.toFloat(), 0f, paint)

            paint.color = Color.BLACK
            paint.alpha = revealAlpha
            paint.textSize = 55f
            paint.textAlign = Paint.Align.CENTER
            paint.typeface = Typeface.DEFAULT_BOLD

            // ((paint.ascent() - paint.descent()) / 2) is the distance from center to baseline of drawn text
            val textY = -textRevealHeight.toFloat() - ((paint.ascent() - paint.descent()) / 2)

            canvas.drawText("\"${gestureSubmissionData.submissionText}\"",
                canvas.width / 2f, textY, paint)

        }
    }

    /**
     * Represents the drawable drawn on top of the content in the layout that
     * provides gesture driven actions
     */
    class ForegroundDrawable(foregroundColourDrawable: ColorDrawable,
                             grayFillerDrawable: ColorDrawable,
                             private val positiveColour: Int,
                             private val negativeColour: Int):
        LayerDrawable(arrayOf(foregroundColourDrawable, grayFillerDrawable)) {

        private lateinit var swipeTransitionAnimator: ObjectAnimator
        private lateinit var changeColourAnimator: ValueAnimator

        private var isPositive: Boolean = true

        private val foregroundColourDrawable: ColorDrawable
            get() = getDrawable(0) as ColorDrawable

        private val greyFillerDrawable: ColorDrawable
            get() = getDrawable(1) as ColorDrawable

        fun animateSwipeGesture(alpha: Int) {

            if (alpha == greyFillerDrawable.alpha)
                return

            if (swipeTransitionAnimator != null)
                swipeTransitionAnimator.cancel()


            swipeTransitionAnimator = ObjectAnimator.ofInt(
                greyFillerDrawable,
                "alpha",
                greyFillerDrawable.alpha,
                alpha
            )

            swipeTransitionAnimator.duration = 200
            swipeTransitionAnimator.interpolator = FastOutSlowInInterpolator()
            swipeTransitionAnimator.start()

        }

        fun animateColourChange(isPositive: Boolean) {

            if(isPositive == this.isPositive)
                return

            this.isPositive = isPositive

            if (changeColourAnimator != null)
                changeColourAnimator.cancel()

            changeColourAnimator = ValueAnimator.ofArgb(foregroundColourDrawable.color,
                if (isPositive) positiveColour else negativeColour)
            changeColourAnimator.addUpdateListener {
                foregroundColourDrawable.color = it.animatedValue as Int
            }

            changeColourAnimator.duration = 200
            changeColourAnimator.interpolator = FastOutSlowInInterpolator()
            changeColourAnimator.start()

        }

    }

    fun displayActions() {

        shouldForegroundBeDrawn = true
        val viewBounds = Rect()

        val viewHeightAnimator = ValueAnimator.ofFloat(initialMeasuredHeight.toFloat(), (initialMeasuredHeight * foregroundDrawableHeightRatio))
        viewHeightAnimator.addUpdateListener {

            val update = it.animatedValue as Float
            val layoutParams = this.layoutParams
            layoutParams.height = update.toInt()
            this.layoutParams = layoutParams
            getLocalVisibleRect(viewBounds)
            foregroundDrawable.bounds = viewBounds

        }

        val revealAlphaAnimator = ValueAnimator.ofInt(0, 255)
        revealAlphaAnimator.addUpdateListener {
            revealAlpha = it.animatedValue as Int
        }

        val textBackgroundAlphaAnimator = ValueAnimator.ofInt(0, 235)
        textBackgroundAlphaAnimator.addUpdateListener {
            textBackgroundAlpha = it.animatedValue as Int
        }

        val textHeightAnimator = ValueAnimator.ofInt(0, initialMeasuredHeight / 2)
        textHeightAnimator.addUpdateListener {
            textRevealHeight = it.animatedValue as Int
        }

        val actionsAlphaAnimator = ObjectAnimator.ofInt(
            foregroundDrawable,
            "alpha",
            0,
            255
        )

        val animatorSet = AnimatorSet()
        animatorSet.duration = 300
        animatorSet.interpolator = FastOutSlowInInterpolator()
        animatorSet.playTogether(viewHeightAnimator, actionsAlphaAnimator,
            revealAlphaAnimator, textBackgroundAlphaAnimator, textHeightAnimator)
        animatorSet.start()

    }

    fun setSwipeTranslation(translationY: Float) {

        if (!shouldForegroundBeDrawn)
            return

        // If user swipes down
        if (translationY < 0) {

            foregroundDrawable.animateColourChange(false)

        } else {

            foregroundDrawable.animateColourChange(true)

        }

        val translationYabs = Math.abs(translationY)

        if (translationYabs > (height / 3)) {
            foregroundDrawable.animateSwipeGesture(0)
        } else {
            foregroundDrawable.animateSwipeGesture(255)
        }


    }

    fun hideActions() {

        val viewBounds = Rect()

        val viewHeightAnimator = ValueAnimator.ofFloat(measuredHeight.toFloat(), initialMeasuredHeight.toFloat())
        viewHeightAnimator.addUpdateListener {

            val update = it.animatedValue as Float
            val layoutParams = this.layoutParams
            layoutParams.height = update.toInt()
            this.layoutParams = layoutParams
            getLocalVisibleRect(viewBounds)
            foregroundDrawable.bounds = viewBounds

        }

        val revealAlphaAnimator = ValueAnimator.ofInt( 255, 0)
        revealAlphaAnimator.addUpdateListener {
            revealAlpha = it.animatedValue as Int
        }

        val textBackgroundAlphaAnimator = ValueAnimator.ofInt(235, 0)
        textBackgroundAlphaAnimator.addUpdateListener {
            textBackgroundAlpha = it.animatedValue as Int
        }

        val textHeightAnimator = ValueAnimator.ofInt(initialMeasuredHeight / 2, 0)
        textHeightAnimator.addUpdateListener {
            textRevealHeight = it.animatedValue as Int
        }

        val actionsAlphaAnimator = ObjectAnimator.ofInt(
            foregroundDrawable,
            "alpha",
            255,
            0
        )

        val animatorSet = AnimatorSet()
        animatorSet.duration = 300
        animatorSet.interpolator = FastOutSlowInInterpolator()
        animatorSet.playTogether(viewHeightAnimator, actionsAlphaAnimator,
            revealAlphaAnimator, textBackgroundAlphaAnimator, textHeightAnimator)

        animatorSet.doOnEnd {
            // Completely remove foreground
            shouldForegroundBeDrawn = false
            invalidate()
        }

        animatorSet.start()

    }

    override fun onAnimationUpdated() {
        invalidate()
    }

    override fun invalidateDrawable(drawable: Drawable) {
        if (drawable == foregroundDrawable)
            invalidate()
        else
            super.invalidateDrawable(drawable)
    }

}