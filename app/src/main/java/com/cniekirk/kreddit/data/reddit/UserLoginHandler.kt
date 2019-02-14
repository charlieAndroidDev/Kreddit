package com.cniekirk.kreddit.data.reddit

import net.dean.jraw.RedditClient
import net.dean.jraw.oauth.StatefulAuthHelper

class UserLoginHandler(private val helper: StatefulAuthHelper) {

    fun authUrl(): String {

        val scopes = arrayOf(
            "account",
            "mysubreddits",
            "privatemessages",
            "read",
            "report",
            "submit",
            "subscribe",
            "vote",
            "save",
            "edit",
            "history",
            "identity",
            "wikiread"
        )

        return helper.getAuthorizationUrl(true, useMobileSite = true, scopes = *scopes)

    }

    suspend fun processSuccessUrl(successUrl: String): RedditClient {

        return helper.onUserChallenge(successUrl)

    }

}