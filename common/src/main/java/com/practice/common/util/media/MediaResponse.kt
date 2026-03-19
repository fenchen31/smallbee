package com.practice.common.util.media

data class MediaResponse(
    var title: String = "未知歌曲",//歌曲名
    var artist: String = "未知歌手",//歌手
    var album: String = "未知专辑",//专辑
    var duration: Long = 0,//总时长
    var position: Long = 0,//当前播放位置
    var isPlaying: Boolean = false//是否正在播放
)