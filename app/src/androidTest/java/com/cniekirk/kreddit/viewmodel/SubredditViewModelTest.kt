package com.cniekirk.kreddit.viewmodel

import com.cniekirk.kreddit.domain.RetrieveFrontPageSubmissionsUseCase
import com.cniekirk.kreddit.ui.subreddit.SubredditViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SubredditViewModelTest {

    private lateinit var subredditViewModel: SubredditViewModel

    @Before
    fun setup() {

        val useCase = mockk<RetrieveFrontPageSubmissionsUseCase>()
        subredditViewModel = SubredditViewModel(useCase)

    }

    @Test
    fun `loading subreddit submissions should change livedata value`() {



    }

}