package com.practice.common.timer

import android.os.Handler
import android.os.Looper
import java.lang.ref.WeakReference

class TimerHandler {
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var listener: WeakReference<TimerListener>? = null
    private var remainTime = -1
    private var notifyTime = 1

    fun start(second: Int, notifyTime: Int = 1, listener: TimerListener? = null) {
        this.listener = WeakReference(listener)
        handler = Handler(Looper.getMainLooper())
        remainTime = second
        this.notifyTime = notifyTime
        runnable = Runnable() {
            when {
                remainTime == second -> {
                    Thread.sleep(notifyTime.toLong())
                    handler?.postDelayed(runnable!!, notifyTime.toLong() * 1000)
                    remainTime -= notifyTime
                }

                remainTime in 1..<second -> {
                    Thread.sleep(notifyTime.toLong())
                    handler?.postDelayed(runnable!!, notifyTime.toLong() * 1000)
                    this.listener?.get()?.onNotify(remainTime)
                    remainTime -= notifyTime
                }

                else -> {
                    this.listener?.get()?.onFinish()
                    handler?.removeCallbacks(runnable!!)
                }
            }
        }
        handler?.post(runnable!!)
    }

    fun cancel() {
        if (isUnStartedOrFinished()) {
            return
        }
        handler?.removeCallbacks(runnable!!)
        runnable = null
        handler = null
        listener?.get()?.onCancel()
        listener = null
        remainTime = -1
    }

    fun pause() {
        if (isUnStartedOrFinished()) {
            return
        }
        handler?.removeCallbacks(runnable!!)
        runnable == null
        handler = null
        listener?.get()?.onPause()
    }

    fun onContinue() {
        if (isUnStartedOrFinished()) {
            return
        }
        start(remainTime, notifyTime, listener?.get()!!)
        listener?.get()?.onContinue()
    }

    private fun isUnStartedOrFinished(): Boolean {
        //        倒计时未开始    ||   倒计时已结束
        return remainTime == -1 || remainTime == 0
    }
}