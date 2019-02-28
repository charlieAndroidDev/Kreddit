package com.cniekirk.kreddit.di

import com.cniekirk.kreddit.ui.subreddit.SubredditActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): SubredditActivity

}