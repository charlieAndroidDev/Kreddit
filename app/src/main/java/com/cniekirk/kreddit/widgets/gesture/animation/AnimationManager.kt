package com.cniekirk.kreddit.widgets.gesture.animation

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData

class AnimationManager(private val gestureActionData: GestureActionData,
                       private val animationListener: AnimationListener) {

    interface AnimationListener {

        fun onShowAnimationUpdated(valueAnimator: ValueAnimator)
        fun onHideAnimationUpdated(valueAnimator: ValueAnimator)
        fun onHideAnimationCompleted()

    }

    fun animateShow() {

        val textRevealAlpha = PropertyValuesHolder.ofInt("TEXT_REVEAL_ALPHA",
            gestureActionData.noAlpha, gestureActionData.fullAlpha)
        val textBackgroundAlpha = PropertyValuesHolder.ofInt("TEXT_BACKGROUND_ALPHA",
            gestureActionData.noAlpha, gestureActionData.opaqueAlpha)
        val textHeight = PropertyValuesHolder.ofInt("TEXT_HEIGHT",
            0, gestureActionData.initialMeasuredHeight / 2)
        val foregroundDrawableAlpha = PropertyValuesHolder.ofInt("FOREGROUND_DRAWABLE_ALPHA",
            gestureActionData.noAlpha, gestureActionData.fullAlpha)
        val viewHeight = PropertyValuesHolder.ofFloat("VIEW_HEIGHT", gestureActionData.initialMeasuredHeight.toFloat(),
            (gestureActionData.initialMeasuredHeight * gestureActionData.foregroundDrawableHeightRatio))

        val revealAnimator = ValueAnimator()
        revealAnimator.setValues(textRevealAlpha, textBackgroundAlpha, textHeight,
            foregroundDrawableAlpha, viewHeight)

        revealAnimator.duration = 300
        revealAnimator.interpolator = FastOutSlowInInterpolator()
        revealAnimator.addUpdateListener {
            animationListener.onShowAnimationUpdated(it)
        }
        revealAnimator.start()

    }

    fun animateHide() {

        val textHideAlpha = PropertyValuesHolder.ofInt("TEXT_REVEAL_ALPHA",
            gestureActionData.fullAlpha, gestureActionData.noAlpha)
        val textBackgroundAlpha = PropertyValuesHolder.ofInt("TEXT_BACKGROUND_ALPHA",
            gestureActionData.opaqueAlpha, gestureActionData.noAlpha)
        val textHeight = PropertyValuesHolder.ofInt("TEXT_HEIGHT",
            gestureActionData.initialMeasuredHeight / 2, 0)
        val foregroundDrawableAlpha = PropertyValuesHolder.ofInt("FOREGROUND_DRAWABLE_ALPHA",
            gestureActionData.fullAlpha, gestureActionData.noAlpha)
        val viewHeight = PropertyValuesHolder.ofFloat("VIEW_HEIGHT", (gestureActionData.initialMeasuredHeight * gestureActionData.foregroundDrawableHeightRatio),
            gestureActionData.initialMeasuredHeight.toFloat())

        val revealAnimator = ValueAnimator()
        revealAnimator.setValues(textHideAlpha, textBackgroundAlpha, textHeight,
            foregroundDrawableAlpha, viewHeight)

        revealAnimator.duration = 300
        revealAnimator.interpolator = FastOutSlowInInterpolator()
        revealAnimator.addUpdateListener {
            animationListener.onHideAnimationUpdated(it)
        }
        revealAnimator.start()

    }

}