package com.cniekirk.kreddit.widgets.gesture.draw

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import com.cniekirk.kreddit.widgets.gesture.GestureAction
import com.cniekirk.kreddit.widgets.gesture.GestureActionLayout
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData

class DrawController(private val gestureActionData: GestureActionData,
                     private val foregroundDrawable: GestureActionLayout.ForegroundDrawable,
                     private val gestureActionIcons: List<GestureAction>) {

    // The paints used by the draw() function
    private val textPaint = Paint()
    private val backgroundRectPaint = Paint()

    // Variable animated properties
    var textRevealAlpha = 0
    var textBackgroundAlpha = 0
    var textRevealHeight = 0f
    var actionIconHeight = gestureActionData.layoutHeight

    /**
     * Only called when foregroundDrawable should be drawn
     * @param canvas: The canvas to draw to passed in from the layout itself
     */
    fun draw(canvas: Canvas) {

        // Draw the background colour of the solid overlay the provides actions
        foregroundDrawable.draw(canvas)

        // Clip the bounds of our layout to double the height
        val clipBounds = canvas.clipBounds
        clipBounds.inset(0, -gestureActionData.initialMeasuredHeight)
        canvas.clipRect(clipBounds)

        // Setup the paint for drawing the opaque white overlay
        backgroundRectPaint.color = Color.WHITE
        backgroundRectPaint.flags = Paint.ANTI_ALIAS_FLAG
        backgroundRectPaint.alpha = textBackgroundAlpha

        // Draw the opaque white overlay
        canvas.drawRect(0f, -gestureActionData.initialMeasuredHeight.toFloat(),
            gestureActionData.layoutWidth.toFloat(), 0f, backgroundRectPaint)

        // Setup the paint for drawing the upwards projected text on the opaque overlay
        textPaint.color = Color.BLACK
        textPaint.textSize = 55f
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.typeface = Typeface.DEFAULT_BOLD
        textPaint.alpha = textRevealAlpha

        // ((textPaint.ascent() - textPaint.descent()) / 2) is the distance from the centre to the baseline of drawn text
        val textY = -textRevealHeight - ((textPaint.ascent() - textPaint.descent()) / 2)

        // Draw the upwards projected text
        canvas.drawText("\"${gestureActionData.gestureSubmissionData?.submissionText}\"",
            canvas.width / 2f, textY, textPaint)

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
        textBackgroundAlpha = valueAnimator.getAnimatedValue("TEXT_BACKGROUND_ALPHA") as Int
        textRevealHeight = (valueAnimator.getAnimatedValue("TEXT_HEIGHT") as Int).toFloat()
        foregroundDrawable.alpha = valueAnimator.getAnimatedValue("FOREGROUND_DRAWABLE_ALPHA") as Int
        actionIconHeight = (valueAnimator.getAnimatedValue("VIEW_HEIGHT") as Float).toInt()

    }


}
