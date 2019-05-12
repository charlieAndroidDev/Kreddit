package com.cniekirk.kreddit.data.reddit.jraw

import com.cniekirk.kreddit.core.platform.NetworkHandler
import com.cniekirk.kreddit.data.reddit.Reddit
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import net.dean.jraw.RedditClient
import net.dean.jraw.android.SharedPreferencesTokenStore
import net.dean.jraw.oauth.AccountHelper
import javax.inject.Inject

/**
 * Extensible Reddit operations handling class.
 *
 * Initialises a new Reddit Client and offers it the the [ConflatedBroadcastChannel]
 */
@ExperimentalCoroutinesApi
class JrawReddit @Inject constructor(
    private val accountHelper: AccountHelper,
    private val clientBroadcastChannel: ConflatedBroadcastChannel<RedditClient>,
    tokenStore: SharedPreferencesTokenStore,
    private val networkHandler: NetworkHandler
): Reddit {

    init {

        // Will need a new client going from userless to logged in
        accountHelper.onSwitch { newClient -> clientBroadcastChannel.offer(newClient) }

        GlobalScope.launch(IO, CoroutineStart.DEFAULT) {

            // Will need a user session tracker, for now stay userless
            accountHelper.switchToUserless()

        }

    }

    /**
     * @return A [JrawSubreddit] instance
     */
    override fun subreddit(): Reddit.Subreddit {
        return JrawSubreddit(clientBroadcastChannel, networkHandler)
    }

}