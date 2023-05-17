package com.onurdemirbas.quicktation.model

data class DetailMessage(val id: Int,
                         val comingFrom: Int,
                         val toUser: Int,
                         val messageValue: String,
                         val messageType: String,
                         val createDate: String,
                         val roomId: Int,
                         val senderName: String,
                         val senderNick: String,
                         val receiverPhoto: String,
                         val receiverName: String,
                         val receiverNick: String)
