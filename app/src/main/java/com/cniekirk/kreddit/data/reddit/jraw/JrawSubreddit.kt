package com.cniekirk.kreddit.data.reddit.jraw

import com.cniekirk.kreddit.data.reddit.Reddit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import net.dean.jraw.RedditClient
import net.dean.jraw.models.Listing
import net.dean.jraw.models.Submission
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.models.TimePeriod

@ExperimentalCoroutinesApi
class JrawSubreddit constructor(
    private val clientBroadcastChannel: ConflatedBroadcastChannel<RedditClient>
): Reddit.Subreddit {

    override fun submissions(
        subredditName: String,
        sort: SubredditSort,
        timePeriod: TimePeriod,
        frontPage: Boolean,
        limit: Int
    ): Iterator<Listing<Submission>> {

        val client = clientBroadcastChannel.value

        val pagination = when {
            frontPage -> client.frontPage()
            else -> client.subreddit(subredditName).posts()
        }

        return pagination
            .timePeriod(timePeriod)
            .sorting(sort)
            .limit(limit)
            .build()
            .iterator()

    }

}