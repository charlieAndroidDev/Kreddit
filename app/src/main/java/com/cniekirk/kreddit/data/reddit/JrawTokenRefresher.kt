package com.cniekirk.kreddit.data.reddit

import android.text.format.DateUtils
import android.util.Log
import com.cniekirk.kreddit.BuildConfig
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import net.dean.jraw.RedditClient
import net.dean.jraw.android.SharedPreferencesTokenStore
import net.dean.jraw.models.OAuthData
import net.dean.jraw.oauth.AuthMethod
import okhttp3.Interceptor
import okhttp3.Response
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset.UTC
import javax.inject.Inject

/**
 * Taken from https://github.com/saket/dank
 *
 * JRAW is capable of refreshing tokens automatically, but it doesn't seem to work.
 * This class does that + also refreshes tokens ahead of time (asynchronously) to
 * avoid making the user wait longer.
 */
class JrawTokenRefresher @ExperimentalCoroutinesApi
@Inject constructor(private val clients: ConflatedBroadcastChannel<RedditClient>) : Interceptor {

    private var isAheadOfTimeRefreshInFlight: Boolean = false

    fun log(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d("TOKEN_REFRESHER", msg)
        }
    }

    @ExperimentalCoroutinesApi
    override fun intercept(chain: Interceptor.Chain): Response {
        if (chain.request().url().toString().startsWith("https://www.reddit.com/api/v1/access_token")) {
            return chain.proceed(chain.request())
        }

        GlobalScope.launch(Main) {

            GlobalScope.launch(IO) { fixRefreshToken(clients.value) }.join()
            // Don't care when this completes but need it to happen after fix
            GlobalScope.launch(IO) { renewIfNeeded(clients.value) }

        }

        return chain.proceed(chain.request())
    }

    /**
     * https://github.com/mattbdean/JRAW/issues/264
     */
    private fun fixRefreshToken(client: RedditClient) {
        if (client.authMethod != AuthMethod.APP) {
            return
        }

        val authManager = client.authManager
        val tokenStore = authManager.tokenStore
        val currentUsername = client.authManager.currentUsername()

        val storedAuthData: OAuthData? = tokenStore.fetchLatest(currentUsername)
        val storedRefreshToken = tokenStore.fetchRefreshToken(currentUsername)

        val isJrawPotentiallyFuckingUp = storedAuthData != null
                && storedAuthData.refreshToken == null
                && storedRefreshToken != null

        if (isJrawPotentiallyFuckingUp) {
            Log.d("TOKEN_REFRESHER", "JRAW is fucking up. Fixing refresh token.")

            tokenStore.storeLatest(
                currentUsername,
                OAuthData.create(
                    storedAuthData!!.accessToken,
                    storedAuthData.scopes,
                    storedRefreshToken,
                    storedAuthData.expiration
                )
            )
        }
    }

    private fun renewIfNeeded(client: RedditClient) {
        if (client.authMethod != AuthMethod.APP || !client.authManager.canRenew()) {
            val usernames = (client.authManager.tokenStore as SharedPreferencesTokenStore).usernames
            log("Cannot renew. usernames: $usernames")
            return
        }

        val expirationDate = tokenExpirationDate(client)
        val recommendedRefreshDate = expirationDate.minusMinutes(10)

        log("Expiration date: $expirationDate")
        log("Time till token expiration: ${formatTime(expirationDate)}")
        log("Username: ${client.authManager.currentUsername()}")
        log("Time till pro-active token expiration: ${formatTime(recommendedRefreshDate)}")

        return when (computeTokenStatus(client, expirationDate, recommendedRefreshDate)) {
            TokenStatus.REFRESH_AHEAD_OF_TIME -> {
                refreshAheadOfTime(client, recommendedRefreshDate)
            }
            TokenStatus.FRESH -> return
        }
    }

    private fun refreshAheadOfTime(client: RedditClient, recommendedRefreshDate: LocalDateTime) {
        log("Time to refresh token")
        log("Recommended refresh date: $recommendedRefreshDate")
        log("Now time: ${LocalDateTime.now(UTC)}")

        if (isAheadOfTimeRefreshInFlight) {
            return
        }

        isAheadOfTimeRefreshInFlight = true
        client.authManager.renew()
        isAheadOfTimeRefreshInFlight = false
        log("Token refreshed")

//        return Completable.fromAction { client.authManager.renew() }
//            .subscribeOn(io())
//            .doOnSubscribe { isAheadOfTimeRefreshInFlight = true }
//            .doOnComplete {
//                isAheadOfTimeRefreshInFlight = false
//                log("Token refreshed")
//            }
    }

    enum class TokenStatus {
        REFRESH_AHEAD_OF_TIME,
        FRESH
    }

    private fun computeTokenStatus(client: RedditClient, expirationDate: LocalDateTime, recommendedRefreshDate: LocalDateTime): TokenStatus {
        val now = LocalDateTime.now(UTC)

        if (now > recommendedRefreshDate && now < expirationDate) {
            return TokenStatus.REFRESH_AHEAD_OF_TIME
        }

        if (now >= expirationDate) {
            throw AssertionError("Jraw did not refresh token automatically")
        }

        val needsRefresh = client.authManager.needsRenewing()
        if (needsRefresh) {
            throw AssertionError("JRAW says token needs refreshing")
        }

        return TokenStatus.FRESH
    }

    private fun tokenExpirationDate(client: RedditClient): LocalDateTime {
        val latestOAuthData: OAuthData = client.authManager.current!!
        if (latestOAuthData.expiration.time == 0L) {
            throw AssertionError("Expiration time is empty")
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(latestOAuthData.expiration.time), UTC)
    }

    private fun formatTime(expirationDateTime: LocalDateTime): CharSequence? {
        return DateUtils.getRelativeTimeSpanString(
            expirationDateTime.toInstant(UTC).toEpochMilli(),
            System.currentTimeMillis(),
            0,
            DateUtils.FORMAT_ABBREV_RELATIVE or DateUtils.FORMAT_ABBREV_MONTH)
    }
}