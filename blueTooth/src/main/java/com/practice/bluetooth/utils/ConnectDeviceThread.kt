package com.practice.bluetooth.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log

class ConnectDeviceThread constructor(
    private val device: BluetoothDevice,
    private val callback: BlueToothUtil.DeviceCallback?
) : Thread() {
    private val TAG = "ConnectThread"
    private lateinit var socket: BluetoothSocket
    @SuppressLint("MissingPermission")
    override fun run() {
        Log.d(TAG, "run:开始连接蓝牙->${device.address}")
        socket = try {
            callback?.connectStatus(BlueToothUtil.ConnectState.CONNECTING)
            device.createRfcommSocketToServiceRecord(BlueToothUtil.MY_UUID)
        } catch (e: Exception) {
            callback?.connectStatus(BlueToothUtil.ConnectState.CONNECT_FAIL, "初始化socket失败")
            Log.e(TAG, "run:初始化socket失败")
            return
        }
        try {
            socket.connect()
            Log.d(TAG, "run:socket连接成功")
            callback?.connectStatus(BlueToothUtil.ConnectState.CONNECT_SUCCESS)
        } catch (e: Exception) {
            callback?.connectStatus(BlueToothUtil.ConnectState.CONNECT_FAIL, "socket连接失败")
            Log.e(TAG, "run:socket连接失败")
            return
        }
    }

    fun cancel() {
        try {
            callback?.connectStatus(BlueToothUtil.ConnectState.DISCONNECTING)
            socket.close()
            callback?.connectStatus(BlueToothUtil.ConnectState.DISCONNECT_SUCCESS)
        } catch (e: Exception) {
            callback?.connectStatus(BlueToothUtil.ConnectState.DISCONNECT_FAIL, "取消连接失败")
            Log.e(TAG, "cancel:取消连接失败")
        }
    }
}