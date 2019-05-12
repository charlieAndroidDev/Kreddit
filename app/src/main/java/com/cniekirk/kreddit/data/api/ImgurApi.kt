package com.cniekirk.kreddit.data.api

import com.cniekirk.kreddit.data.models.ImageInformation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * The retrofit API definition for the Imgur service
 */
interface ImgurApi {

    /**
     * @param imageHash: The hash associated with the image
     * @return A [Call] object representing the HTTP request
     */
    @Headers(
        ImgurConstants.HEADER_AUTH_IMGUR,
        ImgurConstants.HEADER_RAPIDAPI_KEY
    )
    @GET("/3/image/{imageHash}")
    fun getImageInformation(@Path("imageHash") imageHash: String): Call<ImageInformation>

}