package com.cniekirk.kreddit.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cniekirk.kreddit.ui.submission.SubmissionViewModel
import com.cniekirk.kreddit.ui.subreddit.SubredditViewModel
import com.cniekirk.kreddit.utils.AppViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SubredditViewModel::class)
    abstract fun bindSubredditViewModel(subredditViewModel: SubredditViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SubmissionViewModel::class)
    abstract fun bindSubmissionViewModel(submissionViewModel: SubmissionViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(appViewModelFactory: AppViewModelFactory): ViewModelProvider.Factory

}