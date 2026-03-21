package com.practice.bluetooth.viewmodel

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.bluetooth.utils.BlueToothReceiver
import com.practice.bluetooth.utils.BlueToothUtil

class ScanViewModel : ViewModel() {
    val showLoading = ObservableField(false)
    val checkPermission = MutableLiveData((false))
    val openBlueToothAndLocation = MutableLiveData(false)
    val deviceResponse = MutableLiveData<ArrayList<BluetoothDevice>>()
    val connectState = ObservableField("初始状态")
    private val TAG = "ScanViewModel"

    @SuppressLint("MissingPermission")
    fun init(context: Context) {
        BlueToothUtil.getInstance().register(context, { device ->
            val currentList = deviceResponse.value ?: arrayListOf()
            if (currentList.none { item ->
                    item.address == device.address
                }) {
                currentList.add(device)
                deviceResponse.value = currentList
            }
        }, { deviceState ->
            when (deviceState) {
                BlueToothReceiver.DiscoverState.FINISH -> showLoading.set(false)
                else -> {}
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun doNext(step: BlueToothUtil.Step, device: BluetoothDevice? = null) {
        when (step) {
            BlueToothUtil.Step.CHECK_PERMISSION -> //赋予权限后会在fragment中自动进入OPEN_BLUETOOTH_LOCATION
                checkPermission.value = true

            BlueToothUtil.Step.OPEN_BLUETOOTH_LOCATION -> { //开关打开后自动进入START_SCAN
                openBlueToothAndLocation.value = true
                checkPermission.value = false
            }

            BlueToothUtil.Step.START_SCAN -> {
                openBlueToothAndLocation.value = false
                showLoading.set(true)
                deviceResponse.value?.clear()
                BlueToothUtil.getInstance().startScan()
            }

            BlueToothUtil.Step.STOP_SCAN -> {
                BlueToothUtil.getInstance().stopScan()
                showLoading.set(false)
            }

            BlueToothUtil.Step.CONNECT_DEVICE -> connectDevice(device!!)
            BlueToothUtil.Step.DISCONNECT_DEVICE -> BlueToothUtil.getInstance().disConnectDevice()
        }
    }

    private fun connectDevice(device: BluetoothDevice) {
        BlueToothUtil.getInstance().connectStateCallback =
            object : BlueToothUtil.ConnectStateCallback {
                override fun connectStatus(state: BlueToothUtil.ConnectState, errMsg: String?) {
                    when (state) {
                        BlueToothUtil.ConnectState.CONNECT_SUCCESS -> {}
                        else -> connectState.set(state.toString())
                    }
                }
            }
        BlueToothUtil.getInstance().receiveDataCallback =
            object : BlueToothUtil.ReceiveDataCallback {
                override fun onReceiveData(errMsg: String?, data: String?) {
                    Log.d(TAG, "data: $data")
                }
            }
        BlueToothUtil.getInstance().connectDeviceAfterSetCallback(device)
    }

    fun destroy(context: Context) {
        BlueToothUtil.getInstance().unRegister(context)
    }
}