package com.example.clickvoteandroid.dataClasses

import com.google.gson.annotations.SerializedName

data class Petition(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("shortDescription") val shortDescription: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("duration_days") val durationDays: Int,
    @SerializedName("votesCount") val votesCount: Int,
)
