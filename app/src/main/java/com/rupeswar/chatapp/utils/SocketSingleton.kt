package com.rupeswar.chatapp.utils

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SocketSingleton {

    companion object {
        lateinit var socket: Socket

        fun initialiseSocket(URI: String) {
            try {
                val options = IO.Options.builder()
                    .setAuth(mapOf(
                        "token" to AuthUtil.authToken
                    ))
                    .build()
                socket = IO.socket(URI, options)
                socket.connect()
                Log.d("Socket Event", "Socket Connected")
            } catch (e: URISyntaxException) {
                Log.e("Socket Exception", e.localizedMessage ?: "null")
            }
        }
    }
}