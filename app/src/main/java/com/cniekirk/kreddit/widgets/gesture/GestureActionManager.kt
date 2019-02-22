package com.cniekirk.kreddit.widgets.gesture

import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import com.cniekirk.kreddit.widgets.gesture.animation.AnimationManager
import com.cniekirk.kreddit.widgets.gesture.draw.DrawManager
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData

class GestureActionManager(private val foregroundDrawable: GestureActionLayout.ForegroundDrawable,
                           private val gestureIcons: List<GestureAction>,
                           private val gestureActionData: GestureActionData,
                           private val onShowAnimationUpdate: (ValueAnimator) -> Unit,
                           private val onHideAnimationUpdate: (ValueAnimator) -> Unit,
                           private val onHideAnimationComplete: () -> Unit) :
    AnimationManager.AnimationListener {

    val drawManager: DrawManager by lazy { DrawManager(foregroundDrawable, gestureActionData, gestureIcons) }
    private val animationManager: AnimationManager by lazy { AnimationManager(gestureActionData,  this) }

    fun animateShow() {

        animationManager.animateShow()

    }

    fun animateHide() {

        animationManager.animateHide()

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
