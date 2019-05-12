package com.cniekirk.kreddit.repo

import com.cniekirk.kreddit.core.platform.NetworkHandler
import com.cniekirk.kreddit.data.api.ImgurApi
import com.cniekirk.kreddit.data.api.repo.ImgurRepositoryImpl
import com.cniekirk.kreddit.data.models.ImageInformation
import com.cniekirk.kreddit.utils.Either
import com.cniekirk.kreddit.utils.Failure
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class ImgurRepositoryTest {

    private lateinit var imgurRepository: ImgurRepositoryImpl

    private val imageHash = "HASH"

    private val imgurApi = mockk<ImgurApi>()
    private val networkHandler = mockk<NetworkHandler>()
    private val imageInformationCall = mockk<Call<ImageInformation>>()
    private val imageInformationResponse = mockk<Response<ImageInformation>>()

    @Before
    fun setup() {

        imgurRepository = ImgurRepositoryImpl(imgurApi, networkHandler)
    }

    @Test
    fun `should retrieve image information from api`() {

        every { networkHandler.isConnected } returns true
        every { imageInformationResponse.body() } returns ImageInformation.empty()
        every { imageInformationResponse.isSuccessful } returns true
        every { imageInformationCall.execute() } returns imageInformationResponse
        every { imgurApi.getImageInformation(imageHash) } returns imageInformationCall

        val imageInformation = imgurRepository.getImageInformation(imageHash)

        imageInformation shouldEqual Either.Right(ImageInformation.empty())
        verifyAll { imgurApi.getImageInformation(imageHash) }
    }

    @Test
    fun `should return network error when network not connected`() {

        every { networkHandler.isConnected } returns false

        val imageInformation = imgurRepository.getImageInformation(imageHash)

        imageInformation.isLeft shouldEqual true
        imageInformation.either( { failure -> failure shouldBeInstanceOf Failure.NetworkConnectionError::class.java }, {} )

    }

}