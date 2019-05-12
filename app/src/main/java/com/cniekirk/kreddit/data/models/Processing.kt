package com.cniekirk.kreddit.data.models

import com.squareup.moshi.Json

/**
 * JSON model class to be parsed from an Imgur API response
 */
data class Processing(
    @Json(name = "status")
    val status: String
) {

    companion object {
        fun empty() = Processing("")
    }

}