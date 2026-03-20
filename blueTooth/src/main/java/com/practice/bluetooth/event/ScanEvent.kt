package com.practice.bluetooth.event

import com.practice.bluetooth.fragment.ScanFragment
import com.practice.bluetooth.viewmodel.ScanViewModel

class ScanEvent {

    fun startScan(viewModel: ScanViewModel){
        viewModel.doNext(ScanViewModel.Step.CHECK_PERMISSION)//每次扫描都应该先走一遍权限检查
    }

    fun stopScan(viewModel: ScanViewModel){
        viewModel.doNext(ScanViewModel.Step.STOP_SCAN)
    }
}