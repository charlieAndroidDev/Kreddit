package com.cniekirk.kreddit

import android.app.Application
import android.content.Context
import com.cniekirk.kreddit.ui.subreddit.SubredditActivity
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.RuntimeEnvironment

//@ExperimentalCoroutinesApi
//@RunWith(RobolectricTestRunner::class)
//@Config(constants = BuildConfig::class,
//    application = AndroidTest.ApplicationStub::class,
//    sdk = [23])
//abstract class AndroidTest {
//
//    @Rule
//    @JvmField val injectMocks = InjectMocksRule.create(this@AndroidTest)
//
//    fun context(): Context = RuntimeEnvironment.application
//
//    fun activityContext(): Context = mockk<SubredditActivity>()
//
//    internal class ApplicationStub : Application()
//
//}
