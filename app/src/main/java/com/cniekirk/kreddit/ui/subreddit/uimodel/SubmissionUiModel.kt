package com.cniekirk.kreddit.ui.subreddit.uimodel

import java.util.Date

data class SubmissionUiModel(
    val id: String,
    val title: String,
    val content: String?,
    val author: String,
    val subReddit: String,
    val date: Date,
    val votes: Int,
    val thumbnailUrl: String?,
    val url: String?
)