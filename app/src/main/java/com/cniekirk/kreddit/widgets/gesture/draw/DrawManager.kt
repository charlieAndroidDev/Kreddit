package com.cniekirk.kreddit.widgets.gesture.draw

import android.animation.ValueAnimator
import android.graphics.Canvas
import com.cniekirk.kreddit.widgets.gesture.GestureActionLayout
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData

class DrawManager(foregroundDrawable: GestureActionLayout.ForegroundDrawable,
                  gestureActionData: GestureActionData) {

    private val drawController = DrawController(gestureActionData, foregroundDrawable)

    fun draw(canvas: Canvas) {

        drawController.draw(canvas)

    }

    fun updateDrawValues(valueAnimator: ValueAnimator) {

        drawController.updateDrawValues(valueAnimator)

    }

}