package com.practice.bluetooth.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothClass.Device.Major
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.practice.blueTooth.R
import com.practice.bluetooth.response.DeviceResponse
import com.practice.common.base.BaseCallback

class BlueToothReceiver : BroadcastReceiver() {

    private var callback: BaseCallback<DeviceResponse>? = null
    private var discoverCallback: DiscoverCallback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            BluetoothDevice.ACTION_FOUND -> foundDevice(intent)
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> discoverCallback?.onDiscoverState(DiscoverState.FINISH)
            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> discoverCallback?.onDiscoverState(DiscoverState.START)
        }
    }

    @SuppressLint("MissingPermission")
    private fun foundDevice(intent: Intent) {
        val devices = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
        } else {
            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        }
        if (devices != null) {
            callback?.onSuccess(DeviceResponse(devices.address ?: "未知设备", getType(devices.bluetoothClass)))
        } else {
            callback?.onFailure()
        }
    }

    private fun getType(bluetoothClass: BluetoothClass?): Int {
        return when(bluetoothClass?.deviceClass) {
            Major.PHONE -> R.drawable.ic_phone
            Major.COMPUTER -> R.drawable.ic_desktop
            else -> R.drawable.ic_laptop
        }
    }

    fun setDeviceMessageCallback(callback: BaseCallback<DeviceResponse>) {
        this.callback = callback
    }

    fun discoverStateCallback(callback: DiscoverCallback){
        discoverCallback = callback
    }

    interface DiscoverCallback {
        fun onDiscoverState(state: DiscoverState)
    }

    enum class DiscoverState {
        START,
        FINISH,
    }
}