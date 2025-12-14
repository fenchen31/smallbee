package com.practice.smallbee.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.common.timer.TimerCountDown
import com.practice.common.timer.TimerListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewLocateVM:ViewModel() {
    val topData = MutableLiveData<ArrayList<String>>()
    val girlsData = MutableLiveData<ArrayList<String>>()
    fun getLocatePictures() {
        TimerCountDown().start(2, listener = object : TimerListener {
            override fun onFinish() {
                val data = ArrayList<String>().apply {
                    add("https://img2.baidu.com/it/u=744444518,1997903981&fm=253&app=138&f=JPEG?w=800&h=1422")
                    add("https://img0.baidu.com/it/u=3787603119,4151249175&fm=253&app=138&f=JPEG?w=800&h=1422")
                    add("https://img2.baidu.com/it/u=1962933088,1639897562&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=666")
                    add("https://img2.baidu.com/it/u=375232780,3338767622&fm=253&fmt=auto?w=687&h=1215")
                    add("https://k.sinaimg.cn/n/sinakd20250515s/120/w1080h1440/20250515/ac21-b72714922c3edb3871cc3f961a5b9c9c.jpg/w700d1q75cms.jpg?by=cms_fixed_width")
                }
                if (data != null && data.size > 0){
                    topData.value = data
                }
            }
        })
    }

    fun getGirlsData() {
        viewModelScope.launch {
            delay(2000)
            val data = ArrayList<String>().apply {
                add("https://img2.baidu.com/it/u=744444518,1997903981&fm=253&app=138&f=JPEG?w=800&h=1422")
                add("https://img0.baidu.com/it/u=3787603119,4151249175&fm=253&app=138&f=JPEG?w=800&h=1422")
                add("https://img2.baidu.com/it/u=1962933088,1639897562&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=666")
                add("https://img2.baidu.com/it/u=375232780,3338767622&fm=253&fmt=auto?w=687&h=1215")
                add("https://k.sinaimg.cn/n/sinakd20250515s/120/w1080h1440/20250515/ac21-b72714922c3edb3871cc3f961a5b9c9c.jpg/w700d1q75cms.jpg?by=cms_fixed_width")
            }
            if (data != null && data.size > 0){
                girlsData.value = data
            }
        }
    }
}