package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.MyProfileResponse
import com.onurdemirbas.quicktation.model.ProfileDetail
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MyProfileApi {


    @Headers("Content-Type:application/json")
    @POST("/profiledetail")
    suspend fun postMyProfileApi(@Body request: ProfileDetail) : MyProfileResponse
}