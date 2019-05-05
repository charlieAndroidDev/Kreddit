package com.cniekirk.kreddit.di

import com.cniekirk.kreddit.BuildConfig
import com.cniekirk.kreddit.data.api.ImgurApi
import com.cniekirk.kreddit.data.api.repo.ImgurRepository
import com.cniekirk.kreddit.data.api.repo.ImgurRepositoryImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module(includes = [ViewModelModule::class])
class ImgurServiceModule {

    @Provides
    @Singleton
    fun provideImgurApi(retrofit: Retrofit): ImgurApi = retrofit.create(ImgurApi::class.java)

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {

        return when {
            BuildConfig.DEBUG -> OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)).build()
            else -> OkHttpClient.Builder().build()
        }

    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://imgur-apiv3.p.rapidapi.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideImgurApiRepository(imgurRepository: ImgurRepositoryImpl): ImgurRepository = imgurRepository

}