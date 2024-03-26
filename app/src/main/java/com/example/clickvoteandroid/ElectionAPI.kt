package com.example.clickvoteandroid

import retrofit2.Call
import retrofit2.http.GET

interface ElectionsApi {
    @GET("/elections/active")
    fun getActiveElections(): Call<List<Election>>
}
