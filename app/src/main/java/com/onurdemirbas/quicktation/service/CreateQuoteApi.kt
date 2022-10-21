package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.CreateQuote
import com.onurdemirbas.quicktation.model.CreateQuoteResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CreateQuoteApi {
    @Headers("Content-Type:application/json")
    @POST("createQuote")
    suspend fun postDeleteQuoteApi(@Body request: CreateQuote): CreateQuoteResponse
}