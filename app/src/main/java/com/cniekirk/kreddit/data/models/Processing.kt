package com.cniekirk.kreddit.data.models

import com.squareup.moshi.Json

data class Processing(
    @Json(name = "status")
    val status: String
)