package com.practice.smallbee.activity

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.practice.annotation.Route
import com.practice.common.base.BaseActivity
import com.practice.common.util.MediaSessionUtil
import com.practice.smallbee.R
import com.practice.smallbee.databinding.ActivityMediaSessionBinding
import com.practice.smallbee.event.MediaSessionEvent
import com.practice.smallbee.viewmodel.MediaSessionVM

@Route("app/MediaSessionActivity")
class MediaSessionActivity : BaseActivity<ActivityMediaSessionBinding>(R.layout.activity_media_session) {

    private val viewmodel by lazy { ViewModelProvider(this)[MediaSessionVM::class.java] }
    private val event by lazy { MediaSessionEvent() }
    private lateinit var notifycationLauncher:ActivityResultLauncher<Intent>
    private val mediaService by lazy { MediaSessionUtil.instance }
    override fun initView() {
        binding.vm = viewmodel
        binding.event = event
        initPermissionLauncher()
        initObserver()
    }

    private fun initPermissionLauncher(){
        notifycationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK){
                mediaService.addCallback(object : MediaSessionUtil.MediaCallback{
                    override fun onMediaInfo(data: MediaSessionUtil.MediaInfo?) {
                        viewmodel.mediaInfo.value = data
                    }
                })
            } else {
                Toast.makeText(this, "请授权通知权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initObserver(){
        viewmodel.notifycationPermission.observe(this, {
            if (it){
                notifycationLauncher.launch(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
        })
    }

    override fun loadData() {

    }
}