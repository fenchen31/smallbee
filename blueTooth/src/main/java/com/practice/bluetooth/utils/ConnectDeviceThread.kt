package com.practice.bluetooth.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.databinding.ObservableField

class ConnectDeviceThread constructor(
    open val device: BluetoothDevice,
    private val connectCallback: BlueToothUtil.ConnectStateCallback?,
    private val receiveDataCallback: BlueToothUtil.ReceiveDataCallback?,
    private val connectedDevice: MutableList<BluetoothDevice>
) : Thread() {

    var operationType = OperationType.CONNECT_DEVICE
    private val TAG = "ConnectThread"
    private lateinit var socket: BluetoothSocket
    var dataToBeSent = ObservableField("")
        set(value) {
            field = value
            operationType = OperationType.SEND_DATA
            start()
        }
    override fun run() {
        when(operationType){
            OperationType.CONNECT_DEVICE -> connectDevice()
            OperationType.SEND_DATA -> dataToBeSent.get()?.let { WriteThread.getInstance().data.set( it) }
            OperationType.DISCONNECT_DEVICE -> cancel()
        }
    }

    @SuppressLint("MissingPermission")
    private fun connectDevice() {
        Log.d(TAG, "run:开始连接蓝牙->${device.address}")
        socket = try {
            connectCallback?.connectStatus(BlueToothUtil.ConnectState.CONNECTING)
            device.createRfcommSocketToServiceRecord(BlueToothUtil.MY_UUID)
        } catch (e: Exception) {
            connectCallback?.connectStatus(BlueToothUtil.ConnectState.CONNECT_FAIL, "初始化socket失败")
            Log.e(TAG, "run:初始化socket失败$e")
            return
        }
        try {
            socket.connect()
            Log.d(TAG, "run:socket连接成功")
            connectCallback?.connectStatus(BlueToothUtil.ConnectState.CONNECT_SUCCESS)
            connectedDevice.add(device)
            WriteThread.getInstance().updateSocket(socket)
            ReadThread.getInstance().updateSocket(socket, receiveDataCallback)
        } catch (e: Exception) {
            connectCallback?.connectStatus(BlueToothUtil.ConnectState.CONNECT_FAIL, "socket连接失败")
            Log.e(TAG, "run:socket连接失败$e")
            return
        }
    }

    private fun cancel() {
        try {
            connectCallback?.connectStatus(BlueToothUtil.ConnectState.DISCONNECTING)
            socket.close()
            connectCallback?.connectStatus(BlueToothUtil.ConnectState.DISCONNECT_SUCCESS)
            connectedDevice.remove(device)
            Log.d(TAG, "cancel:取消连接成功")
        } catch (e: Exception) {
            connectCallback?.connectStatus(BlueToothUtil.ConnectState.DISCONNECT_FAIL, "取消连接失败")
            Log.e(TAG, "cancel:取消连接失败$e")
        }
        operationType = OperationType.CONNECT_DEVICE
    }

    enum class OperationType{
        SEND_DATA,
        CONNECT_DEVICE,
        DISCONNECT_DEVICE,
    }
}