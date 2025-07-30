package com.practice.common.timer

import android.os.Handler
import android.os.Looper
import java.util.Timer
import java.util.TimerTask

/**
 * 倒计时
 * 关闭应用后仍在后台执行
 */
class TimerAlarmManager {
    private var timer: Timer? = null
    private var listener: TimerListener? = null
    private var remainTime = -1
    private var notifyTime = 1
    private var time: Int? = 0
    fun start(second: Int, notifyTime: Int = 1, listener: TimerListener? = null) {
        this.listener = listener
        this.notifyTime = notifyTime
        timer = Timer()
        remainTime = second
        time = second
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                remainTime -= notifyTime
                when {
                    remainTime <= 0 -> {
                        timer!!.cancel()
                        Handler(Looper.getMainLooper()).post(Runnable { listener?.onFinish() })
                    }

                    remainTime in 1..second -> {
                        Handler(Looper.getMainLooper()).post(Runnable {
                            listener?.onNotify(
                                remainTime
                            )
                        })
                        timer!!.schedule(this, 0, (notifyTime * 1000).toLong())
                    }
                }

            }
        }, 0, (notifyTime * 1000).toLong())
    }

    fun cancel() {
        if (isUnStartedOrFinished()) {
            return
        }
        timer?.cancel()
        timer = null
        Handler(Looper.getMainLooper()).post(Runnable {
            listener?.onCancel()
            listener = null
        })
        remainTime = -1
    }

    fun pause() {
        if (isUnStartedOrFinished()) {
            return
        }
        Handler(Looper.getMainLooper()).post(Runnable {
            listener?.onPause()
            listener = null
        })
        timer?.cancel()
    }

    fun onContinue() {
        if (isUnStartedOrFinished()) {
            return
        }
        start(remainTime, notifyTime, listener)
        Handler(Looper.getMainLooper()).post(Runnable { listener?.onContinue() })
    }

    private fun isUnStartedOrFinished(): Boolean {
        //        倒计时未开始    ||   倒计时已结束
        return remainTime == -1 || remainTime == 0
    }

}