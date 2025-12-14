package com.practice.common.util

class ClickUtil {
    companion object {
        private var lastClickTime: Long = 0
        fun isFastClick(time: Int = 500): Boolean {
            val nowTime = System.currentTimeMillis()
            return if (nowTime - lastClickTime > time) {
                lastClickTime = nowTime
                false
            } else {
                lastClickTime = nowTime
                true
            }
        }
    }
}