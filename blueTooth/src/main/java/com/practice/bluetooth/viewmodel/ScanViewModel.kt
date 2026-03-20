package com.practice.bluetooth.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScanViewModel : ViewModel() {
    val showLoading = ObservableField(false)
    val checkPermission = MutableLiveData((false))
    var startScan = MutableLiveData(false)
    val openBlueToothAndLocation = MutableLiveData(false)

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
                startScan.value = false
                showLoading.set(false)
            }
        }
    }

    private fun startScan(){
        openBlueToothAndLocation.value = false
        startScan.value = true
        showLoading.set(true)

    }

    enum class Step {
        CHECK_PERMISSION,//检查权限
        OPEN_BLUETOOTH_LOCATION,//打开蓝牙和定位
        START_SCAN,//开始扫描
        STOP_SCAN,//停止扫描
    }
}