package com.practice.common.util.media

import android.app.Notification
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.practice.common.base.BaseCallback
import java.util.regex.Pattern

class MediaInfoService: NotificationListenerService() {

    private val packageName = arrayListOf(
        "com.netease.cloudmusic",
        "com.tencent.qqmusic",
        "com.xiaomi.smarthome",
    )
    private val callbacks by lazy { arrayListOf<BaseCallback<MediaResponse>>() }

    // 正则表达式匹配多语言媒体控制关键词
    private val MEDIA_CONTROL_PATTERN: Pattern = Pattern.compile(
        "(播放|暂停|上一首|下一首|Play|Pause|Previous|Next)"
    )

    private val mediaData by lazy { MediaResponse() }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (isMedia(sbn)){
            updateMediaInfo(sbn?.notification?.extras)
            callbacks.forEach { callback -> callback.onSuccess(mediaData) }
        } else {
            callbacks.forEach { callback -> callback.onSuccess(null) }
        }
    }

    private fun updateMediaInfo(extras: Bundle?) {
        mediaData.artist = extras?.getString(Notification.EXTRA_TITLE) ?: "未知歌手"
        mediaData.title = extras?.getString(Notification.EXTRA_TEXT) ?: "未知歌曲"
        mediaData.album = extras?.getString(Notification.EXTRA_SUB_TEXT) ?: "未知专辑"
    }

    private fun isMedia(sbn: StatusBarNotification?): Boolean{
        if (sbn == null){
            return false
        }
        if (packageName.contains(sbn.packageName)){
            return true
        }
        sbn.notification?.extras?.let {
            return it.containsKey(Notification.EXTRA_MEDIA_SESSION) ||
                    it.containsKey(Notification.EXTRA_TITLE)
        }
        return hasMediaControlButton(sbn.notification?.actions)
    }

    private fun hasMediaControlButton(action: Array<Notification.Action>?): Boolean{
        if (action.isNullOrEmpty()){
            return false
        } else {
            for (i in action.indices){
                val title = action[i].title
                // 判断是否是多语言媒体控制按钮
                if (title != null && MEDIA_CONTROL_PATTERN.matcher(title).matches()){
                    return true
                }
            }
            return false
        }
    }

    fun addCallback(callback: BaseCallback<MediaResponse>){
        callbacks.add(callback)
    }
}