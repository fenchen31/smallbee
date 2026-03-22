package com.practice.bluetooth.event

import com.practice.bluetooth.utils.BlueToothUtil

class WriteDataEvent {

    fun sendData(data:String){
        BlueToothUtil.getInstance().sendData(data)
    }
}