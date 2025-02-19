package com.test.smallbee.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.test.smallbee.base.ErrorActivity
import java.io.PrintWriter
import java.io.StringWriter

class WholeException(var context: Context) : Thread.UncaughtExceptionHandler {
    private val TAG = WholeException::class.simpleName

    override fun uncaughtException(t: Thread, e: Throwable) {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        e.printStackTrace(printWriter)
        Log.e(TAG, stringWriter.toString())
        context.startActivity(Intent(context, ErrorActivity::class.java).apply {
            putExtra(ErrorActivity.DATA, stringWriter.toString())
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(10)
    }
}