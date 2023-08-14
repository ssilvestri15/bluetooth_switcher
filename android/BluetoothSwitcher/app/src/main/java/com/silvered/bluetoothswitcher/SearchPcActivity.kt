package com.silvered.bluetoothswitcher

import android.Manifest
import android.animation.ObjectAnimator
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
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
                runOnUiThread { binding.devicesFoundRv.visibility = View.GONE }
            } else {
                runOnUiThread { binding.devicesFoundRv.visibility = View.VISIBLE }
                binding.devicesFoundRv.adapter = DeviceListAdapter(deviceList) { deviceInfo ->
                    nearbyManager.stopDiscovery()
                    runOnUiThread {
                        binding.linearLayout2.visibility = View.GONE
                        binding.scanAnimation.pauseAnimation()
                        binding.pcDetail.pcName.text = deviceInfo.host
                        binding.pcDetail.root.visibility = View.VISIBLE
                        binding.waitingText.visibility = View.VISIBLE
                        //rotate star infinitely
                        val rotate = ObjectAnimator.ofFloat(binding.pcDetail.star, View.ROTATION, 0f, 360f)
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

        nearbyManager.startDiscovery()

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    private suspend fun connectToPc(deviceInfo: DeviceInfo) = withContext(Dispatchers.IO) {
        val socketServer = SocketServer.getInstance()
        socketServer.startSocketConnection(
            ip = deviceInfo.ip,
            port = deviceInfo.port,
            onConnected = {
                Log.d("SearchPcActivity", "Connected to PC")
                socketServer.sendMessage("Hello there!")
            },
            onDisconnected = {
                Log.d("SearchPcActivity", "onDisconnected")
            },
            onMessageReceived = { message ->
                Log.d("SearchPcActivity", "Message received: $message")
            }
        )
    }

}