package com.cniekirk.kreddit.data

import java.util.Date

data class SubmissionUiModel(
    val id: String,
    val title: String,
    val author: String,
    val subReddit: String,
    val date: Date,
    val votes: Int,
    val thumbnailUrl: String?
)