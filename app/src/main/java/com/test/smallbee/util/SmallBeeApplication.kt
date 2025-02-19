package com.test.smallbee.util

import android.app.Application

class SmallBeeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(WholeException(this))
    }
}