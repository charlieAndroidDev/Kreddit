package com.cniekirk.kreddit.usecase

import com.cniekirk.kreddit.data.api.repo.ImgurRepository
import com.cniekirk.kreddit.data.models.ImageInformation
import com.cniekirk.kreddit.domain.GetImageInformationUseCase
import com.cniekirk.kreddit.utils.Either
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Test to make sure UseCase invocation calls appropriate repository function
 */
@ExperimentalCoroutinesApi
class GetImageDetailsTest {

    // Mock the required Class
    private val repo = mockk<ImgurRepository>()

    private lateinit var getImageInformationUseCase: GetImageInformationUseCase
    private val imageHash = "HASH"

    /**
     * Initialise the SUT (System Under Test)
     */
    @Before
    fun setup() {
        getImageInformationUseCase = GetImageInformationUseCase(repo)
        every { repo.getImageInformation(imageHash) } returns Either.Right(ImageInformation.empty())
    }

    /**
     * The actual test.  [verifyAll] checks to make sure behaviour captured
     * in the lambda is executed and nothing else
     *
     * We can use [runBlocking] as the UseCase's [run] is a suspending function
     * This makes it very easy to test and implement for real
     */
    @Test
    fun `get image information queries repository`() {
        runBlocking { getImageInformationUseCase.run(imageHash) }
        verifyAll { repo.getImageInformation(imageHash) }
    }

}