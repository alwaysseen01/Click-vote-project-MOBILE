package com.example.clickvoteandroid

import retrofit2.Call
import retrofit2.http.GET

interface SurveyApi {
    @GET("/surveys/active")
    fun getActiveSurveys(): Call<List<Survey>>
}