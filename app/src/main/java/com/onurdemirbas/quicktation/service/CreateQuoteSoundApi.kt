package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.CreateQuoteSound
import com.onurdemirbas.quicktation.model.CreateQuoteSoundResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CreateQuoteSoundApi {
    @Headers("Content-Type:application/json")
    @POST("createQuoteSound")
    suspend fun postDeleteQuoteApi(@Body request: CreateQuoteSound): CreateQuoteSoundResponse
}