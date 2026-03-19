package com.practice.smallbee.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.smallbee.response.ChooseJumpResponse

class MainViewModel:ViewModel() {
    val jumpData = MutableLiveData<ArrayList<ChooseJumpResponse>>()
    fun initData(){
        val data = mutableListOf<ChooseJumpResponse>()
        data.add(ChooseJumpResponse("设备更新","app/DeviceUpdateActivity"))
        data.add(ChooseJumpResponse("databinding","app/DatabindingActivity"))
        data.add(ChooseJumpResponse("CoordinatorLayout","app/CoordinatorLayoutActivity"))
        data.add(ChooseJumpResponse("BindingRecyclerview","app/BindingRecyclerviewActivity"))
        data.add(ChooseJumpResponse("BinderProxy","app/BinderProxyActivity"))
        data.add(ChooseJumpResponse("MediaSession","app/MediaSessionActivity"))
        data.add(ChooseJumpResponse("MessageCenter","app/MessageCenterActivity"))
        data.add(ChooseJumpResponse("MessageCenterActivity2","app/MessageCenterActivity2"))
        data.add(ChooseJumpResponse("MultiRecyclerViewActivity","app/MultiRecyclerViewActivity"))
        data.add(ChooseJumpResponse("TestActivity","app/TestActivity"))
        data.add(ChooseJumpResponse("ViewLocateActivity","app/ViewLocateActivity"))
        data.add(ChooseJumpResponse("ViewPagerActivity","app/ViewPagerActivity"))
        data.add(ChooseJumpResponse("MediaSessionActivity","app/MediaSessionActivity"))
        jumpData.value = data as ArrayList<ChooseJumpResponse>
    }
}