package com.cniekirk.kreddit.widgets.gesture.draw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import com.cniekirk.kreddit.widgets.gesture.GestureActionLayout
import com.cniekirk.kreddit.widgets.gesture.draw.data.GestureActionData

class DrawController(private val context: Context,
                     private val gestureActionData: GestureActionData,
                     private val foregroundDrawable: GestureActionLayout.ForegroundDrawable) {

    private lateinit var textPaint: Paint
    private lateinit var backgroundRectPaint: Paint

    init {

        textPaint.color = Color.BLACK
        textPaint.alpha = gestureActionData.variableTextAlpha
        textPaint.textSize = 55f
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.typeface = Typeface.DEFAULT_BOLD

        backgroundRectPaint.color = Color.WHITE
        backgroundRectPaint.alpha = gestureActionData.variableBackgroundAlpha
        backgroundRectPaint.flags = Paint.ANTI_ALIAS_FLAG

    }

    /**
     * Only called when foregroundDrawable should be drawn
     * @param canvas: The canvas to draw to passed in from the layout itself
     */
    fun draw(canvas: Canvas) {

        foregroundDrawable.draw(canvas)

        val clipBounds = canvas.clipBounds
        clipBounds.inset(0, -gestureActionData.initialMeasuredHeight)
        canvas.clipRect(clipBounds)

        canvas.drawRect(0f, -gestureActionData.initialMeasuredHeight.toFloat(),
            gestureActionData.layoutWidth.toFloat(), 0f, backgroundRectPaint)

        val textY = -gestureActionData.variableTextRevealHeight.toFloat() - ((textPaint.ascent() - textPaint.descent()) / 2)

        canvas.drawText("\"${gestureActionData.gestureSubmissionData?.submissionText}\"",
            canvas.width / 2f, textY, textPaint)

    }



}
