package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.EditProfile
import com.onurdemirbas.quicktation.model.EditProfileResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface EditProfileApi {
    @Headers("Content-Type:application/json")
    @POST("/userfollows")
    suspend fun postEditProfileApi(@Body request: EditProfile): EditProfileResponse
}