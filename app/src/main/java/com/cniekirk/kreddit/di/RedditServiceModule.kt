package com.cniekirk.kreddit.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.cniekirk.kreddit.data.reddit.JrawTokenRefresher
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import net.dean.jraw.RedditClient
import net.dean.jraw.android.AndroidHelper
import net.dean.jraw.android.AppInfoProvider
import net.dean.jraw.android.ManifestAppInfoProvider
import net.dean.jraw.android.SharedPreferencesTokenStore
import net.dean.jraw.oauth.AccountHelper
import net.dean.jraw.oauth.TokenStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module(includes = [ViewModelModule::class])
class RedditServiceModule {

    @Provides
    @Singleton
    fun redditClientBroadcastChannel(): ConflatedBroadcastChannel<RedditClient> {
        return ConflatedBroadcastChannel()
    }

    @Provides
    fun appInfoProvider(appContext: Context): AppInfoProvider {

        return ManifestAppInfoProvider(appContext)

    }

    @Singleton
    @Provides
    fun sharedPreferencesTokenStore(appContext: Context): SharedPreferencesTokenStore {

        val store = SharedPreferencesTokenStore(appContext)
        store.load()
        store.autoPersist = true
        return store

    }

    @Provides
    fun tokenStore(sharedPreferencesTokenStore: SharedPreferencesTokenStore): TokenStore {

        return sharedPreferencesTokenStore

    }

    @Provides
    fun sharedPreferences(appContext: Context): SharedPreferences {

        return PreferenceManager.getDefaultSharedPreferences(appContext)

    }

    @Provides
    @Named("deviceUuid")
    fun deviceUuid(sharedPreferences: SharedPreferences): UUID {

        val key = "deviceUuid"
        if(!sharedPreferences.contains(key)) {
            sharedPreferences.edit()
                .putString(key, UUID.randomUUID().toString())
                .apply()
        }

        return UUID.fromString(sharedPreferences.getString(key, null))

    }

    @Provides
    @Singleton
    fun accountHelper(
        appInfoProvider: AppInfoProvider,
        tokenStore: SharedPreferencesTokenStore,
        tokenRefresher: JrawTokenRefresher,
        @Named("deviceUuid") deviceUUID: UUID
    ): AccountHelper {

        val httpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(tokenRefresher)
            .apply {

                addInterceptor(
                    HttpLoggingInterceptor { message -> Log.d("JRAW_HTTP", message) }.apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                )

            }.build()

        return AndroidHelper.accountHelper(appInfoProvider, deviceUUID, tokenStore, httpClient)
    }

}