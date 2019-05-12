package com.cniekirk.kreddit.data.models

import com.squareup.moshi.Json

/**
 * JSON model class to be parsed from an Imgur API response
 */
data class ImageInformation(
    @Json(name = "data")
    val data: Data,
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "status")
    val status: Int
) {

    companion object {
        fun empty() = ImageInformation(Data.empty(), false, 0)
    }

}