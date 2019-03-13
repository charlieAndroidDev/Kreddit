package com.cniekirk.kreddit.data.api.repo

import com.cniekirk.kreddit.data.models.ImageInformation
import com.cniekirk.kreddit.utils.Either
import com.cniekirk.kreddit.utils.Failure

interface ImgurRepository {

    fun getImageInformation(imageHash: String): Either<Failure, ImageInformation>

}