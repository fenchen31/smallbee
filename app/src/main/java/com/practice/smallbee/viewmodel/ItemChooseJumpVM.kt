package com.practice.smallbee.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.core.ARouter

class ItemChooseJumpVM:ViewModel() {

    val text = MutableLiveData("")
    val jumpData = MutableLiveData("")

    fun clickItem(context: Context) {
        ARouter.getInstance().jumpActivity(context, jumpData.value!!)
    }
}