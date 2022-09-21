package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.Register
import com.onurdemirbas.quicktation.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegisterApi {

    @Headers("Content-Type:application/json")
    @POST("/register")
    suspend fun postRegisterApi(@Body request: Register): RegisterResponse
}