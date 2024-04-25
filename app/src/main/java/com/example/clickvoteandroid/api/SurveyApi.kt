package com.example.clickvoteandroid.api

import com.example.clickvoteandroid.dataClasses.Survey
import retrofit2.Call
import retrofit2.http.GET

interface SurveyApi {
    @GET("/surveys/active")
    fun getActiveSurveys(): Call<List<Survey>>
}