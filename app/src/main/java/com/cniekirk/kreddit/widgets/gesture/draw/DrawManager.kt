package com.cniekirk.kreddit.widgets.gesture.draw

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import com.cniekirk.kreddit.widgets.gesture.GestureAction
import com.cniekirk.kreddit.widgets.gesture.GestureActionLayout
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData

/**
 * Class to manage the drawing of the control
 */
class DrawManager(foregroundDrawable: GestureActionLayout.ForegroundDrawable,
                  gestureActionData: GestureActionData,
                  gestureActionIcons: List<GestureAction>) {

    private val drawController = DrawController(gestureActionData, foregroundDrawable, gestureActionIcons)

    /**
     * Draw the control
     * @param canvas: The [Canvas] to draw the control to
     */
    fun draw(canvas: Canvas) {

        drawController.draw(canvas)

    }

    /**
     * Used for animating reveal
     */
    fun updateDrawValues(valueAnimator: ValueAnimator) {

        drawController.updateDrawValues(valueAnimator)

    }

}