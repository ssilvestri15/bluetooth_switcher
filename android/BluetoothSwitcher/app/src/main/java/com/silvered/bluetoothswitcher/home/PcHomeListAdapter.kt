package com.silvered.bluetoothswitcher.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.silvered.bluetoothswitcher.DeviceInfo
import com.silvered.bluetoothswitcher.databinding.Pcdetailselected2Binding

class PcHomeListAdapter(
    val deviceList: List<DeviceInfo>,
    val onItemClicked: (deviceInfo: DeviceInfo) -> Unit = {}
): RecyclerView.Adapter<PcHomeListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(Pcdetailselected2Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(deviceList[position], onItemClicked)
    }

    class ViewHolder(private val binding: Pcdetailselected2Binding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(deviceInfo: DeviceInfo, onItemClicked: (deviceInfo: DeviceInfo) -> Unit = {}) {
            if (deviceInfo.selected) {
                binding.star.visibility = ViewGroup.VISIBLE
            } else {
                binding.star.visibility = ViewGroup.INVISIBLE
            }
            binding.pcName.text = deviceInfo.host
            binding.root.setOnClickListener {
                onItemClicked(deviceInfo)
            }
        }

    }

}
