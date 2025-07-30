package com.practice.common.timer

import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class TimerScheduled {
    lateinit var executor: ScheduledExecutorService

    fun start(second:Int, listener: TimerListener){
        executor = Executors.newSingleThreadScheduledExecutor()
        executor.schedule({
            listener.onFinish()
        },second.toLong(), TimeUnit.SECONDS)
        executor.scheduleAtFixedRate(Runnable {  }, 0, 1, TimeUnit.SECONDS)
    }
}