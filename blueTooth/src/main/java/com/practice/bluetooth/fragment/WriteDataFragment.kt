package com.practice.bluetooth.fragment

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.practice.blueTooth.R
import com.practice.blueTooth.databinding.FragmentWriteDataBinding
import com.practice.bluetooth.event.WriteDataEvent
import com.practice.bluetooth.viewmodel.WriteDataVM
import com.practice.common.base.BaseFragment

class WriteDataFragment : BaseFragment<FragmentWriteDataBinding>(R.layout.fragment_write_data) {

    private val viewModel : WriteDataVM by viewModels()
    private val event by lazy { WriteDataEvent() }
    override fun initBinding() {
        binding.event = event
        binding.vm = viewModel
    }

    override fun initView(arguments: HashMap<String, Any>?) {
        viewModel.init()
        arguments?.let { viewModel.device.set(it[DATA] as BluetoothDevice) }
    }

}