package com.practice.smallbee.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.smallbee.response.CoordinatorResponse

class BindingRecyclerviewViewmodel: ViewModel() {
    private var originData= MutableLiveData(ArrayList<CoordinatorResponse>())
    val data:LiveData<ArrayList<CoordinatorResponse>> = originData
    private var number = 1

    fun loadData(){
        originData.value = ArrayList(List(100){CoordinatorResponse(it + 1, "显示文字" + (it + 1))})
    }
    fun changeData(){
        originData.value = ArrayList(List(100){CoordinatorResponse(it + number, "显示文字" + (it + number))})
        number++
    }
}