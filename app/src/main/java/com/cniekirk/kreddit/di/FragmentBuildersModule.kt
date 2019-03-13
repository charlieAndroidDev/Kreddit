package com.cniekirk.kreddit.di

import com.cniekirk.kreddit.ui.submission.FragmentSubmission
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeFragmentSubmission(): FragmentSubmission

}