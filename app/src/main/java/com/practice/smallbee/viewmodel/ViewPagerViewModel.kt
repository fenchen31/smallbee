package com.practice.smallbee.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.common.timer.TimerHandler
import com.practice.common.timer.TimerListener
import com.practice.smallbee.R
import com.practice.smallbee.response.ViewPagerResponse

class ViewPagerViewModel : ViewModel() {
    private val originData = MutableLiveData<ArrayList<ViewPagerResponse>>()
    var data:LiveData<ArrayList<ViewPagerResponse>> = originData

    fun loadData(){
        TimerHandler().start(2, 1, object :TimerListener{
            override fun onFinish() {
                originData.value = arrayListOf(
                    ViewPagerResponse(R.mipmap.thumb01),
                    ViewPagerResponse(R.mipmap.thumb02),
                    ViewPagerResponse(R.mipmap.thumb03),
                    ViewPagerResponse(R.mipmap.thumb04),
                    ViewPagerResponse(R.mipmap.thumb05),
                    ViewPagerResponse(R.mipmap.thumb06),
                    ViewPagerResponse(R.mipmap.thumb07)
                )
            }
        })
    }

    fun changeData(){
        val first = originData.value?.removeAt(0)
        originData.value?.add(first!!)
    }
}