package com.practice.common.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes

object BitmapUtil {
    fun getAvatar(resources: Resources, @DrawableRes resId: Int, targetWidthInPX: Int? = null): Bitmap {
        when{
            targetWidthInPX == null -> return BitmapFactory.decodeResource(resources, resId)
            else -> {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeResource(resources, resId, options)
                options.inJustDecodeBounds = false
                options.inDensity = options.outWidth
                options.inTargetDensity = targetWidthInPX
                return BitmapFactory.decodeResource(resources, resId, options)
            }
        }
    }
}