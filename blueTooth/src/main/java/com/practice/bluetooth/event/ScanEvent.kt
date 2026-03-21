package com.practice.bluetooth.event

import com.practice.bluetooth.fragment.ScanFragment
import com.practice.bluetooth.utils.BlueToothUtil
import com.practice.bluetooth.viewmodel.ScanViewModel

class ScanEvent {
    private var data = 1

    fun startScan(viewModel: ScanViewModel){
        viewModel.doNext(BlueToothUtil.Step.CHECK_PERMISSION)//每次扫描都应该先走一遍权限检查
    }

    fun stopScan(viewModel: ScanViewModel){
        viewModel.doNext(BlueToothUtil.Step.STOP_SCAN)
    }
}