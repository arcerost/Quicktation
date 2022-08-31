package com.onurdemirbas.quicktaion.service

import com.onurdemirbas.quicktaion.model.*
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ForgotPwApi {

    @Headers("Content-Type:application/json")
    @POST("/forgotpassword")
        suspend fun postForgotApi(@Body request: ForgotPassword): ForgotPasswordResponse



    @Headers("Content-Type:application/json")
    @POST("/checkauthcode")
    suspend fun postCheckApi(@Body request: CheckAuthPw): CheckAuthPwResponse


    @Headers("Content-Type:application/json")
    @POST("/updateuserpassword")
    suspend fun postUpdateApi(@Body request: UpdatePw): UpdatePwResponse
}