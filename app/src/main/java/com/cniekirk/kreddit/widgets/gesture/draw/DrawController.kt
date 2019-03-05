package com.cniekirk.kreddit.widgets.gesture.draw

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import com.cniekirk.kreddit.widgets.gesture.GestureAction
import com.cniekirk.kreddit.widgets.gesture.GestureActionLayout
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData

class DrawController(private val gestureActionData: GestureActionData,
                     private val foregroundDrawable: GestureActionLayout.ForegroundDrawable,
                     private val gestureActionIcons: List<GestureAction>) {

    // Variable animated properties
    var textRevealAlpha = 0
    var actionIconHeight = gestureActionData.layoutHeight

    /**
     * Only called when foregroundDrawable should be drawn
     * @param canvas: The canvas to draw to passed in from the layout itself
     */
    fun draw(canvas: Canvas) {

        // Draw the background colour of the solid overlay the provides actions
        Log.d("FD", "Alpha = ${foregroundDrawable.alpha}")
        foregroundDrawable.draw(canvas)

        // Draw each gesture action icon
        gestureActionIcons.forEachIndexed { index, gestureAction ->

            // Calculate the appropriate spacing for the gesture action icon
            val offset = index * (gestureActionData.layoutWidth / gestureActionIcons.size)
            val eigth = (gestureActionData.layoutWidth / (gestureActionIcons.size * 2))

            // Saves assigning another variable but isn't particularly readable
            gestureAction.drawable.alpha = textRevealAlpha

            // Set the gesture action icon bounds
            gestureAction.drawable.setBounds((offset + eigth) - (gestureAction.drawable.intrinsicWidth / 2), (actionIconHeight / 2) - (gestureAction.drawable.intrinsicHeight / 2),
                ((offset + eigth) - (gestureAction.drawable.intrinsicWidth / 2) + gestureAction.drawable.intrinsicWidth),
                (actionIconHeight / 2) + (gestureAction.drawable.intrinsicHeight / 2))

            // Finally draw the gesture action icon
            gestureAction.drawable.draw(canvas)

        }

    }

    // Called from the animator
    fun updateDrawValues(valueAnimator: ValueAnimator) {

        textRevealAlpha = valueAnimator.getAnimatedValue("TEXT_REVEAL_ALPHA") as Int
        foregroundDrawable.alpha = valueAnimator.getAnimatedValue("FOREGROUND_DRAWABLE_ALPHA") as Int
        //actionIconHeight = (valueAnimator.getAnimatedValue("VIEW_HEIGHT") as Float).toInt()

    }


}
