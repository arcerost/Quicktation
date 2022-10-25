package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.*
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface HomeApi {

    @Headers("Content-Type:application/json")
    @POST("homepageitems")
    suspend fun postMainApi(@Body request: Home): HomeResponse

    @Headers("Content-Type:application/json")
    @POST("homepageitems")
    suspend fun postMainScanApi(@Body request: Home): HomeResponse

    @Headers("Content-Type:application/json")
    @POST("search")
    suspend fun postSearchApi(@Body request: Search): SearchResponse //user

    @Headers("Content-Type:application/json")
    @POST("search")
    suspend fun postSearchQuoteApi(@Body request: Search): SearchQuoteResponse //quote
}