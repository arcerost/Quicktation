package com.onurdemirbas.quicktation.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationsService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains a data payload.
        remoteMessage.data.let {
            // Handle the data message here.
            // You can use Jetpack Compose here to update your UI based on the notification
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            // Handle the notification message here.
            // You can use Jetpack Compose here to update your UI based on the notification
        }
    }

    override fun onNewToken(token: String) {
        // Log and optionally send the new registration token to your app server.
        Log.d("TAG", "New token: $token")
        // sendRegistrationToServer(token)
    }
}