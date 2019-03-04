package com.cniekirk.kreddit.data.reddit.jraw

import com.cniekirk.kreddit.core.platform.NetworkHandler
import com.cniekirk.kreddit.data.SubmissionUiModel
import com.cniekirk.kreddit.data.reddit.Reddit
import com.cniekirk.kreddit.utils.Either
import com.cniekirk.kreddit.utils.Failure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.runBlocking
import net.dean.jraw.RedditClient
import net.dean.jraw.models.Listing
import net.dean.jraw.models.Submission
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.models.TimePeriod

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class JrawSubreddit constructor(
    private val clientBroadcastChannel: ConflatedBroadcastChannel<RedditClient>,
    private val networkHandler: NetworkHandler
): Reddit.Subreddit {

    override fun getSubmissions(
        subredditName: String,
        sort: SubredditSort,
        timePeriod: TimePeriod,
        frontPage: Boolean,
        limit: Int
    ): Either<Failure, List<SubmissionUiModel>> {

        return when(networkHandler.isConnected) {
            true -> {
                val iterator = submissions(subredditName, sort, timePeriod, frontPage, limit)
                val subList = iterator.next().toList()

                val uiSubmissions = subList.map { submission ->
                    SubmissionUiModel(submission.uniqueId, submission.title,
                        submission.author, "R/${submission.subreddit}",
                        submission.created, submission.score, submission.thumbnail)
                }.toList()

                Either.Right(uiSubmissions)
            }
            false, null -> Either.Left(Failure.NetworkConnection())
        }

    }

    override fun submissions(
        subredditName: String,
        sort: SubredditSort,
        timePeriod: TimePeriod,
        frontPage: Boolean,
        limit: Int
    ): Iterator<Listing<Submission>> {

        // Don't really want to block here but we need a client before we can perform actions
        return runBlocking {

            val client = clientBroadcastChannel.consume {

                this.receive()

            }

            val pagination = when {
                frontPage -> client.frontPage()
                else -> client.subreddit(subredditName).posts()
            }

            return@runBlocking pagination
                .timePeriod(timePeriod)
                .sorting(sort)
                .limit(limit)
                .build()
                .iterator()

        }

    }

}