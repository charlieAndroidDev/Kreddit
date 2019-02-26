package com.cniekirk.kreddit.widgets.gesture

import android.animation.ValueAnimator
import android.graphics.Canvas
import com.cniekirk.kreddit.widgets.gesture.animation.AnimationManager
import com.cniekirk.kreddit.widgets.gesture.draw.DrawManager
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData
import kotlin.LazyThreadSafetyMode.NONE

class GestureActionManager(private val foregroundDrawable: GestureActionLayout.ForegroundDrawable,
                           private val gestureIcons: List<GestureAction>,
                           private val gestureActionData: GestureActionData,
                           private val onShowAnimationUpdate: (ValueAnimator) -> Unit,
                           private val onHideAnimationUpdate: (ValueAnimator) -> Unit,
                           private val onHideAnimationComplete: () -> Unit) :
    AnimationManager.AnimationListener {

    // 'lazy' by default uses double-checked locking to ensure lambda only called once, we know this already so don't need it
    private val drawManager: DrawManager by lazy(NONE) { DrawManager(foregroundDrawable, gestureActionData, gestureIcons) }
    private val animationManager: AnimationManager by lazy(NONE) { AnimationManager(gestureActionData,  this) }

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
        onShowAnimationUpdate(valueAnimator)

    }

    override fun onHideAnimationUpdated(valueAnimator: ValueAnimator) {

        drawManager.updateDrawValues(valueAnimator)
        onHideAnimationUpdate(valueAnimator)

    }

    override fun onHideAnimationCompleted() {

        onHideAnimationComplete()

    }

}
