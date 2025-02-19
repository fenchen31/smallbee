package com.test.smallbee.util

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.Serializable
/*
* 1.当网页被重定向时，没有正确处理重定向url与原url的映射关系，导致loading无法消失
* 2.没有处理返回按钮逻辑
* */
class H5WebViewClient : WebViewClient() {
    private val TAG = "H5WebViewClient"
    lateinit var data: ArrayList<Response>
    lateinit var loadState: LoadState
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        Log.d(TAG, "onPageStarted: $url")
        url?.let { outPutState(1, it) }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        url?.let { outPutState(2, url) }
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        request?.url?.let { outPutState(3, request.url.toString()) }
    }

    private fun outPutState(state: Int, webviewUrl: String) {
        val url = UrlTransfer.transferToOriginUrl(webviewUrl)
        if (data.any {
                //还原url中的中文字符
                val originUrl = UrlTransfer.transferToOriginUrl(it.url)
                it.controlState == true && it.url.isNotEmpty()
                        && (originUrl == url || originUrl == url.substring(0, url.length - 1))
            }) {
            state(state)
        }
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return super.shouldOverrideUrlLoading(view, request)
    }

    private fun state(state: Int) {
        when (state) {
            1 -> loadState.start()
            2 -> loadState.finish()
            3 -> loadState.error()
        }
    }

    interface LoadState {
        fun start()
        fun finish()
        fun error()
    }

    data class Response(var url: String, var controlState: Boolean? = true) : Serializable
}