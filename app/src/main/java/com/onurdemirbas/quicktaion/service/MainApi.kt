package com.onurdemirbas.quicktaion.service

import com.onurdemirbas.quicktaion.model.Main
import com.onurdemirbas.quicktaion.model.MainResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MainApi {

    @Headers("Content-Type:application/json")
    @POST("/homepageitems")
    suspend fun postMainApi(@Body request: Main): MainResponse //: MainResponse
}