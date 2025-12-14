package com.practice.smallbee.viewmodel

import androidx.lifecycle.ViewModel
import com.practice.smallbee.adapter.EquityRecordAdapter
import com.practice.smallbee.response.GemeEquityRecordBean
import com.practice.smallbee.response.GemeEquityRecordBean.RecordBean

class MultiRecyclerViewViewModel(private val adapter: EquityRecordAdapter):ViewModel() {


    private lateinit var gameData: GemeEquityRecordBean
    private lateinit var trafficData: GemeEquityRecordBean

    fun loadData() {
        gameData = generateData(true, 19)
        trafficData = generateData(false, 19)
        adapter.setGamedata(gameData)
        adapter.setTrafficData(trafficData)
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

    fun deleteGameData(){
        for (i in 0..9) {
            gameData.recordList.removeAt(gameData.recordList.lastIndex)
        }
        adapter.setGamedata(gameData)
    }

    fun deleteAlldata() {
        adapter.setGamedata(null)
        adapter.setTrafficData(null)
    }

    fun gameIncrease(){
        gameData.recordList.addAll(generateData(true, 9).recordList)
        adapter.setGamedata(gameData)
    }

    fun trafficIncrease(){
        trafficData.recordList.addAll(generateData(false, 9).recordList)
        adapter.setTrafficData(trafficData)
    }

    fun trafficDelete() {
        repeat(10){trafficData.recordList.removeAt(trafficData.recordList.lastIndex)}
        adapter.setGamedata(trafficData)
    }
}