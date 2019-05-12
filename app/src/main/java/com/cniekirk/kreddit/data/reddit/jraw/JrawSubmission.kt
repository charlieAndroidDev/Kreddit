package com.cniekirk.kreddit.data.reddit.jraw

import com.cniekirk.kreddit.core.platform.NetworkHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import net.dean.jraw.RedditClient
import javax.inject.Inject

@ExperimentalCoroutinesApi
class JrawSubmission @Inject constructor(
    private val clientBroadcastChannel: ConflatedBroadcastChannel<RedditClient>,
    private val networkHandler: NetworkHandler
)