package com.practice.bluetooth.adapter

import android.Manifest
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothClass.Device
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.practice.blueTooth.R
import com.practice.blueTooth.databinding.ItemDeviceBinding
import com.practice.bluetooth.response.DeviceResponse
import com.practice.common.recyclerview.SingleAdapter

class DeviceAdapter(private val layoutId: Int) :
    SingleAdapter<ItemDeviceBinding, DeviceResponse>(layoutId) {
    override fun bindingItem(
        binding: ItemDeviceBinding, position: Int, data: ArrayList<DeviceResponse>
    ) {
        binding.data = data[position]
    }

    private fun setIcon(clazz: BluetoothClass, view: ImageView) {
        val resourceId = when (clazz.deviceClass) {
            Device.COMPUTER_LAPTOP -> R.drawable.ic_laptop
            Device.COMPUTER_DESKTOP -> R.drawable.ic_desktop
            Device.PHONE_SMART -> R.drawable.ic_phone
            else -> R.drawable.ic_bluetooth_device
        }
        view.setImageResource(resourceId)
    }

    private fun hasPermission(): Boolean {
        val permission: String
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permission = Manifest.permission.BLUETOOTH_CONNECT
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            permission = Manifest.permission.BLUETOOTH_ADMIN
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN)
        }
        if (result != PackageManager.PERMISSION_GRANTED) {
            Log.d("DeviceAdapter", "checkSelfPermissio,没有$permission 权限")
            return false
        }
        return true
    }
}