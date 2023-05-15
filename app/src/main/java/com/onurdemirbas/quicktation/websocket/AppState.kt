package com.onurdemirbas.quicktation.websocket

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class AppState(myId: Int){
    val webSocket: WebSocket
    val listener: WebSocketListener
    init {
        val client = OkHttpClient()
        val req = Request.Builder().url("ws://63.32.138.61:4000/").build()
        Log.d("deneme1","benim $myId")
        listener = WebSocketListener(myId)
        webSocket = client.newWebSocket(req, listener)
    }
}