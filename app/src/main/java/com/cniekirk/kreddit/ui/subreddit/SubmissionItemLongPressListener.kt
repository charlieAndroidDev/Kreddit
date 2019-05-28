package com.cniekirk.kreddit.ui.subreddit

interface SubmissionItemLongPressListener {

    fun onLongPress(position: Int)

    fun onLongPressRelease(position: Int)

    fun onLongPressDrag(touchX: Int)

}