package com.practice.common.timer

import android.os.CountDownTimer

class TimerCountDown {
    private var timer: CountDownTimer? = null
    private var listener: TimerListener? = null
    private var remainTime: Long = -1
    private var notifyTime: Int = 1

    fun start(second: Long,noticeTime: Int = 1, listener: TimerListener){
        this.listener = listener
        this.notifyTime = noticeTime
        this.timer = object :CountDownTimer(second*1000, noticeTime.toLong()*1000){
            override fun onTick(remainTime: Long){
                val remainTimeInt = (remainTime + 999)/1000
                this@TimerCountDown.remainTime = remainTimeInt
                listener?.onNotify(remainTimeInt.toInt())
            }
            override fun onFinish() {
                listener?.onFinish()
                timer = null
                remainTime = -1
                this@TimerCountDown.listener = null
            }
        }
        timer?.start()
    }

    fun cancel(){
        if (isUnStartedOrFinished()){
            return
        }
        timer?.cancel()
        listener?.onCancel()
        timer = null
        listener = null
        remainTime = -1
    }

    fun pause(){
        if (isUnStartedOrFinished()){
            return
        }
        timer?.cancel()
        listener?.onPause()
    }

    fun onContinue(){
        if (isUnStartedOrFinished()){
            return
        }
        start(remainTime, notifyTime, listener!!)
        listener!!.onContinue()
    }

    private fun isUnStartedOrFinished():Boolean{
        //        倒计时未开始     ||   倒计时已结束
        return remainTime == -1L || remainTime == 0L
    }
}