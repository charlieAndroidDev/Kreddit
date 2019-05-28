package com.cniekirk.kreddit.core.extensions

import android.content.res.ColorStateList
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.REVERSE
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

fun ImageView.switchImageAnim(imgRes: Int, colorInt: Int) {

    with(AlphaAnimation(1.0f, 0.0f)) {
        duration = 100
        interpolator = FastOutSlowInInterpolator()
        repeatCount = 1
        repeatMode = REVERSE
        setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                setImageResource(imgRes)
                ImageViewCompat.setImageTintList(this@switchImageAnim, ColorStateList.valueOf(colorInt))
            }
            override fun onAnimationEnd(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {}
        })
        start()
    }


}