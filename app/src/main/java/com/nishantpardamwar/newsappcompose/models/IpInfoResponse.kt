package com.nishantpardamwar.newsappcompose.models

import com.google.gson.annotations.SerializedName

data class IpInfoResponse(
    @SerializedName("status") val status: String,
    @SerializedName("country") val country: String,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("region") val region: String,
    @SerializedName("regionName") val regionName: String,
    @SerializedName("city") val city: String,
    @SerializedName("zip") val zip: Int,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("isp") val isp: String,
    @SerializedName("org") val org: String,
    @SerializedName("query") val query: String
)