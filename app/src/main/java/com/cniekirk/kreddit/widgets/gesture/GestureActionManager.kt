package com.cniekirk.kreddit.widgets.gesture

import android.content.Context
import com.cniekirk.kreddit.widgets.gesture.draw.DrawManager
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData

class GestureActionManager(private val context: Context,
                           private val animationListener: AnimationListener,
                           private val foregroundDrawable: GestureActionLayout.ForegroundDrawable,
                           private val gestureActionData: GestureActionData) {

    val drawManager: DrawManager by lazy { DrawManager(context, foregroundDrawable, gestureActionData) }

    interface AnimationListener {

        fun onAnimationUpdated()

    }



}
