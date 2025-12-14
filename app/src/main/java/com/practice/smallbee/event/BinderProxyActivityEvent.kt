package com.practice.smallbee.event

import com.practice.smallbee.viewmodel.BinderProxyViewModel

class BinderProxyActivityEvent constructor(private val viewModel: BinderProxyViewModel) {

    fun sendData() {
        viewModel.sendData()
    }
}