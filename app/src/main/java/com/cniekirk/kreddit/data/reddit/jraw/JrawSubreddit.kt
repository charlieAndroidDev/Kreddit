package com.cniekirk.kreddit.data.reddit.jraw

import com.cniekirk.kreddit.core.platform.NetworkHandler
import com.cniekirk.kreddit.data.SubmissionRequestOptions
import com.cniekirk.kreddit.ui.subreddit.uimodel.SubmissionUiModel
import com.cniekirk.kreddit.data.reddit.Reddit
import com.cniekirk.kreddit.utils.Either
import com.cniekirk.kreddit.utils.Failure
import com.cniekirk.kreddit.core.extensions.format
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
import java.util.concurrent.TimeUnit

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class JrawSubreddit constructor(
    private val clientBroadcastChannel: ConflatedBroadcastChannel<RedditClient>,
    private val networkHandler: NetworkHandler
): Reddit.Subreddit {

    override fun getSubmissions(submissionRequestOptions: SubmissionRequestOptions):
            Either<Failure, List<SubmissionUiModel>> {

        return when(networkHandler.isConnected) {
            true -> {
                val iterator = submissions(submissionRequestOptions.subredditName, submissionRequestOptions.sort,
                    submissionRequestOptions.timePeriod, submissionRequestOptions.isFrontPage, submissionRequestOptions.limit)
                val subList = iterator.next().toList()

                val uiSubmissions = subList.map { submission ->

                    val difference = System.currentTimeMillis() - submission.created.time
                    val days = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)
                    val hours = TimeUnit.HOURS.convert(difference, TimeUnit.MILLISECONDS)
                    val minutes = TimeUnit.MINUTES.convert(difference, TimeUnit.MILLISECONDS)

                    val timeString = when {
                        days < 1 && hours >= 1 -> "${hours}h"
                        hours < 1 -> "${minutes}m"
                        else -> "${days}d"
                    }

                    val scoreString = when {
                        submission.score > 1000 && (submission.score % 1000) < 100 -> "${submission.score / 1000}K"
                        submission.score > 1000 -> "%.1fK".format(submission.score / 1000f)
                        else -> submission.score.toString()
                    }

                    SubmissionUiModel(
                        submission.uniqueId, submission.title,
                        submission.selfText, submission.author, "R/${submission.subreddit}",
                        timeString, scoreString, submission.thumbnail, submission.url
                    )
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