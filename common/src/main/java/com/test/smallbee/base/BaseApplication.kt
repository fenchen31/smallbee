package com.test.smallbee.base

import android.app.Application
import com.test.smallbee.util.SharePreferencesUtil
import com.test.smallbee.util.SmallBeeException

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(SmallBeeException(this))
        SharePreferencesUtil.init(this, SharePreferencesUtil.FILE_NAME)
    }
}