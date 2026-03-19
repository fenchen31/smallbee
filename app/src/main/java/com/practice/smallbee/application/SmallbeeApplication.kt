package com.practice.smallbee.application

import android.app.Application
import com.practice.common.util.MediaSessionUtil
import com.practice.core.ARouter

class SmallbeeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        ARouter.getInstance().init(this, "com.practice.router")
    }
}