package com.example.androidtvtestapp.model

import com.google.gson.annotations.SerializedName

data class Video(
    val id: Int,
    val name: String,
    @SerializedName("name_orig") val originalName: String?,
    val files: List<File>,
    @SerializedName("thumbnail_small") val thumbnailSmall: String,
    @SerializedName("thumbnail_big") val thumbnailBig: String,
    @SerializedName("ad_banner") val adBanner: String,
    val description: String,
    val year: Int,
    val actors: String,
    val director: String,
    val genres: String,
    val countries: String,
    @SerializedName("genres_kinopoisk") val genresKinopoisk: String,
    @SerializedName("is_parent_control") val isParentControl: Int,
    @SerializedName("is_package") val isPackage: Int,
    @SerializedName("is_season") val isSeason: Int,
    @SerializedName("is_announcement") val isAnnouncement: Int,
    @SerializedName("has_subscription") val hasSubscription: Int,
    val rating: Int,
    @SerializedName("kinopoisk_rating") val kinopoiskRating: Double,
    @SerializedName("imdb_rating") val imdbRating: Double,
    @SerializedName("average_customers_rating") val averageCustomersRating: Double,
    val duration: Int,
    val provider: String,
    val language: String,
    @SerializedName("is_purchased") val isPurchased: Int,
    @SerializedName("is_favorited") val isFavorited: Int,
    @SerializedName("is_viewed") val isViewed: Int,
    @SerializedName("screenshot_big") val screenshotBig: String,
    @SerializedName("screenshot_b_big") val screenshotBigB: String,
    @SerializedName("video_source") val videoSource: Int,
    val position: Int,
    @SerializedName("position_asset_id") val positionAssetId: Int,
    @SerializedName("price_category_type") val priceCategoryType: Int,
    @SerializedName("video_provider_id") val videoProviderId: Int,
    @SerializedName("is_4k") val is4k: Int
)