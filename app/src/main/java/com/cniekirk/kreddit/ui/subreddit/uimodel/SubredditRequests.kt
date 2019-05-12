package com.cniekirk.kreddit.ui.subreddit.uimodel

import com.cniekirk.kreddit.data.SubmissionRequestOptions
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.models.TimePeriod

/**
 * Object to statically build a front page Subreddit request
 */
object SubredditRequests {

    fun frontPage(sort: SubredditSort = SubredditSort.BEST, timePeriod: TimePeriod = TimePeriod.DAY): SubmissionRequestOptions {

        return SubmissionRequestOptions(sort = sort, timePeriod = timePeriod, isFrontPage = true)

    }

}