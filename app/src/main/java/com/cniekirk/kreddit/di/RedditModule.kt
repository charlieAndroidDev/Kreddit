package com.cniekirk.kreddit.di

import com.cniekirk.kreddit.data.reddit.jraw.JrawReddit
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module(includes = [RedditServiceModule::class, ViewModelModule::class])
class RedditModule {

    @Provides
    @Singleton
    fun provideReddit(jrawReddit: JrawReddit): JrawReddit {
        return jrawReddit
    }

}