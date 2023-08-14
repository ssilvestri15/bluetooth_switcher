package com.silvered.bluetoothswitcher

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter


class SocketServer private constructor() {


    private lateinit var socket: Socket

    companion object {
        private var instance: SocketServer? = null

        fun getInstance(): SocketServer {
            if (instance == null) {
                instance = SocketServer()
            }
            return instance!!
        }
    }

    fun startSocketConnection(
        ip: String,
        port: Int,
        onConnected: () -> Unit ,
        onDisconnected: () -> Unit,
        onMessageReceived: (message: String) -> Unit,
    ) {
        try {
            socket = IO.socket("http://$ip:$port")

            // Set up event listeners
            socket.on(Socket.EVENT_CONNECT) { onConnected() }
            socket.on(Socket.EVENT_DISCONNECT) { onDisconnected() }
            socket.on("message") { args ->
                val message = args[0] as String
                onMessageReceived(message)
            }
            socket.connect()
        } catch (e: Exception) {
            Log.e("SocketIO", "Error connecting to server: ${e.printStackTrace()}")
            return
        }
    }

    fun stopSocketConnection() {
        socket.disconnect()
    }

    fun sendMessage(message: String) {
        socket.emit("message", message)
    }
}