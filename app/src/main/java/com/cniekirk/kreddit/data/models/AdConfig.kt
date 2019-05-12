package com.cniekirk.kreddit.data.models

import com.squareup.moshi.Json

/**
 * JSON model class to be parsed from an Imgur API response
 */
data class AdConfig(
    @Json(name = "safeFlags")
    val safeFlags: List<String>,
    @Json(name = "highRiskFlags")
    val highRiskFlags: List<String>,
    @Json(name = "unsafeFlags")
    val unsafeFlags: List<String>,
    @Json(name = "showAds")
    val showAds: Boolean
) {

    companion object {
        fun empty() = AdConfig(emptyList(), emptyList(), emptyList(), false)
    }

}