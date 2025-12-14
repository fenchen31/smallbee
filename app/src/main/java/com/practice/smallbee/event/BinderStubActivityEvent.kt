package com.practice.smallbee.event

import com.practice.smallbee.viewmodel.BinderStubViewModel

class BinderStubActivityEvent constructor(private val viewModel: BinderStubViewModel) {

    fun sendData(){
        viewModel.sendData()
    }
}