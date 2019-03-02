package com.cniekirk.kreddit.di

import com.cniekirk.kreddit.ui.subreddit.SubredditActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
abstract class MainActivityModule {

    @Singleton
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class, RedditServiceModule::class])
    abstract fun contributeMainActivity(): SubredditActivity

}