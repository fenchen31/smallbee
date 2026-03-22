package com.practice.bluetooth.viewmodel

import android.bluetooth.BluetoothDevice
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.practice.bluetooth.utils.BlueToothUtil

class WriteDataVM : ViewModel() {
    val receiveData = ObservableField("")
    val sendData = ObservableField("")
    val device = ObservableField<BluetoothDevice>()
    val inputText = ObservableField("")

    fun init(){
        BlueToothUtil.getInstance().receiveDataCallback = object :BlueToothUtil.ReceiveDataCallback{
            override fun onReceiveData(errMsg: String?, data: String?) {
                val text = if (errMsg.isNullOrEmpty()) errMsg else data
                receiveData.set(receiveData.get() + "\n" + text)
            }
        }
    }
}