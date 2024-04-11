package com.example.clickvoteandroid

import retrofit2.Call
import retrofit2.http.GET

interface PetitionsApi {
    @GET("/petitions/active")
    fun getActivePetitions(): Call<List<Petition>>
}