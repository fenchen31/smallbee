package com.practice.smallbee.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.smallbee.response.UserResponse

class DatabindingListVM : ViewModel() {

    private var viewmodelList = mutableListOf<DatabindingItemVM>()
    private val viewmodels = MutableLiveData<List<DatabindingItemVM>>()
    val itemViewModels:LiveData<List<DatabindingItemVM>> = viewmodels

    init {
        loadData()
    }

    private fun loadData() {
        val data = listOf(
            UserResponse(1000, true, "一号员工"),
            UserResponse(5200, false, "都可地库"),
            UserResponse(1581, false, "u额卡西莫多"),
        )
        viewmodelList.clear()
        data.forEach { user->
            viewmodelList.add(DatabindingItemVM(user))
        }
        viewmodels.value = viewmodelList.toList()
    }

    fun addUser(){
        val lastUser = viewmodelList.last().getCurrectUser()
        val user = UserResponse(lastUser.id + 1, !lastUser.online, lastUser.name + 1 )
        viewmodelList.add(DatabindingItemVM(user))
        viewmodels.value = viewmodelList.toList()
    }
}