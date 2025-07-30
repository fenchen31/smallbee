package com.practice.common.timer

interface TimerListener{
    fun onNotify(remainTime: Int){}
    fun onFinish() 
    fun onCancel(){}
    fun onPause(){}
    fun onContinue(){}
}