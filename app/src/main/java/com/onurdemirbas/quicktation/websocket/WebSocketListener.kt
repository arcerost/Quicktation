package com.onurdemirbas.quicktation.websocket

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.Response
import okhttp3.WebSocket
import org.json.JSONException
import org.json.JSONObject

class WebSocketListener(private val myId: Int) : okhttp3.WebSocketListener() {
    data class Message(
        val text: String,
        val fromUserId: Int,
        val toUserId: Int,
        private val sent: Boolean
    ) : BaseMessage {
        override val messageText: String
            get() = text
        override val receiverId: Int
            get() = toUserId
        override val senderId: Int
            get() = fromUserId
        override val isSent: Boolean
            get() = sent
    }
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    override fun onOpen(webSocket: WebSocket, response: Response) {
        outPut("Connected")
        super.onOpen(webSocket, response)
        onAssignUser(webSocket, myId)
    }
    override fun onMessage(webSocket: WebSocket, text: String) {
        try {
            val jsonReceived = JSONObject(text)
            when (jsonReceived.optString("type")) {
                "sendMessage" -> {
                    val messageText = jsonReceived.optString("messageText")
                    val fromUserId = jsonReceived.optInt("userId")
                    val toUserId = jsonReceived.optInt("toUserId")
                    val isSent = fromUserId == myId
                    outPut("Received message: $messageText from user: $fromUserId to user: $toUserId")
                    _messages.value = _messages.value + Message(messageText, fromUserId, toUserId, isSent)
                }
                else -> {
                    val error = jsonReceived.optInt("error")
                    val errorText = jsonReceived.optString("errorText")
                    val response = jsonReceived.optJSONObject("response")
                    if (error != 0) {
                        outPut("Received error: $errorText")
                    } else if (response != null) {
                        val messageText = response.optString("messageValue")
                        val fromUserId = response.optInt("comingFrom")
                        val toUserId = response.optInt("toUser")
                        val messageId = response.opt("messageId")
                        val isSent = fromUserId == myId
                        val newMessage = Message(messageText, fromUserId, toUserId, isSent)
                        if (!_messages.value.contains(newMessage)) {
                            _messages.value = _messages.value + newMessage
                        }
                        outPut("Received message: $messageText from user: $fromUserId to user: $toUserId: messageId: $messageId")
                    } else {
                        outPut(errorText)
                    }
                }
            }
        } catch (e: JSONException) {
            outPut("Error parsing JSON: ${e.message}")
        }
    }

    fun sendMessage(webSocket: WebSocket, messageText: String, toUserId: Int) {
        Log.d("idCheck","myId: $myId, toUserId: $toUserId")
        val message = Message(messageText, myId, toUserId, true)
        _messages.value = _messages.value + message
        val jsonToSend = JSONObject()
        jsonToSend.put("type", "sendMessage")
        jsonToSend.put("messageText", messageText)
        jsonToSend.put("userId", myId)
        jsonToSend.put("toUserId", toUserId)
        webSocket.send(jsonToSend.toString())
    }

    private fun onAssignUser(webSocket: WebSocket, userId: Int) {
        val json = JSONObject()
        json.put("userId", userId)
        json.put("type", "assignUser")
        val message = json.toString()
        webSocket.send(message)
        outPut("onAssignUser")
    }
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSE_STATUS,null)
        outPut("Closing : kapanıyor $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        outPut("Error: hata ${t.message} ")
    }

    companion object{
        const val NORMAL_CLOSE_STATUS = 1000
    }
    private fun outPut(text: String)
    {
        Log.d("onur",text)
    }
}