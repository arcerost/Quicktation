package com.onurdemirbas.quicktaion.service

import com.onurdemirbas.quicktaion.model.Like
import com.onurdemirbas.quicktaion.model.LikeResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LikeApi {
    @Headers("Content-Type:application/json")
    @POST("/likequote")
    suspend fun postLikeApi(@Body request: Like): LikeResponse
}