package com.example.clickvoteandroid.dataClasses

import com.google.gson.annotations.SerializedName

data class SurveyOption(
    @SerializedName("id") val id: Long,
    @SerializedName("text") val text: String,
    @SerializedName("votesCount") val votesCount: Int,
)
