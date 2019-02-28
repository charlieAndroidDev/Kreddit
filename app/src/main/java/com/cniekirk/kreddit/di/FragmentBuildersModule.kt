package com.cniekirk.kreddit.di

import com.cniekirk.kreddit.ui.subreddit.SubredditFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeSubredditFragment(): SubredditFragment

}