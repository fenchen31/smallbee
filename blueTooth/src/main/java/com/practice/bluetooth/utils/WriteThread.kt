package com.practice.bluetooth.utils

import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.databinding.ObservableField
import com.practice.common.base.BaseCallback
import com.practice.common.service.ApiService

class WriteThread private constructor() {

    companion object {
        @Volatile
        private var instance: WriteThread? = null

        fun getInstance(): WriteThread {
            return instance ?: synchronized(this) {
                instance ?: WriteThread().also { instance = it }
            }
        }
    }

    private lateinit var socket: BluetoothSocket
    private var callback: BaseCallback<String>? = null
    private val TAG = "WriteDataThread"
    private val executorService by lazy { ApiService.getInstance().okHttpClient.dispatcher.executorService }
    var data = ObservableField("")
        set(value) {
            field = value
            sendData(value.get())
        }

    fun updateSocket(socket: BluetoothSocket, callback: BaseCallback<String>? = null) {
        recycle()
        this.socket = socket
        this.callback = callback
    }


    private fun sendData(data: String?) {
        if (data.isNullOrEmpty()) {
            return
        }
        executorService.execute {
            try {
                val byte = data.toByteArray()
                val outputString = socket.getOutputStream()
                outputString.write(byte)
                outputString.flush()
                callback?.onSuccess(data)
                Log.d(TAG, "run: 数据发送成功${data.toString()}")
            } catch (e: Exception) {
                Log.e(TAG, "run: 数据发送失败${data.toString()} \n$e")
                callback?.onFailure()
            }
            this.data.set("")
        }
    }

    fun recycle() {
        try {
            if (::socket.isInitialized) {
                socket.close()
            }
        } catch (e: Exception) {
            Log.e(TAG, "recycle: 关闭socket失败$e")
        }
    }
}