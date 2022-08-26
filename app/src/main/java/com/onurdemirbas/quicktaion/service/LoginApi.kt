package com.onurdemirbas.quicktaion.service

import com.onurdemirbas.quicktaion.model.Login
import com.onurdemirbas.quicktaion.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApi {

    @Headers("Content-Type:application/json")
    @POST("/login")
    suspend fun postLoginApi(@Body request: Login): LoginResponse
}