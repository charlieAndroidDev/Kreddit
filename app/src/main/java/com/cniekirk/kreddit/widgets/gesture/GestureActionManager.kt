package com.cniekirk.kreddit.widgets.gesture

import android.animation.ValueAnimator
import android.graphics.Canvas
import com.cniekirk.kreddit.widgets.gesture.animation.AnimationManager
import com.cniekirk.kreddit.widgets.gesture.draw.DrawManager
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData

/**
 * Class to manage the general operation of the control
 */
class GestureActionManager(private val foregroundDrawable: GestureActionLayout.ForegroundDrawable,
                           private val gestureIcons: List<GestureAction>,
                           private val gestureActionData: GestureActionData,
                           private val onHideAnimationComplete: () -> Unit) :
    AnimationManager.AnimationListener {

    // 'lazy' by default uses double-checked locking to ensure lambda only called once at a time, we know this already so don't need it
    private val drawManager: DrawManager by lazy(LazyThreadSafetyMode.NONE) { DrawManager(foregroundDrawable, gestureActionData, gestureIcons) }
    private val animationManager: AnimationManager by lazy(LazyThreadSafetyMode.NONE) { AnimationManager(gestureActionData,  this) }

    fun animateShow() {

        animationManager.animateShow()

    }

    fun animateHide() {

        animationManager.animateHide()

    }

    fun draw(canvas: Canvas) {

        drawManager.draw(canvas)

    }

    override fun onShowAnimationUpdated(valueAnimator: ValueAnimator) {

        drawManager.updateDrawValues(valueAnimator)

    }

    override fun onHideAnimationUpdated(valueAnimator: ValueAnimator) {

        drawManager.updateDrawValues(valueAnimator)

    }

    override fun onHideAnimationCompleted() {

        onHideAnimationComplete()

    }

}
