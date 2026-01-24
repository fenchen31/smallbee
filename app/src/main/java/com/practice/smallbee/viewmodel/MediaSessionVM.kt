package com.practice.smallbee.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.common.util.MediaSessionUtil

class MediaSessionVM:ViewModel() {

    val checkPermission = MutableLiveData<Boolean>()
    val mediaInfoPermission = MutableLiveData<Boolean>()
    val packageUsagePermission = MutableLiveData<Boolean>()
    val mediaInfo = MutableLiveData<MediaSessionUtil.MediaInfo?>()

    fun initMediaInfo(context: Context) {
        if (mediaInfoPermission.value == true && packageUsagePermission.value == true) {
            MediaSessionUtil.instance.init(context)
            MediaSessionUtil.instance.addCallback(object : MediaSessionUtil.MediaCallback {
                override fun onMediaInfo(data: MediaSessionUtil.MediaInfo?) {
                    mediaInfo.value = data
                }
            })
        }
    }
}