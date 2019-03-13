package com.cniekirk.kreddit.data.models

import com.squareup.moshi.Json

data class ImageInformation(
    @Json(name = "data")
    val data: Data,
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "status")
    val status: Int
)