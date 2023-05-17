package com.onurdemirbas.quicktation.websocket

interface BaseMessage {
    val messageText: String
    val senderId: Int
    val receiverId: Int
    val isSent: Boolean
}