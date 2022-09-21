package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.Login
import com.onurdemirbas.quicktation.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApi {

    @Headers("Content-Type:application/json")
    @POST("/login")
    suspend fun postLoginApi(@Body request: Login): LoginResponse
}