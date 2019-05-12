package com.cniekirk.kreddit.usecase

import com.cniekirk.kreddit.data.SubmissionRequestOptions
import com.cniekirk.kreddit.data.reddit.Reddit
import com.cniekirk.kreddit.domain.RetrieveFrontPageSubmissionsUseCase
import com.cniekirk.kreddit.utils.Either
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.models.TimePeriod
import org.junit.Before
import org.junit.Test

/**
 * Test to make sure UseCase invocation calls appropriate repository function
 */
@ExperimentalCoroutinesApi
class GetSubmissionsTest {

    // Mock the required Class
    private val repo = mockk<Reddit>()

    private lateinit var retrieveFrontPageSubmissionsUseCase: RetrieveFrontPageSubmissionsUseCase
    private val requestOptions = SubmissionRequestOptions(sort = SubredditSort.BEST,
        timePeriod = TimePeriod.DAY, isFrontPage = true)

    /**
     * Initialise the SUT (System Under Test)
     */
    @Before
    fun setup() {
        retrieveFrontPageSubmissionsUseCase = RetrieveFrontPageSubmissionsUseCase(repo)
        every { repo.subreddit().getSubmissions(requestOptions) } returns Either.Right(emptyList())
    }

    /**
     * The actual test.  [verifyAll] checks to make sure behaviour captured
     * in the lambda is executed and nothing else
     *
     * We can use [runBlocking] as the UseCase's [run] is a suspending function
     * This makes it very easy to test and implement for real
     */
    @Test
    fun `get subreddit submissions queries repository`() {
        runBlocking { retrieveFrontPageSubmissionsUseCase.run(requestOptions) }
        verifyAll { repo.subreddit().getSubmissions(requestOptions) }
    }

}