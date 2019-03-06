package com.cniekirk.kreddit.data

import net.dean.jraw.models.SubredditSort
import net.dean.jraw.models.TimePeriod

data class SubmissionRequestOptions(
    val subredditName: String = "",
    val sort: SubredditSort,
    val timePeriod: TimePeriod,
    val isFrontPage: Boolean,
    val limit: Int = 50
)