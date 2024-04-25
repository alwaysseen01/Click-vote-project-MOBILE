package com.example.clickvoteandroid.api

import com.example.clickvoteandroid.dataClasses.Survey
import com.example.clickvoteandroid.dataClasses.SurveyOption
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SurveyApi {
    @GET("/surveys/active")
    fun getActiveSurveys(): Call<List<Survey>>

    @GET("surveys/completed")
    fun getCompletedSurveys(): Call<List<Survey>>

    @GET("surveys/{survey_id}/winner")
    fun getCompletedSurveyWinner(@Path("survey_id") surveyId: Long): Call<SurveyOption>?
}