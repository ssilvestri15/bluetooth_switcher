package com.silvered.bluetoothswitcher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.silvered.bluetoothswitcher.databinding.PcWithNameBinding

class DeviceListAdapter(
    val deviceList: List<DeviceInfo>,
    val onItemClicked: (deviceInfo: DeviceInfo) -> Unit = {}
): RecyclerView.Adapter<DeviceListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PcWithNameBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(deviceList[position], onItemClicked)
    }

    class ViewHolder(private val binding: PcWithNameBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(deviceInfo: DeviceInfo, onItemClicked: (deviceInfo: DeviceInfo) -> Unit = {}) {
            binding.layout.pcName.text = deviceInfo.host
            binding.layout.root.setOnClickListener {
                onItemClicked(deviceInfo)
            }
        }

    }

}
