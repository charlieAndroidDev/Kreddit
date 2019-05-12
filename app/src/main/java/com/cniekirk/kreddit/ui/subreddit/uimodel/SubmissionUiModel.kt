package com.cniekirk.kreddit.ui.subreddit.uimodel

/**
 * UI Model class representing a Submission
 */
data class SubmissionUiModel(
    val id: String,
    val title: String,
    val content: String?,
    val author: String,
    val subReddit: String,
    val date: String,
    val voteString: String,
    val thumbnailUrl: String?,
    val url: String?
)