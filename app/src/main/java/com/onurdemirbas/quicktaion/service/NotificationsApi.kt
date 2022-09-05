package com.onurdemirbas.quicktaion.service

import com.onurdemirbas.quicktaion.model.Notifications
import com.onurdemirbas.quicktaion.model.NotificationsResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationsApi {
    @Headers("Content-Type:application/json")
    @POST("/notification")
    suspend fun postNotificationApi(@Body request: Notifications): ArrayList<NotificationsResponse>
}