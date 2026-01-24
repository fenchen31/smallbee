package com.practice.common.util

import android.Manifest
import android.app.Application
import android.content.Context
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.util.Log

class MediaSessionUtil private constructor() {
    companion object {
        @JvmStatic
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { MediaSessionUtil() }
    }

    private val TAG = MediaSessionUtil::class.java.simpleName

    private val callbacks = mutableListOf<MediaCallback>()
    private val mediaInfo by lazy { MediaInfo("", "", "", 0L, 0L, false) }
    private lateinit var context: Context
    private var mediaController: MediaController? = null
    private lateinit var mediaSessionManager: MediaSessionManager
    private val sessionListener by lazy { MediaSessionManager.OnActiveSessionsChangedListener {
        updateMediaSession()
    } }

    private val mediaCallback = object :MediaController.Callback(){
        override fun onMetadataChanged(metadata: MediaMetadata?) {
            super.onMetadataChanged(metadata)
            updateMediaInfo(metadata, mediaInfo, mediaController?.playbackState)
        }

        override fun onPlaybackStateChanged(state: PlaybackState?) {
            super.onPlaybackStateChanged(state)
            updateMediaInfo(mediaController?.metadata, mediaInfo, state)
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
            updateMediaInfo(null, mediaInfo, null)
        }

    }

    fun init(context: Context) {
        this.context = context
        mediaSessionManager = context.getSystemService(MediaSessionManager::class.java)
        mediaSessionManager.addOnActiveSessionsChangedListener(sessionListener, null)
        val controllers = mediaSessionManager.getActiveSessions(null)
        if (controllers.isEmpty()) {
            updateMediaInfo(null, mediaInfo, null)
        } else {
            val controller = controllers[0]//只取第一个
            updateMediaInfo(
                controller.metadata, mediaInfo, controller.playbackState
            )
            registerNewController(controller)
        }

    }

    fun addCallback(callback: MediaCallback) {
        callbacks.add(callback)
    }

    private fun updateMediaSession() {
        if (context == null || mediaSessionManager == null){
            Log.d(TAG, "updateMediaSession: context :${context}, mediaSessionManager :${mediaSessionManager}")
            return
        }
        val controllers = mediaSessionManager.getActiveSessions(null)
        if (controllers.isEmpty()){
            mediaController?.unregisterCallback(mediaCallback)
            updateMediaInfo(null, mediaInfo, null)
        } else {
            registerNewController(controllers[0])
        }
    }

    private fun registerNewController(controller: MediaController?) {
        if (mediaController != controller){
            mediaController?.unregisterCallback(mediaCallback)
            mediaController = controller
            if (controller != null) {
                mediaController?.registerCallback(mediaCallback)
            }
        }
    }

    private fun updateMediaInfo(
        metadata: MediaMetadata?, mediaInfo: MediaInfo, playState: PlaybackState?
    ) {
        mediaInfo.title = metadata?.getString(MediaMetadata.METADATA_KEY_TITLE) ?: "未知"
        mediaInfo.artist = metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: "未知歌手"
        mediaInfo.album = metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM) ?: "未知专辑"
        mediaInfo.duration = metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION) ?: 0L
        mediaInfo.position = playState?.position ?: 0L
        mediaInfo.isPlaying = isPlaying(playState)
        Log.d("MediaInfoUtil", "updateMediaInfo: $mediaInfo")
        callbacks.forEach({ it.onMediaInfo(mediaInfo) })
    }

    private fun isPlaying(playStatus: PlaybackState?): Boolean {
        return if (playStatus == null) {
            false
        } else {
            Log.d(TAG, "isPlaying: ${playStatus.state}")
            when (playStatus.state) {
                PlaybackState.STATE_PLAYING -> true     // 播放中
                PlaybackState.STATE_FAST_FORWARDING -> true // 快进中
                PlaybackState.STATE_REWINDING -> true     // 快退中
                PlaybackState.STATE_BUFFERING -> true     // 缓冲中
                PlaybackState.STATE_CONNECTING -> true
                PlaybackState.STATE_SKIPPING_TO_PREVIOUS -> true
                PlaybackState.STATE_SKIPPING_TO_NEXT -> true
                PlaybackState.STATE_SKIPPING_TO_QUEUE_ITEM -> true
                PlaybackState.STATE_NONE -> false      // 无状态
                PlaybackState.STATE_STOPPED -> false     // 已停止
                PlaybackState.STATE_PAUSED -> false       // 已暂停
                PlaybackState.STATE_ERROR -> false       // 错误状态
                else -> false
            }
        }
    }

    fun permissions():Array<String>{
        return arrayOf(
            Manifest.permission.MEDIA_CONTENT_CONTROL,
            Manifest.permission.PACKAGE_USAGE_STATS,
            Manifest.permission.INTERNET
        )
    }

    interface MediaCallback {
        fun onMediaInfo(data: MediaInfo? = null)
    }

    data class MediaInfo(
        var title: String,//歌曲名
        var artist: String,//歌手
        var album: String,//专辑
        var duration: Long,//总时长
        var position: Long,//当前播放位置
        var isPlaying: Boolean = false//是否正在播放
    )
}