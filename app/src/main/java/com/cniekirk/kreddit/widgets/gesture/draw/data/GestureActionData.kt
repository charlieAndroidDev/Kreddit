package com.cniekirk.kreddit.widgets.gesture.draw.data

/**
 * Value holder class for animation and drawing
 */
data class GestureActionData(
    val noAlpha: Int = 0,
    val opaqueAlpha: Int = 235,
    val fullAlpha: Int = 255,
    val initialMeasuredHeight: Int,
    val layoutWidth: Int,
    val layoutHeight: Int
)