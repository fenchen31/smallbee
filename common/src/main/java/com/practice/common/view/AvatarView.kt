package com.practice.common.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.practice.common.R

/**
 * @author      黎亮亮
 * @Date        2025.9.9
 * @Description 用xfermode实现重叠图形的快速裁切与选择
 */
class AvatarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private val bounds = RectF()
    private lateinit var circleBitmap: Bitmap
    private lateinit var squareBitmap: Bitmap
    var radius: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bounds.set(
            paddingStart.toFloat(),
            paddingTop.toFloat(),
            width - paddingEnd.toFloat(),
            height - paddingBottom.toFloat()
        )
        radius = (width - paddingStart - paddingEnd) / 2f
        circleBitmap =
            createBitmap((bounds.right - bounds.left).toInt(), (bounds.bottom - bounds.top).toInt())
        squareBitmap =
            createBitmap((bounds.right - bounds.left).toInt(), (bounds.bottom - bounds.top).toInt())
        val canvas = Canvas(circleBitmap)
        paint.color = ContextCompat.getColor(context, R.color.color_FFC107)
        canvas.drawCircle(
            bounds.right * 3 / 4f, bounds.bottom * 1 / 4f, bounds.right / 2f, paint
        )
        canvas.setBitmap(squareBitmap)
        paint.color = ContextCompat.getColor(context, R.color.color_2196F3)
        canvas.drawRect(
            0f, bounds.bottom * 1 / 4f, bounds.right * 3 / 4f, bounds.bottom, paint
        )
    }

    override fun onDraw(canvas: Canvas) {/*
        //方式1
        val count = canvas.saveLayer(bounds, null)
        canvas.drawBitmap(circleBitmap, paddingStart.toFloat(), paddingTop.toFloat(), paint)
        paint.xfermode = mode
        canvas.drawBitmap(squareBitmap, paddingStart.toFloat(), paddingTop.toFloat(), paint)
        paint.xfermode = null
        canvas.restoreToCount(count)*/

        //TODO 1.创建离屏缓冲,这时候的绘制就是在离屏缓冲上的
        //方式2
        val count = canvas.saveLayer(bounds, null)
        canvas.drawCircle(
            paddingStart + (width - paddingStart - paddingEnd) / 2f,
            paddingTop + (height - paddingTop - paddingBottom) / 2f,
            radius,
            paint
        )
        paint.xfermode = mode
        val bitmap = getAvatar(R.mipmap.thumb07, width - paddingStart - paddingEnd)
        canvas.drawBitmap(
            bitmap,
            paddingStart.toFloat(),
            paddingTop + (height - paddingTop - paddingBottom - bitmap.height) / 2f,
            paint
        )
        paint.xfermode = null
        //TODO 2.恢复离屏缓冲
        canvas.restoreToCount(count)
    }

    fun getAvatar(@DrawableRes resId: Int, targetWidth: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = targetWidth
        return BitmapFactory.decodeResource(resources, resId, options)
    }
}