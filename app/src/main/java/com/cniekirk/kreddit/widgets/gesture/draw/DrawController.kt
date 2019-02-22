package com.cniekirk.kreddit.widgets.gesture.draw

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import com.cniekirk.kreddit.widgets.gesture.GestureActionLayout
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData

class DrawController(private val gestureActionData: GestureActionData,
                     private val foregroundDrawable: GestureActionLayout.ForegroundDrawable) {

    private val textPaint = Paint()
    private val backgroundRectPaint = Paint()

    var textRevealAlpha = 0
    var textBackgroundAlpha = 0
    var textRevealHeight = 0f

    /**
     * Only called when foregroundDrawable should be drawn
     * @param canvas: The canvas to draw to passed in from the layout itself
     */
    fun draw(canvas: Canvas) {

        Log.d("THIS", "This: $this")

        foregroundDrawable.draw(canvas)

        val clipBounds = canvas.clipBounds
        clipBounds.inset(0, -gestureActionData.initialMeasuredHeight)
        canvas.clipRect(clipBounds)

        backgroundRectPaint.color = Color.WHITE
        backgroundRectPaint.flags = Paint.ANTI_ALIAS_FLAG
        backgroundRectPaint.alpha = textBackgroundAlpha

        canvas.drawRect(0f, -gestureActionData.initialMeasuredHeight.toFloat(),
            gestureActionData.layoutWidth.toFloat(), 0f, backgroundRectPaint)

        textPaint.color = Color.BLACK
        textPaint.textSize = 55f
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.typeface = Typeface.DEFAULT_BOLD
        textPaint.alpha = textRevealAlpha

        Log.d("TEXT H", "TEXT H = $textRevealHeight")

        // ((textPaint.ascent() - textPaint.descent()) / 2) is the distance from the centre to the baseline of drawn text
        val textY = -textRevealHeight - ((textPaint.ascent() - textPaint.descent()) / 2)

        canvas.drawText("\"${gestureActionData.gestureSubmissionData?.submissionText}\"",
            canvas.width / 2f, textY, textPaint)

    }

    fun updateDrawValues(valueAnimator: ValueAnimator) {

        Log.d("THIS_2", "This: $this")

        textRevealAlpha = valueAnimator.getAnimatedValue("TEXT_REVEAL_ALPHA") as Int
        textBackgroundAlpha = valueAnimator.getAnimatedValue("TEXT_BACKGROUND_ALPHA") as Int
        textRevealHeight = (valueAnimator.getAnimatedValue("TEXT_HEIGHT") as Int).toFloat()
        foregroundDrawable.alpha = valueAnimator.getAnimatedValue("FOREGROUND_DRAWABLE_ALPHA") as Int

    }


}
