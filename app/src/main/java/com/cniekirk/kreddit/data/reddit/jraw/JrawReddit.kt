package com.cniekirk.kreddit.data.reddit.jraw

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import net.dean.jraw.RedditClient
import net.dean.jraw.android.SharedPreferencesTokenStore
import net.dean.jraw.oauth.AccountHelper
import javax.inject.Inject

@ExperimentalCoroutinesApi
class JrawReddit @Inject constructor(
    private val accountHelper: AccountHelper,
    private val clientBroadcastChannel: ConflatedBroadcastChannel<RedditClient>,
    tokentStore: SharedPreferencesTokenStore
) {

    init {

        // Will need a new client going from userless to logged in
        accountHelper.onSwitch { newClient -> clientBroadcastChannel.offer(newClient) }

        

    }

}