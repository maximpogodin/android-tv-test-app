package com.example.androidtvtestapp.model

import com.google.gson.annotations.SerializedName

data class File(
    val id: Int,
    val name: String,
    val duration: Int,
    @SerializedName("promo_image") val promoImage: String,
    val quality: Int,
    @SerializedName("ext_id") val extId: String,
    val position: Int,
    @SerializedName("is_viewed") val isViewed: Int
)