package com.cniekirk.kreddit.data.reddit

import com.cniekirk.kreddit.data.SubmissionRequestOptions
import com.cniekirk.kreddit.ui.subreddit.uimodel.SubmissionUiModel
import com.cniekirk.kreddit.utils.Either
import com.cniekirk.kreddit.utils.Failure
import net.dean.jraw.models.*

interface Reddit {

    companion object {
        const val CONTEXT_QUERY_PARAM = "context"
        const val COMMENT_DEFAULT_CONTEXT_COUNT = 3

        val DEFAULT_COMMENT_SORTING = CommentSort.CONFIDENCE
        val DEFAULT_SUBREDDIT_SORTING = SubredditSort.BEST
    }

    interface Subreddit {

        // Get subreddit info
        //fun find(subredditName: String)

        // Find subreddit submissions
        fun submissions(subredditName: String,
                        sort: SubredditSort,
                        timePeriod: TimePeriod,
                        frontPage: Boolean,
                        limit: Int)
        : Iterator<Listing<Submission>>

        fun getSubmissions(submissionRequestOptions: SubmissionRequestOptions)
        : Either<Failure, List<SubmissionUiModel>>

    }

    fun subreddit(): Reddit.Subreddit

}