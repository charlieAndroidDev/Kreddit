package com.cniekirk.kreddit.di

import android.content.Context
import com.cniekirk.kreddit.FlexApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

/**
 * Dagger Component class
 */
@ExperimentalCoroutinesApi
@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    RedditServiceModule::class,
    ImgurServiceModule::class,
    MainActivityModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Context): Builder

        fun build(): AppComponent

    }

    fun inject(flexApp: FlexApp)

}