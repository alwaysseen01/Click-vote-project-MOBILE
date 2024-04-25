package com.example.clickvoteandroid.dataClasses

import com.google.gson.annotations.SerializedName

data class ElectionOption(
    @SerializedName("id") val id: Long,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("middleName") val middleName: String,
    @SerializedName("dateOfBirth") val dateOfBirth: String,
    @SerializedName("address") val address: String,
    @SerializedName("position") val position: String,
    @SerializedName("shortDescription") val shortDescription: String,
    @SerializedName("longDescription") val longDescription: String,
    @SerializedName("photoUrl") val photoUrl: String,
    @SerializedName("votesCount") val votesCount: Int,
)

