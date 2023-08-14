package com.silvered.bluetoothswitcher

import android.R.attr.data
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import androidx.activity.ComponentActivity
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.security.Security
import java.util.Arrays
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


data class DeviceInfo(
    val name: String,
    val host: String,
    val ip: String,
    val port: Int,
)

class NearbyManager(
    private val context: ComponentActivity,
    private val onDeviceFound: (deviceList: List<DeviceInfo>) -> Unit = {}
): NsdManager.DiscoveryListener {

    private val TAG = "NearbyManager"

    private val nsdManager: NsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager

    private val SERVICE_TYPE = "_ble._tcp" // Replace this with your desired service type

    private val deviceList = mutableListOf<DeviceInfo>()

    fun startDiscovery() {
        nsdManager.discoverServices(
            SERVICE_TYPE,
            NsdManager.PROTOCOL_DNS_SD,
            this
        )
    }

    fun stopDiscovery() {
        nsdManager.stopServiceDiscovery(this)
    }

    override fun onDiscoveryStarted(serviceType: String) {
        // Discovery started
        Log.d(TAG, "onDiscoveryStarted: $serviceType")
    }

    override fun onServiceFound(serviceInfo: NsdServiceInfo) {
        // Service found, you can access its details through serviceInfo object
        if (serviceInfo != null) {
            Log.d(TAG, "onServiceFound: $serviceInfo")
            nsdManager.resolveService(serviceInfo, object: NsdManager.ResolveListener {
                override fun onResolveFailed(p0: NsdServiceInfo?, p1: Int) {
                    Log.d(TAG, "onResolveFailed: $p0, $p1")
                }

                override fun onServiceResolved(service: NsdServiceInfo?) {
                    if (service != null) {
                        val map = parseNsdTxtRecordToMap(service.attributes)
                        Log.d(TAG, "onServiceResolved: $map")
                        val host = map.getString("host") ?: ""
                        val ip = map.getString("ip") ?: ""
                        val port = map.getInt("port") ?: -1

                        if (host != "" && ip != "" && port != -1) {
                            deviceList.add(DeviceInfo(service.serviceName, host, ip, port))
                            Log.d(TAG, "onServiceResolved: $deviceList")
                            onDeviceFound(deviceList)
                        }

                    } else {
                        Log.d(TAG, "onServiceResolved: service is null")
                    }
                }

            })
        }

    }

    override fun onServiceLost(serviceInfo: NsdServiceInfo) {
        // Service lost (disappeared from the network)
        deviceList.removeIf { it.name == serviceInfo.serviceName }
        onDeviceFound(deviceList)
    }

    override fun onDiscoveryStopped(serviceType: String) {
        // Discovery stopped
        Log.d(TAG, "onDiscoveryStopped: $serviceType")
    }

    override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
        // Discovery start failed
        Log.d(TAG, "onStartDiscoveryFailed: $serviceType, errorCode: $errorCode")
    }

    override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
        // Discovery stop failed
        Log.d(TAG, "onStopDiscoveryFailed: $serviceType, errorCode: $errorCode")
    }

    fun parseNsdTxtRecordToMap(txtRecord: Map<String, ByteArray>?): JSONObject {

        if (txtRecord == null || txtRecord.isEmpty()) {
            return JSONObject()
        }

        if (txtRecord.containsKey("key")) {
            val key = String(txtRecord["key"]!!, StandardCharsets.UTF_8)
            //val json = decrypt(key)
            return JSONObject(key)
        }

        return JSONObject()
    }

    fun decrypt(encryptedText: String): String {
        try {

            val keyByte: ByteArray = Base64.getUrlDecoder().decode("keyFake")
            val decodedkey = Base64.getUrlDecoder().decode(keyByte)
            val key = Arrays.copyOfRange(decodedkey, 16, decodedkey.size)

            val decoded: ByteArray = Base64.getUrlDecoder().decode(encryptedText)

            val iv = decoded.copyOfRange(9, 25)
            val cypertext = decoded.copyOfRange(25, decoded.size - 32)

            Security.addProvider(BouncyCastleProvider())
            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC")
            val secretKey = SecretKeySpec(key, "AES")
            val ivParameterSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)
            return String(cipher.doFinal(cypertext))

        } catch (e: Exception) {
            println("Error while encrypting: $e")
            return ""
        }
    }



}