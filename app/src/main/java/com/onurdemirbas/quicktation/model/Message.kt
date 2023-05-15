package com.onurdemirbas.quicktation.model

data class Message(
    val id: Int,
    val sender: Int,
    val receiver: Int,
    val lastMessageValue: String,
    val lastMessageDate: String,
    val lastMessageType: String,
    val senderName: Any,
    val senderPhoto: Any,
    val senderNick: String
)