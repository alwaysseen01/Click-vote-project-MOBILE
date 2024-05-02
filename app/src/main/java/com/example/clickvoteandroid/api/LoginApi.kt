package com.example.clickvoteandroid.api

import com.example.clickvoteandroid.LoginActivity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.clickvoteandroid.dataClasses.LoginRequest

interface LoginApi {
    @POST("/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginActivity.LoginResponse>
}