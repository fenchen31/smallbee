package com.practice.bluetooth.viewmodel

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.bluetooth.response.DeviceResponse
import com.practice.bluetooth.utils.BlueToothReceiver
import com.practice.common.base.BaseCallback

class ScanViewModel : ViewModel() {
    val showLoading = ObservableField(false)
    val checkPermission = MutableLiveData((false))
    val openBlueToothAndLocation = MutableLiveData(false)
    private lateinit var blueToothAdapter: BluetoothAdapter
    val deviceResponse = MutableLiveData<ArrayList<DeviceResponse>>()
    private lateinit var blueToothReceiver: BlueToothReceiver

    fun init(context: Context) {
        blueToothAdapter = context.getSystemService(BluetoothManager::class.java).adapter
        blueToothReceiver = BlueToothReceiver()
        val filter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            addAction(BluetoothDevice.ACTION_FOUND)//找到设备
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)//配对状态改变
        }
        context.registerReceiver(blueToothReceiver, filter)
        blueToothReceiver.setDeviceMessageCallback(object : BaseCallback<DeviceResponse> {
            override fun onSuccess(data: DeviceResponse?) {
                data?.let {
                    val currentList = deviceResponse.value ?: arrayListOf()
                    if (currentList.none { item ->
                            item.name == it.name && item.resourceId == it.resourceId
                        }) {
                        currentList.add(it)
                        deviceResponse.value = currentList
                    }
                }
            }
        })
        blueToothReceiver.discoverStateCallback(object : BlueToothReceiver.DiscoverCallback {
            override fun onDiscoverState(state: BlueToothReceiver.DiscoverState) {
                when (state) {
                    BlueToothReceiver.DiscoverState.FINISH -> showLoading.set(false)
                    else -> {}
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun doNext(step: Step) {
        when (step) {
            Step.CHECK_PERMISSION -> //赋予权限后会在fragment中自动进入OPEN_BLUETOOTH_LOCATION
                checkPermission.value = true

            Step.OPEN_BLUETOOTH_LOCATION -> { //开关打开后自动进入START_SCAN
                openBlueToothAndLocation.value = true
                checkPermission.value = false
            }

            Step.START_SCAN -> startScan()
            Step.STOP_SCAN -> {
                blueToothAdapter.cancelDiscovery()
                showLoading.set(false)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startScan() {
        openBlueToothAndLocation.value = false
        showLoading.set(true)
        if (blueToothAdapter.isDiscovering) {
            blueToothAdapter.cancelDiscovery()
        }
        blueToothAdapter.startDiscovery()
    }

    fun destroy(context: Context) {
        context.unregisterReceiver(blueToothReceiver)
    }

    enum class Step {
        CHECK_PERMISSION,//检查权限
        OPEN_BLUETOOTH_LOCATION,//打开蓝牙和定位
        START_SCAN,//开始扫描
        STOP_SCAN,//停止扫描
    }
}