package com.practice.bluetooth.adapter

import com.practice.blueTooth.databinding.ItemDeviceBinding
import com.practice.bluetooth.response.DeviceResponse
import com.practice.common.recyclerview.SingleAdapter

class DeviceAdapter(private val layoutId: Int) :
    SingleAdapter<ItemDeviceBinding, DeviceResponse>(layoutId) {
    override fun bindingItem(
        binding: ItemDeviceBinding, position: Int, data: ArrayList<DeviceResponse>
    ) {
        binding.data = data[position]
        binding.ivIcon.setImageResource(data[position].resourceId)
    }
}