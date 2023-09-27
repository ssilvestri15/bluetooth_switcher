package com.silvered.bluetoothswitcher.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.silvered.bluetoothswitcher.DeviceInfo
import com.silvered.bluetoothswitcher.NearbyManager
import com.silvered.bluetoothswitcher.R
import com.silvered.bluetoothswitcher.SocketServer
import com.silvered.bluetoothswitcher.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var socketServer: SocketServer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        socketServer = SocketServer.getInstance()


        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.pcRecyclerView.layoutManager = linearLayoutManager
        socketServer.connectedDeviceList.add(DeviceInfo(name = "Test", host = "Test", ip = "test", port=1977, selected = false))
        socketServer.connectedDeviceList.add(DeviceInfo(name = "Test", host = "Test1", ip = "test", port=1977, selected = false))
        socketServer.connectedDeviceList.add(DeviceInfo(name = "Test", host = "Test2", ip = "test", port=1977, selected = false))
        socketServer.connectedDeviceList.add(DeviceInfo(name = "Test", host = "Test3", ip = "test", port=1977, selected = false))
        socketServer.connectedDeviceList.add(DeviceInfo(name = "Test", host = "Test4", ip = "test", port=1977, selected = false))
        socketServer.connectedDeviceList.add(DeviceInfo(name = "Test", host = "Test5", ip = "test", port=1977, selected = false))
        socketServer.connectedDeviceList.add(DeviceInfo(name = "Test", host = "Test6", ip = "test", port=1977, selected = false))
        socketServer.connectedDeviceList.add(DeviceInfo(name = "Test", host = "Test7", ip = "test", port=1977, selected = false))
        if (socketServer.connectedDeviceList.size > 0)  {
            socketServer.connectedDeviceList[0].selected = true
        }
        binding.pcRecyclerView.adapter = PcHomeListAdapter(socketServer.connectedDeviceList){ device ->
            if (device.selected) return@PcHomeListAdapter
            device.selected = true
            val index = socketServer.connectedDeviceList.indexOf(device)
            socketServer.connectedDeviceList.removeAt(index)
            socketServer.connectedDeviceList[0].selected = false
            socketServer.connectedDeviceList.add(0, device)
            binding.pcRecyclerView.adapter?.notifyItemMoved(index, 0)
            binding.pcRecyclerView.adapter?.notifyItemChanged(0)
            binding.pcRecyclerView.adapter?.notifyItemChanged(1)
            binding.pcRecyclerView.scrollToPosition(0)
        }
    }
}