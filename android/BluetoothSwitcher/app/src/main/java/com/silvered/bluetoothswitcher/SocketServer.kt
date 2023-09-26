package com.silvered.bluetoothswitcher

import android.util.Log
import java.net.Socket

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
        onMessageReceived: (message: String) -> Unit,
        onError: (error: Exception) -> Unit
    ) {
        try {
            Log.d("SocketIO", "Connecting to server: $ip:$port")
            socket = Socket(ip, port)
            onConnected()
            while (socket.isConnected) {
                val message = socket.getInputStream().bufferedReader().readLine()
                Log.d("SocketIO", "Message received: $message")
                onMessageReceived(message)
            }
        } catch (e: Exception) {
            onError(e)
        }
    }

    fun stop(onDisconnected: () -> Unit, onError: (error: Exception) -> Unit) {
        try {
            socket.close()
            onDisconnected()
        } catch (e: Exception) {
            onError(e)
        }
    }

    fun sendMessage(message: String, onError: (error: Exception) -> Unit) {
        try {
            val outputStream = socket.getOutputStream()
            outputStream.write((message+"\n").toByteArray())
        } catch (e: Exception) {
            onError(e)
        }
    }
}