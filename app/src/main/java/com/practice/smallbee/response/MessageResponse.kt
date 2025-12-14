package com.practice.smallbee.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(val total: Int, val list: List<MessageResponseItem>)
data class MessageResponseItem(
    //val hasMore: Boolean,
    val total: Int,//总条数
    val id: Int,//消息id
    //   val result: String,//结果：如设备连接成功，设备连接失败
    val type: Int,// 1-设备告警，2-连接成功，3-版本更新，4-删除设备，5-新品上架',
    @SerializedName("msgName")
    val appName: String,//app名称：您的设备
    @SerializedName("version")
    val appVersion: String,//deviceName
    @SerializedName("createTime")
    val installTime: String,//于time
    @SerializedName("receiveTime")
    val messageTime: Long,//消息时间：如3分钟前，2025-07-10 15:03
    var status: Int,//状态 0-未读，1-已读
    //var noRead: Boolean,//是否未读
    val next: String,//暂时无
    val link: String,//暂时无
    var select: Boolean = false,
    val content: String//详情
)