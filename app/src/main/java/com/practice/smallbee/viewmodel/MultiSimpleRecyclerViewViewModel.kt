package com.practice.smallbee.viewmodel

import androidx.lifecycle.ViewModel
import com.practice.common.timer.TimerCountDown
import com.practice.common.timer.TimerListener
import com.practice.smallbee.adapter.EquityRecordAdapter
import com.practice.smallbee.adapter.GameRecordAdapter
import com.practice.smallbee.response.GemeEquityRecordBean
import com.practice.smallbee.response.GemeEquityRecordBean.RecordBean

class MultiSimpleRecyclerViewViewModel(private val adapter: GameRecordAdapter) : ViewModel() {


    private lateinit var data: GemeEquityRecordBean

    fun loadData() {
        TimerCountDown().start(1, 1, object : TimerListener {
            override fun onFinish() {
                data = generateData(true, 19)
                adapter.setData(data)
            }
        })
    }

    private fun generateData(game: Boolean, count: Int): GemeEquityRecordBean {
        val data = GemeEquityRecordBean()
        val list = ArrayList<RecordBean>()
        data.name = if (game) "游戏权益" else "流量权益"
        data.count = "一共有100个权益"
        data.recordList = list
        for (i in 0..count) {
            val item = GemeEquityRecordBean.RecordBean()
            item.name = "name$i"
            item.validityPeriodDuration = "+1$i 分钟"
            item.validityPeriodStart = "2022-05-01 09:00:00"
            item.validityPeriodEnd = "20$i-05-01 09:01:00"
            data.recordList.add(item)
        }
        return data
    }

    fun deleteData() {
        for (i in 0..9) {
            data.recordList.removeAt(data.recordList.lastIndex)
        }
        adapter.setData(data)
    }

    fun increaseData() {
        data.recordList.addAll(generateData(true, 9).recordList)
        adapter.setData(data)
    }
}