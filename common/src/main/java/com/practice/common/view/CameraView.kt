package com.practice.common.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.practice.common.R
import com.practice.common.util.BitmapUtil

class CameraView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private lateinit var bitmap:Bitmap
    private val camera:Camera = Camera()
    private val BITMAP_PADDING = 10f
    private var BITMAP_SIZE: Float = 0.0f
    private val topFlip = 0f
    private val bottomFlip = 30f
    private val flipRotation = 30f

    init {
        camera.setLocation(0f, 0f, -6 * resources.displayMetrics.density)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        BITMAP_SIZE = w - paddingStart - paddingEnd - BITMAP_PADDING * 2
        bitmap = BitmapUtil.getAvatar(resources, R.mipmap.thumb07, BITMAP_SIZE.toInt())
    }
    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.translate(BITMAP_PADDING + BITMAP_SIZE / 2, BITMAP_PADDING + BITMAP_SIZE / 2)
        canvas.rotate(-flipRotation)
        camera.save()
        camera.rotateX(topFlip)
        camera.applyToCanvas(canvas)
        camera.restore()
        canvas.clipRect(-BITMAP_SIZE, -BITMAP_SIZE, BITMAP_SIZE, 0f)
        canvas.rotate(flipRotation)
        canvas.translate(-(BITMAP_PADDING + BITMAP_SIZE / 2), -(BITMAP_PADDING + BITMAP_SIZE / 2))
        canvas.drawBitmap(bitmap, paddingStart + BITMAP_PADDING, paddingTop + BITMAP_PADDING, null)
        canvas.restore()
    }
}