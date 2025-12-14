package com.practice.smallbee.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.smallbee.response.MessageResponse
import com.practice.smallbee.response.MessageResponseItem

class MessageCenterVM : ViewModel() {
    private val originData = MutableLiveData<MessageResponse>()
    val data: LiveData<MessageResponse> = originData
    var loadStatus:MutableLiveData<Boolean> = MutableLiveData(false)
    var loadFinished:MutableLiveData<Boolean> = MutableLiveData(false)

    private var time = 0

    fun loadData(pageNum: Int) {
        generateData(pageNum, pageSize = 10)
        loadFinished.value = false
    }

    private fun generateData(pageNum: Int, pageSize: Int){
        val list = ArrayList<MessageResponseItem>()
        object :CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                if (pageNum % 2 == 0){
                    loadStatus.value = true
                    loadFinished.value = true
                    return
                }
                for (i in pageNum until pageNum + pageSize) {
                    list.add(
                        MessageResponseItem(
                            total = 0,
                            id = i * 10,
                            type = 0,
                            appName = "名称" + i,
                            appVersion = "appVersion" + i,
                            installTime = "束带结发克里斯多夫",
                            messageTime = System.currentTimeMillis(),
                            status = 0,
                            next = "next",
                            select = false,
                            link = "link",
                            content = "啊圣诞快乐拒收到付i我饿凯撒路打法就撒大萨达开发设计第佛七我饿叫哦发手机打卡了受打击"
                        )
                    )
                }
                val response = originData.value?.list
                if (pageNum == 1 || response == null) {
                    originData.value = MessageResponse(100, list)
                } else {
                    val newList = response.toMutableList()
                    newList.addAll(list)
                    originData.value = MessageResponse(100, newList)
                }
                loadFinished.value = true
            }
        }.start()
    }

    fun changeData(selected: Boolean){
        originData.value?.let {it->
            val newData = it.list.map {item ->
                item.copy(select = selected)
            }
            originData.value = it.copy(list = newData)
        }
    }
}