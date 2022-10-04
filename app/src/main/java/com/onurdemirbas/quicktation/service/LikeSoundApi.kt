package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.LikeQuoteSound
import com.onurdemirbas.quicktation.model.LikeSoundResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LikeSoundApi {
    @Headers("Content-Type:application/json")
    @POST("/likequotesound")
    suspend fun postLikeSoundApi(@Body request: LikeQuoteSound): LikeSoundResponse
}