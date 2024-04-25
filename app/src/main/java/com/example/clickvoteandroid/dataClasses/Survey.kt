package com.example.clickvoteandroid.dataClasses

import com.google.gson.annotations.SerializedName

data class Survey(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("duration_days") val durationDays: Int,
    @SerializedName("options") val options: List<SurveyOption>
)
