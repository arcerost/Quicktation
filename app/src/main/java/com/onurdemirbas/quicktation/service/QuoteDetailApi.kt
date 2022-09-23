package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.QuoteDetail
import com.onurdemirbas.quicktation.model.QuoteDetailResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface QuoteDetailApi {
    @Headers("Content-Type:application/json")
    @POST("/quotationdetail")
    suspend fun postQuoteDetailApi(@Body request: QuoteDetail): QuoteDetailResponse
}