package com.cniekirk.kreddit.data.api.repo

import com.cniekirk.kreddit.data.models.ImageInformation
import com.cniekirk.kreddit.utils.Either
import com.cniekirk.kreddit.utils.Failure

/**
 * Repository interface for getting Imgur image information
 */
interface ImgurRepository {

    fun getImageInformation(imageHash: String): Either<Failure, ImageInformation>

}