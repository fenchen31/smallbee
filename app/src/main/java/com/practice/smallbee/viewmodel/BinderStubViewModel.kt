package com.practice.smallbee.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BinderStubViewModel:ViewModel() {
    private var originData = MutableLiveData<String>()
    val data:LiveData<String> = originData
    fun loadData(){
        originData.value = "这里是数据发送方，已经成功发生数据了"
    }

    fun sendData(){

    }
}