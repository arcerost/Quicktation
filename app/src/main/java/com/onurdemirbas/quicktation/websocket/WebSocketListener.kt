package com.onurdemirbas.quicktation.websocket

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.protobuf.ByteString
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import org.json.JSONObject

class WebSocketListener(id: String) : okhttp3.WebSocketListener() {
    val myId = id
    override fun onOpen(webSocket: WebSocket, response: Response) {
        outPut("Connected")
        super.onOpen(webSocket, response)
//        onAssignUser(webSocket, 2, "assignUser")
    }
    private var wsData = mutableStateOf ("")
    override fun onMessage(webSocket: WebSocket, text: String) {
        outPut("Received: $text")
    }
    fun onAssignUser(webSocket: WebSocket, userId: Int, type: String) {
        val json = JSONObject()
        json.put("userId", userId)
        json.put("type", type)
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
        private const val NORMAL_CLOSE_STATUS = 1000
    }
    private fun outPut(text: String)
    {
        Log.d("onur",text)
    }
}

class SocketAbortedException : Exception()

data class SocketUpdate(val text: String? = null, val byteString: ByteString? = null, val exception: Throwable? = null)