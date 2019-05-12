package com.cniekirk.kreddit.domain

import com.cniekirk.kreddit.data.api.repo.ImgurRepository
import com.cniekirk.kreddit.data.models.ImageInformation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * UseCase to get information about an Imgur Image
 * @param imgurRepository: The [ImgurRepository] which provides functions to get information from an image
 */
@ExperimentalCoroutinesApi
class GetImageInformationUseCase @Inject constructor(private val imgurRepository: ImgurRepository):
    BaseUseCase<ImageInformation, String>() {

    /**
     * Long running operation to get information about an Imgur image
     * @param params: The Imgur image hash
     */
    override suspend fun run(params: String) = imgurRepository.getImageInformation(params)

}