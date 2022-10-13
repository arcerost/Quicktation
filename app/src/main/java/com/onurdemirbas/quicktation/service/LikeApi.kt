package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.Like
import com.onurdemirbas.quicktation.model.LikeResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LikeApi {
    @Headers("Content-Type:application/json")
    @POST("likequote")
    suspend fun postLikeApi(@Body request: Like): LikeResponse
}