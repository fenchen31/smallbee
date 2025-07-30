package com.practice.common.util

import android.content.res.Resources

object DpPx {
    fun dp2px(dp: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun px2dp(px: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (px / scale - 0.5f).toInt()
    }
}