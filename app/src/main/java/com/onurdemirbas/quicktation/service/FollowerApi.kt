package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.FollowResponse
import com.onurdemirbas.quicktation.model.Follower
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FollowerApi {

    @Headers("Content-Type:application/json")
    @POST("userfollows")
    suspend fun postFollowerApi(@Body request: Follower): FollowResponse
}