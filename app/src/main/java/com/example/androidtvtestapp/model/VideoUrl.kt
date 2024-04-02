package com.example.androidtvtestapp.model

import com.google.gson.annotations.SerializedName

data class VideoUrl(
    val uri: String,
    @SerializedName("stream_service_id") val streamServiceId: Int,
    @SerializedName("ext_id") val extId: String,
    @SerializedName("ext_partner_id") val extPartnerId: String,
    @SerializedName("ext_auth_token") val extAuthToken: String?,
    @SerializedName("ext_apikey") val extApiKey: String?,
    val error: Int,
    @SerializedName("error_message") var errorMessage: String
)