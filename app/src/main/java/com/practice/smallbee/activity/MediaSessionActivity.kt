package com.practice.smallbee.activity

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.practice.annotation.Route
import com.practice.common.util.MediaSessionUtil
import com.practice.smallbee.R
import com.practice.smallbee.databinding.ActivityMediaSessionBinding
import com.practice.smallbee.event.MediaSessionEvent
import com.practice.smallbee.viewmodel.MediaSessionVM

@Route("app/MediaSessionActivity")
class MediaSessionActivity : AppCompatActivity() {

    private val viewmodel by lazy { ViewModelProvider(this)[MediaSessionVM::class.java] }
    private val binding by lazy { ActivityMediaSessionBinding.inflate(layoutInflater) }
    private val event by lazy { MediaSessionEvent() }
    private lateinit var mediaContentLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var packageUseLauncher: ActivityResultLauncher<Intent>
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding.vm = viewmodel
        binding.event = event
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mediaContentLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
           viewmodel.mediaInfoPermission.value = it.all { entry ->  entry.value}
        }
        packageUseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            viewmodel.packageUsagePermission.value = it.resultCode == RESULT_OK
        }
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            viewmodel.mediaInfoPermission.value = true
        }
        initObserver()
    }


    fun initObserver(){
        viewmodel.checkPermission.observe(this,{
            if (!hasPackageUsageStatsPermission()){
                viewmodel.packageUsagePermission.value = true
            } else {
                requestPackageUsageStatsPermission()
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                    data = Uri.parse("package:$packageName")
                }
                launcher.launch(intent)
            } else {
                // API < 21 不需要此权限
                viewmodel.packageUsagePermission.value = true
            }
            //mediaContentLauncher.launch(MediaSessionUtil.instance.permissions())
        })
        viewmodel.mediaInfoPermission.observe(this,{
            viewmodel.initMediaInfo(this)
        })
        viewmodel.packageUsagePermission.observe(this,{
            viewmodel.initMediaInfo(this)
        })
    }


    private fun requestMediaContentControlPermission() {
        // 对于普通应用，这个权限通常无法直接请求，需要用户手动在设置中开启
        // 尝试打开权限设置页面
        val intent = Intent(Settings.ACTION_REQUEST_MANAGE_MEDIA).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        }
        try {
            packageUseLauncher.launch(intent)
        } catch (e: Exception) {
            // 如果上述方式失败，尝试其他方式
            val fallbackIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
            }
            packageUseLauncher.launch(fallbackIntent)
        }
    }


    private fun hasPackageUsageStatsPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName)
            mode == AppOpsManager.MODE_ALLOWED
        } else {
            true
        }
    }
    private fun requestPackageUsageStatsPermission() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
            data = Uri.parse("package:$packageName")
        }
        packageUseLauncher.launch(intent)
    }
}