package com.cniekirk.kreddit.widgets.gesture.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Rect
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

class AnimationManager(val onAnimationUpdated: () -> Unit) {


    interface AnimationListener {

        fun onAnimationUpdated()

    }


}