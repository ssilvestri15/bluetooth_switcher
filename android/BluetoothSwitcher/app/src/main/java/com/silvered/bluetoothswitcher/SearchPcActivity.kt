package com.silvered.bluetoothswitcher

import android.Manifest
import android.animation.ObjectAnimator
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.silvered.bluetoothswitcher.databinding.ActivitySearchPcAcivityBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchPcActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchPcAcivityBinding
    private lateinit var nearbyManager: NearbyManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchPcAcivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.devicesFoundRv.layoutManager = linearLayoutManager



        nearbyManager = NearbyManager(this) { deviceList ->

            if (deviceList.isEmpty()) {
                runOnUiThread {
                    binding.devicesFoundRv.visibility = View.GONE
                }
            } else {
                runOnUiThread {
                    binding.devicesFoundRv.visibility = View.VISIBLE
                    binding.devicesFoundRv.adapter = DeviceListAdapter(deviceList) { deviceInfo ->
                        nearbyManager.stopDiscovery()
                        runOnUiThread {
                            binding.linearLayout2.visibility = View.GONE
                            binding.scanAnimation.pauseAnimation()
                            binding.pcDetail.pcName.text = deviceInfo.host
                            binding.pcDetail.root.visibility = View.VISIBLE
                            binding.waitingText.visibility = View.VISIBLE
                            //rotate star infinitely
                            val rotate =
                                ObjectAnimator.ofFloat(binding.pcDetail.star, View.ROTATION, 0f, 360f)
                            rotate.duration = 5000
                            rotate.repeatCount = ObjectAnimator.INFINITE
                            rotate.start()
                            CoroutineScope(Dispatchers.IO).launch {
                                connectToPc(deviceInfo)
                            }
                        }
                    }
                }
            }
        }

        nearbyManager.startDiscovery()

    }

    override fun onResume() {
        //nearbyManager.startDiscovery()
        super.onResume()
    }

    override fun onPause() {
        //nearbyManager.stopDiscovery()
        super.onPause()
    }

    private suspend fun connectToPc(deviceInfo: DeviceInfo) = withContext(Dispatchers.IO) {
        Log.d("SearchPcActivity", "Connecting to PC")
        val socketServer = SocketServer.getInstance()
        try {
            socketServer.startSocketConnection(
                ip = deviceInfo.ip,
                port = deviceInfo.port,
                onConnected = {
                    Log.d("SearchPcActivity", "Connected to PC")
                    val shared = getSharedPreferences("info", MODE_PRIVATE)
                    val token = shared.getString("token", "") ?: ""
                    val device_id = shared.getString("device_id", "") ?: ""
                    val connectionRequestModel = ConnectionRequestModel(
                        host = Settings.Global.getString(
                            contentResolver,
                            Settings.Global.DEVICE_NAME
                        ),
                        mac = device_id,
                        token = token,
                        type = "auth"
                    )
                    Log.d("SearchPcActivity", "Sending message: $connectionRequestModel")
                    socketServer.sendMessage(Gson().toJson(connectionRequestModel)) { error ->
                        Log.e("SearchPcActivity", "Error sending message: ${error.printStackTrace()}")
                    }
                },
                onMessageReceived = { message ->
                    Log.d("SearchPcActivity", "Message received: $message")
                },
                onError = { error ->
                    Log.e("SearchPcActivity", "Error connecting to PC: ${error.printStackTrace()}")
                }
            )
        } catch (e: Exception) {
            Log.e("SearchPcActivity", "Error connecting to PC: ${e.printStackTrace()}")
            return@withContext
        }

    }

}