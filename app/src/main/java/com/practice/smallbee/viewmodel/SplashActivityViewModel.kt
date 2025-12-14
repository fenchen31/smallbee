package com.practice.smallbee.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.common.service.ApiService
import com.practice.common.timer.TimerCountDown
import com.practice.common.timer.TimerListener
import com.practice.smallbee.response.SplashAdvertiseResponse

class SplashActivityViewModel:ViewModel() {
    private val serviceProvider = MutableLiveData<String>()
    private val originData = MutableLiveData<SplashAdvertiseResponse>()
    var data:LiveData<SplashAdvertiseResponse> = originData
    fun loadData(){
        TimerCountDown().start(0L, 1, object :TimerListener{
            override fun onFinish() {
                originData.value = SplashAdvertiseResponse("火山引擎")
            }
        })
    }

    fun loadAdvertise(){

    }
}