package com.cniekirk.kreddit.widgets.gesture.draw

import android.content.Context
import android.graphics.Canvas
import com.cniekirk.kreddit.widgets.gesture.GestureActionLayout
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData

class DrawManager(context: Context,
                  foregroundDrawable: GestureActionLayout.ForegroundDrawable,
                  gestureActionData: GestureActionData) {

    private val drawController = DrawController(context, gestureActionData, foregroundDrawable)

    fun draw(canvas: Canvas) {

        drawController.draw(canvas)

    }

}