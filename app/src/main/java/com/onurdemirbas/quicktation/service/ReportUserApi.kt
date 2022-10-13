package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.ReportUser
import com.onurdemirbas.quicktation.model.ReportUserResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ReportUserApi {

    @Headers("Content-Type:application/json")
    @POST("reportUser")
    suspend fun postReportUserApi(@Body request: ReportUser): ReportUserResponse
}