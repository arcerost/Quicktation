package com.onurdemirbas.quicktation.service

import com.onurdemirbas.quicktation.model.Notifications
import com.onurdemirbas.quicktation.model.NotificationsResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationsApi {
    @Headers("Content-Type:application/json")
    @POST("/notification")
    suspend fun postNotificationApi(@Body request: Notifications): ArrayList<NotificationsResponse>
}