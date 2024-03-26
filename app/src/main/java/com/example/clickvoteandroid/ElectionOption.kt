package com.example.clickvoteandroid

import com.google.gson.annotations.SerializedName

data class ElectionOption(
    @SerializedName("id") val id: Long,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("middle_name") val middleName: String,
    @SerializedName("date_of_birth") val dateOfBirth: String,
    @SerializedName("address") val address: String,
    @SerializedName("position") val position: String,
    @SerializedName("short_description") val shortDescription: String,
    @SerializedName("long_description") val longDescription: String,
    @SerializedName("photo_url") val photoUrl: String,
    @SerializedName("votes_count") val votesCount: Int,
)

