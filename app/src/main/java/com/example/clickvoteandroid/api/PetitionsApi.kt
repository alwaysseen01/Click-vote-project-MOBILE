package com.example.clickvoteandroid.api

import com.example.clickvoteandroid.dataClasses.Petition
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PetitionsApi {
    @GET("/petitions/active")
    fun getActivePetitions(): Call<List<Petition>>

    @GET("/petitions/completed")
    fun getCompletedPetitions(): Call<List<Petition>>
}