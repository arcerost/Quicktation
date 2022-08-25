package com.onurdemirbas.quicktaion.service

import com.onurdemirbas.quicktaion.model.Register
import com.onurdemirbas.quicktaion.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegisterApi {

    @Headers("Content-Type:application/json")
    @POST("/register")
    suspend fun postRegisterApi(@Body request: Register): RegisterResponse
}