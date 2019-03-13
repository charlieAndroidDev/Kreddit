package com.cniekirk.kreddit.domain

import com.cniekirk.kreddit.data.api.repo.ImgurRepository
import com.cniekirk.kreddit.data.models.ImageInformation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetImageInformationUseCase @Inject constructor(private val imgurRepository: ImgurRepository):
    BaseUseCase<ImageInformation, String>() {

    override suspend fun run(params: String) = imgurRepository.getImageInformation(params)

}