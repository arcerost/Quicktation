package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.DeleteQuote
import com.onurdemirbas.quicktation.model.DeleteQuoteResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DeleteQuoteApi {
    @Headers("Content-Type:application/json")
    @POST("deleteQuote")
    suspend fun postDeleteQuoteApi(@Body request: DeleteQuote): DeleteQuoteResponse
}