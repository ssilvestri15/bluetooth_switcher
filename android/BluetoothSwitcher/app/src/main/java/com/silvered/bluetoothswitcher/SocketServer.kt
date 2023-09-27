package com.silvered.bluetoothswitcher

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.Socket

class SocketServer private constructor() {


    private lateinit var socket: Socket
    val connectedDeviceList = mutableListOf<DeviceInfo>()

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
            Log.d("Socket", "Connecting to server: $ip:$port")
            socket = Socket(ip, port)
            onConnected()
            while (socket.isConnected) {
                val message = socket.getInputStream().bufferedReader().readLine()
                Log.d("Socket", "Message received: $message")
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
    fun saveDevice(sharedPreferences: SharedPreferences, deviceJson: String ,onSaved: (success: Boolean) -> Unit = {}) {
        try {
            val deviceMapString = sharedPreferences.getString("device_list", null)
            var deviceMap: MutableMap<String, DeviceInfo>? = null

            if (deviceMapString == null) {
                deviceMap = mutableMapOf()
            } else {
                deviceMap = Gson().fromJson(deviceMapString, object : TypeToken<MutableMap<String, DeviceInfo>>() {}.type)
            }

            if (deviceMap == null) {
                deviceMap = mutableMapOf()
            }

            sharedPreferences.edit().apply {
                remove("device_list")
                commit()
            }.let {
                val device = Gson().fromJson(deviceJson, DeviceInfo::class.java)
                deviceMap[device.host] = Gson().fromJson(deviceJson, DeviceInfo::class.java)
                sharedPreferences.edit().apply {
                    putString("device_list", Gson().toJson(deviceMap))
                    commit()
                }
            }
            val device = Gson().fromJson(deviceJson, DeviceInfo::class.java)
            connectedDeviceList.add(device)
            onSaved(true)
        } catch (e: Exception) {
            Log.e("SocketServer", "Error saving device: ${e.printStackTrace()}")
            onSaved(false)
        }
    }

}