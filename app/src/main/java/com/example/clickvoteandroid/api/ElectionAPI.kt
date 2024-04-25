package com.example.clickvoteandroid.api

import com.example.clickvoteandroid.dataClasses.Election
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ElectionsApi {
    @GET("/elections/active")
    fun getActiveElections(): Call<List<Election>>

    @GET("/elections/completed")
    fun getCompletedElections(): Call<List<Election>>

    @GET("/elections/{election_id}/results")
    fun getCompletedElectionResults(@Path("election_id") electionId: Long): Call<List<Map<String, Any>>>
}
