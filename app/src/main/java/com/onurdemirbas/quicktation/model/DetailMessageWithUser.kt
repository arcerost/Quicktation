package com.onurdemirbas.quicktation.model

import com.onurdemirbas.quicktation.websocket.BaseMessage

class DetailMessageWithUser(
    private val detailMessage: DetailMessage,
    private val userId: Int
) : BaseMessage {
    override val messageText: String
        get() = detailMessage.messageValue
    override val senderId: Int
        get() = detailMessage.comingFrom
    override val receiverId: Int
        get() = detailMessage.toUser
    override val isSent: Boolean
        get() = detailMessage.comingFrom == userId
}