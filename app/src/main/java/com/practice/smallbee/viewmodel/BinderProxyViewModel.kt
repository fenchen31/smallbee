package com.practice.smallbee.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BinderProxyViewModel : ViewModel() {
    private var originData = MutableLiveData<String>()
    val data: LiveData<String> = originData

    fun defaultData(data: String) {
        originData.value = data
    }

    fun loadData() {
        originData.value = "这里是数据发送方，已经成功发生数据了"
    }

    fun sendData() {

    }
}