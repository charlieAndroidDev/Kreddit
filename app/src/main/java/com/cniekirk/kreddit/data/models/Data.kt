package com.cniekirk.kreddit.data.models

import com.squareup.moshi.Json

/**
 * JSON model class to be parsed from an Imgur API response
 */
data class Data(
    @Json(name = "id")
    val id: String,
    @Json(name = "title")
    val title: Any,
    @Json(name = "description")
    val description: String,
    @Json(name = "datetime")
    val datetime: Int,
    @Json(name = "type")
    val type: String,
    @Json(name = "animated")
    val animated: Boolean,
    @Json(name = "width")
    val width: Int,
    @Json(name = "height")
    val height: Int,
    @Json(name = "size")
    val size: Int,
    @Json(name = "views")
    val views: Int,
    @Json(name = "bandwidth")
    val bandwidth: Long,
    @Json(name = "vote")
    val vote: Any,
    @Json(name = "favorite")
    val favorite: Boolean,
    @Json(name = "nsfw")
    val nsfw: Boolean,
    @Json(name = "section")
    val section: String,
    @Json(name = "account_url")
    val accountUrl: Any,
    @Json(name = "account_id")
    val accountId: Any,
    @Json(name = "is_ad")
    val isAd: Boolean,
    @Json(name = "in_most_viral")
    val inMostViral: Boolean,
    @Json(name = "has_sound")
    val hasSound: Boolean,
    @Json(name = "tags")
    val tags: List<Any>,
    @Json(name = "ad_type")
    val adType: Int,
    @Json(name = "ad_url")
    val adUrl: String,
    @Json(name = "edited")
    val edited: String,
    @Json(name = "in_gallery")
    val inGallery: Boolean,
    @Json(name = "link")
    val link: String,
    @Json(name = "mp4_size")
    val mp4Size: Int,
    @Json(name = "mp4")
    val mp4: String,
    @Json(name = "gifv")
    val gifv: String,
    @Json(name = "hls")
    val hls: String,
    @Json(name = "processing")
    val processing: Processing,
    @Json(name = "ad_config")
    val adConfig: AdConfig) {

    companion object {

        fun empty() = Data(
            "",
            "",
            "",
            0,
            "",
            false,
            0,
            0,
            0,
            0,
            0L,
            "",
            false,
            false,
            "",
            "",
            "",
            false,
            false,
            false,
            emptyList(),
            0,
            "",
            "",
             false,
            "",
            0,
            "",
            "",
            "",
            Processing.empty(),
            AdConfig.empty()
        )

    }

}