package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.FollowUnFollowUser
import com.onurdemirbas.quicktation.model.FollowUnFollowUserResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FollowUnfollowUserApi {
    @Headers("Content-Type:application/json")
    @POST("followUnfollowUser")
    suspend fun postFollowUnfollowUserApi(@Body request: FollowUnFollowUser): FollowUnFollowUserResponse
}