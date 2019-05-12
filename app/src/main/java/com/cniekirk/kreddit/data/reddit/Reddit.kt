package com.cniekirk.kreddit.data.reddit

import com.cniekirk.kreddit.data.SubmissionRequestOptions
import com.cniekirk.kreddit.ui.subreddit.uimodel.SubmissionUiModel
import com.cniekirk.kreddit.utils.Either
import com.cniekirk.kreddit.utils.Failure
import net.dean.jraw.models.*
import net.dean.jraw.references.CommentsRequest
import net.dean.jraw.tree.RootCommentNode

/**
 * Interface representing required Reddit operations
 */
interface Reddit {

    companion object {
        const val CONTEXT_QUERY_PARAM = "context"
        const val COMMENT_DEFAULT_CONTEXT_COUNT = 3

        val DEFAULT_COMMENT_SORTING = CommentSort.CONFIDENCE
        val DEFAULT_SUBREDDIT_SORTING = SubredditSort.BEST
    }

    interface Subreddit {

        /**
         * @param subredditName: The subreddit name
         * @param sort: The sort method to sort the received data
         * @param frontPage: True when we want the Reddit front page
         * @param limit: The max number of results to be returned
         *
         * @return An [Iterator] of the list of [Submission]
         */
        fun submissions(subredditName: String,
                        sort: SubredditSort,
                        timePeriod: TimePeriod,
                        frontPage: Boolean,
                        limit: Int)
        : Iterator<Listing<net.dean.jraw.models.Submission>>

        /**
         * Wraps the above function to return an [Either] monad
         * @return An [Either] monad representing Failure or a [List] of [SubmissionUiModel]
         */
        fun getSubmissions(submissionRequestOptions: SubmissionRequestOptions)
        : Either<Failure, List<SubmissionUiModel>>

    }

    interface Submission {

        fun fetchComments(request: CommentsRequest): Either<Failure, RootCommentNode>

    }

    /**
     * @return The [Reddit.Subreddit] instance
     */
    fun subreddit(): Reddit.Subreddit

}