package com.practice.bluetooth.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import com.practice.bluetooth.response.DeviceResponse
import com.practice.common.base.BaseCallback
import java.util.UUID

class BlueToothUtil private constructor(){

    private lateinit var blueToothAdapter: BluetoothAdapter
    private lateinit var blueToothReceiver: BlueToothReceiver
    private var connectDeviceThread:ConnectDeviceThread ?= null
    companion object{
        @Volatile
        private var instance: BlueToothUtil? = null
        @JvmStatic
        val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        @JvmStatic
        fun getInstance(): BlueToothUtil{
            return instance ?: synchronized(this){
                instance ?: BlueToothUtil().also { instance = it }
            }
        }
    }

    fun register(context: Context, fun1:((BluetoothDevice) -> Unit) ?= null, fun2:((BlueToothReceiver.DiscoverState) -> Unit) ?= null,) {
        blueToothAdapter = context.getSystemService(BluetoothManager::class.java).adapter
        blueToothReceiver = BlueToothReceiver()
        val filter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            addAction(BluetoothDevice.ACTION_FOUND)//找到设备
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)//配对状态改变
        }
        blueToothReceiver.setDeviceMessageCallback(object : BaseCallback<BluetoothDevice> {
            override fun onSuccess(data: BluetoothDevice?) {
                data?.let {
                    fun1?.invoke(data)
                }
            }
        })
        blueToothReceiver.discoverStateCallback(object : BlueToothReceiver.DiscoverCallback {
            override fun onDiscoverState(state: BlueToothReceiver.DiscoverState) {
                fun2?.invoke(state)
            }
        })
        context.registerReceiver(blueToothReceiver, filter)
    }

    @SuppressLint("MissingPermission")
    fun startScan(){
        if (blueToothAdapter.isDiscovering) {
            blueToothAdapter.cancelDiscovery()
        }
        blueToothAdapter.startDiscovery()
    }

    @SuppressLint("MissingPermission")
    fun stopScan(){
        if (blueToothAdapter.isDiscovering) {
            blueToothAdapter.cancelDiscovery()
        }
    }

    fun connectDevice(device: BluetoothDevice, callback: DeviceCallback?){
        callback?.connectStatus(ConnectState.CONNECTING)
        synchronized(this) {
            connectDeviceThread?.cancel()
            connectDeviceThread = null
            connectDeviceThread = ConnectDeviceThread(device, callback)
            cancelDiscover()
            connectDeviceThread?.start()
        }
    }

    fun disConnectDevice(device: BluetoothDevice){
       // device.cancelBondProcess()
    }

    @SuppressLint("MissingPermission")
    private fun cancelDiscover(){
        if (blueToothAdapter.isDiscovering) {
            blueToothAdapter.cancelDiscovery()
        }
    }

    fun unRegister(context: Context){
        context.unregisterReceiver(blueToothReceiver)
    }

    interface DeviceCallback{
        fun connectStatus(state: ConnectState, errMsg: String?= null)
        fun onReceiveData(data: ByteArray)
        fun onRssiUpdate(rssi: Int)
    }

    enum class ConnectState {
        CONNECTING,
        CONNECT_SUCCESS,
        CONNECT_FAIL,
        DISCONNECTING,
        DISCONNECT_SUCCESS,
        DISCONNECT_FAIL
    }

    enum class Step {
        CHECK_PERMISSION,//检查权限
        OPEN_BLUETOOTH_LOCATION,//打开蓝牙和定位
        START_SCAN,//开始扫描
        STOP_SCAN,//停止扫描
        CONNECT_DEVICE,//连接设备
        DISCONNECT_DEVICE,//断开连接
    }
}