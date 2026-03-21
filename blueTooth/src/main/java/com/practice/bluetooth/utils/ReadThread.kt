package com.practice.bluetooth.utils

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.practice.common.base.BaseCallback
import com.practice.common.service.ApiService
import java.io.InputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.atomic.AtomicBoolean

class ReadThread private constructor() {

    companion object {
        @Volatile
        private var instance: ReadThread? = null

        fun getInstance(): ReadThread {
            return instance ?: synchronized(this) {
                instance ?: ReadThread().also { instance = it }
            }
        }
    }

    private lateinit var socket: BluetoothSocket
    private var callback: BlueToothUtil.ReceiveDataCallback ?= null
    private lateinit var executorService: ExecutorService
    private val TAG = "ReadThread"
    private val END_MARK = byteArrayOf(0x03.toByte())
    private val BUFFER_SIZE = 1024 * 8
    private val isRunning = AtomicBoolean(false)
    private val stringBuilder = StringBuilder()

    fun updateSocket(socket: BluetoothSocket, callback: BlueToothUtil.ReceiveDataCallback?) {
        recycle()
        this.socket = socket
        this.callback = callback
        executorService = ApiService.getInstance().okHttpClient.dispatcher.executorService
        startReceiveData()
    }

    private fun hasEndMark(bytes: ByteArray): Int {
        val markSize = END_MARK.size
        if (bytes.isEmpty() || END_MARK.isEmpty() || bytes.size < markSize) {
            return -1
        }
        for (i in bytes.size - markSize downTo 0) {
            if(bytes[i] == END_MARK[END_MARK.size - 1]) {
                for (j in 1 until markSize) {
                    if (bytes[i - j] != END_MARK[markSize - 1 - j]){
                        break
                    }
                }
                return i - markSize + 1
            }
        }
        return -1
    }

    private fun startReceiveData() {
        if (!isRunning.compareAndSet(false, true)) {
            return//重复启动
        }
        executorService.execute {
            val inputStream = try {
                socket.inputStream
            } catch (e: Exception) {
                callback?.onReceiveData(errMsg = e.message.toString())
                isRunning.compareAndSet(true, false)
                Log.e(TAG, "startReceiveData:获取inputStream失败： $e")
                return@execute
            }
            val buffer = ByteArray(BUFFER_SIZE)
            while (isRunning.get() && socket.isConnected) {
                parseData(buffer, inputStream)
            }
        }
    }

    private fun parseData(buffer: ByteArray, inputStream: InputStream) {
        try {
            val size = inputStream.read(buffer)
            if (size > 0) {
                val endPosition = hasEndMark(buffer)
                if (endPosition >= 0){
                    stringBuilder.append(buffer.copyOfRange(0, endPosition))
                    Log.i(TAG, "startReceiveData: $stringBuilder")
                    callback?.onReceiveData(data = stringBuilder.toString())
                    stringBuilder.clear()
                } else {
                    stringBuilder.append(buffer)
                }
            }
        } catch (e: Exception) {
            callback?.onReceiveData(errMsg = e.message.toString())
            Log.e(TAG, "startReceiveData:$e")
            stringBuilder.clear()
        }
    }

    fun recycle() {
        isRunning.compareAndSet(true, false)
        stringBuilder.clear()
        try {
            if (::socket.isInitialized) {
                socket.close()
            }
        } catch (e: Exception) {
            Log.e(TAG, "recycle:socket-> $e")
        }
    }
}