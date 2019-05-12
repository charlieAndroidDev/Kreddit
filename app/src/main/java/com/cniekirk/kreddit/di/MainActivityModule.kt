package com.cniekirk.kreddit.di

import com.cniekirk.kreddit.ui.subreddit.SubredditActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Allows [SubredditActivity] and its children to be injected
 */
@ExperimentalCoroutinesApi
@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): SubredditActivity

}