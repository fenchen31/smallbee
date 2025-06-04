package com.practice.smallbee

import android.app.Application
import com.practice.core.ARouter

class SmallbeeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        ARouter.getInstance().init(this, "com.practice.router")
    }
}