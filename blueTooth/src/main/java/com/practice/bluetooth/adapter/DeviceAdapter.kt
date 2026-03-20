package com.practice.bluetooth.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothClass.Device.Major
import android.bluetooth.BluetoothDevice
import com.practice.blueTooth.R
import com.practice.blueTooth.databinding.ItemDeviceBinding
import com.practice.bluetooth.response.DeviceResponse
import com.practice.common.recyclerview.SingleAdapter

class DeviceAdapter(private val layoutId: Int) :
    SingleAdapter<ItemDeviceBinding, BluetoothDevice>(layoutId) {

    var clickItem: ((BluetoothDevice) -> Unit) ?= null
    @SuppressLint("MissingPermission")
    override fun bindingItem(
        binding: ItemDeviceBinding, position: Int, data: ArrayList<BluetoothDevice>
    ) {
        binding.data = data[position]
        binding.ivIcon.setImageResource(getType(data[position].bluetoothClass))
        binding.root.setOnClickListener {
            clickItem?.invoke(binding.data!!)
        }
    }

    private fun getType(bluetoothClass: BluetoothClass?): Int {
        return when(bluetoothClass?.deviceClass) {
            Major.PHONE -> R.drawable.ic_phone
            Major.COMPUTER -> R.drawable.ic_desktop
            else -> R.drawable.ic_laptop
        }
    }
}