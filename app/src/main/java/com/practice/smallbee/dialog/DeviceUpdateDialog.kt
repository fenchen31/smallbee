package com.practice.smallbee.dialog

import com.practice.common.base.BaseDialog
import com.practice.smallbee.R
import com.practice.smallbee.databinding.DialogDeviceUpdateBinding

class DeviceUpdateDialog :
    BaseDialog<DialogDeviceUpdateBinding>(layoutId = R.layout.dialog_device_update) {

    override fun initView() {

    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        binding.root.post {
            binding.tvText.text = "高：${binding.root.height}\n宽：${binding.root.width}"
        }
    }

}