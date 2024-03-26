package com.example.clickvoteandroid

import com.google.gson.annotations.SerializedName

data class Election(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("duration_days") val durationDays: Int,
    @SerializedName("options") val options: List<ElectionOption>
)
