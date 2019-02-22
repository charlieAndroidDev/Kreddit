package com.cniekirk.kreddit.widgets.gesture.draw

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import com.cniekirk.kreddit.widgets.gesture.GestureAction
import com.cniekirk.kreddit.widgets.gesture.GestureActionLayout
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData

class DrawManager(foregroundDrawable: GestureActionLayout.ForegroundDrawable,
                  gestureActionData: GestureActionData,
                  gestureActionIcons: List<GestureAction>) {

    private val drawController = DrawController(gestureActionData, foregroundDrawable, gestureActionIcons)

    fun draw(canvas: Canvas) {

        drawController.draw(canvas)

    }

    fun updateDrawValues(valueAnimator: ValueAnimator) {

        drawController.updateDrawValues(valueAnimator)

    }

}