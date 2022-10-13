package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.Home
import com.onurdemirbas.quicktation.model.HomeResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface HomeApi {

    @Headers("Content-Type:application/json")
    @POST("homepageitems")
    suspend fun postMainApi(@Body request: Home): HomeResponse //: MainResponse

    @Headers("Content-Type:application/json")
    @POST("homepageitems")
    suspend fun postMainScanApi(@Body request: Home): HomeResponse //: MainResponse
}