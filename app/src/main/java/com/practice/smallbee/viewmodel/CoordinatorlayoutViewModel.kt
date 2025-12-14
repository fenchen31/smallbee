package com.practice.smallbee.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.smallbee.response.CoordinatorResponse

class CoordinatorlayoutViewModel: ViewModel() {
    private var data = MutableLiveData(0)
    private var recyclerviewData = MutableLiveData<ArrayList<CoordinatorResponse>>()
    val number:LiveData<Int> = data
    val recyclerData: LiveData<ArrayList<CoordinatorResponse>> = recyclerviewData

    fun loadRecyclerViewData(){
        recyclerviewData.value = ArrayList(List(100){CoordinatorResponse(it,"数据$it")})
    }
    fun increase(){
        data.value = data.value!! + 1
    }

    fun reduce(){
        data.value = data.value!! - 1
    }
}