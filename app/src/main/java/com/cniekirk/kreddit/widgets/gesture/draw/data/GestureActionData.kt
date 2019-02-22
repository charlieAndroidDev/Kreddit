package com.cniekirk.kreddit.widgets.gesture.draw.data

import com.cniekirk.kreddit.ui.subreddit.uimodel.GestureSubmissionData

data class GestureActionData(
    val noAlpha: Int = 0,
    val opaqueAlpha: Int = 235,
    val fullAlpha: Int = 255,
    var variableTextAlpha: Int,
    var variableBackgroundAlpha: Int,
    var variableTextRevealHeight: Int,
    val initialMeasuredHeight: Int,
    val foregroundDrawableHeightRatio: Float,
    val layoutWidth: Int,
    val layoutHeight: Int,
    var gestureSubmissionData: GestureSubmissionData?
)