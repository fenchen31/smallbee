package com.test.smallbee.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.test.smallbee.app.R
import com.test.smallbee.app.databinding.ActivityH5Binding
import com.test.smallbee.util.Const
import com.test.smallbee.util.H5WebChromeClinet
import com.test.smallbee.util.H5WebViewClient
import java.io.Serializable

class H5Activity : AppCompatActivity() {
    lateinit var binding: ActivityH5Binding
    var data: Response? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_h5)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        try {
            data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra(Const.data, Response::class.java)
            } else {
                intent.getSerializableExtra(Const.data) as Response
            }
        } catch (e: Exception) {
            loadProgress(3)
        }
        if (data == null || data?.link?.isEmpty() == true) {
            loadProgress(3)
        } else {
            data?.let { binding.web.loadUrl(it.link) }
            loadProgress(1)
        }
        binding.web.webViewClient = (H5WebViewClient().also {
            it.loadState = object : H5WebViewClient.LoadState {
                override fun start() {
                    loadProgress(1)
                }

                override fun finish() {
                    loadProgress(2)
                }

                override fun error() {
                    loadProgress(3)
                }
            }
            it.data = arrayListOf(H5WebViewClient.Response(data?.link.toString(), true))
        })
        binding.web.webChromeClient = H5WebChromeClinet()
        binding.web.settings.apply {
            javaScriptEnabled = true
            setSupportZoom(true)//允许缩放
            //网页加载内容自适应屏幕
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            loadWithOverviewMode = true
            useWideViewPort = true
        }
    }

    fun loadProgress(step: Int) {
        when (step) {
            1 -> {//加载中
                binding.clLoading.visibility = View.VISIBLE
                binding.clFailure.visibility = View.GONE
            }

            2 -> {//成功
                binding.clLoading.visibility = View.GONE
                binding.clFailure.visibility = View.GONE
            }

            3 -> {//失败
                binding.clLoading.visibility = View.GONE
                binding.clFailure.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.web.stopLoading()
        binding.web.clearHistory()
        binding.web.destroy()
        binding.main.removeAllViews()
    }

    data class Response(var link: String) : Serializable
}