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

    private var callback: BaseCallback<BluetoothDevice>? = null
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
            callback?.onSuccess(devices)
        } else {
            callback?.onFailure()
        }
    }



    fun setDeviceMessageCallback(callback: BaseCallback<BluetoothDevice>) {
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