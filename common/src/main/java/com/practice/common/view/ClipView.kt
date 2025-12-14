package com.practice.common.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.withSave
import com.practice.common.R
import com.practice.common.util.BitmapUtil
import com.practice.common.util.dp
import kotlin.math.min
import androidx.core.graphics.withClip
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import org.jetbrains.annotations.NotNull
import kotlin.math.abs
import kotlin.math.sqrt
import androidx.core.graphics.withMatrix

class ClipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private val TAG = "ClipView"
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var bitmap: Bitmap
    private val transform = Matrix()
    private var scaleX = 0f
    private var scaleY = 0f
    private val camera = Camera()
    private val BITMAP_PADDING = 100.dp
    private var left = 0f
    private var top = 0f
    private val rect = RectF()
    private var right = 0f
    private var bottom = 0f
    private var availabeWidth = 0f
    private var availabeHalfWidth = 0f
    private var availabeHeight = 0f
    private var availabeHalfHeight = 0f
    private var xDegress = 30f
    var degress = 10f
        set(value) {
            field = value
            invalidate()
        }
    private val sqrt2 = 10//sqrt(2.0).toFloat()
    var xDrgess = 0f
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }
    var yDrgess = 0f
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }
    var zDrgess = 0f
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }

    init {
        camera.setLocation(0f, 0f, -6 * resources.displayMetrics.density)
        paint.color = ContextCompat.getColor(context, R.color.color_DB3A3A)
        paint.strokeWidth = 2.dp
        paint.style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        left = paddingStart + BITMAP_PADDING
        top = paddingTop + BITMAP_PADDING
        right = w - paddingEnd - BITMAP_PADDING
        bottom = h - paddingBottom - BITMAP_PADDING
        availabeWidth = right - left
        availabeHalfWidth = availabeWidth / 2
        availabeHeight = bottom - top
        availabeHalfHeight = availabeHeight / 2
        val targetWith = min(right - left, bottom - top)
        bitmap = BitmapUtil.getAvatar(resources, R.mipmap.girls1, targetWith.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        canvas.withClip(paddingStart, paddingTop, width - paddingEnd, height - paddingBottom) {
            flipTop(canvas)
            flipBottomWithMatrix(canvas)
            //flipBottomCustom(canvas)
        }
    }
    private fun flipTop(canvas: Canvas) {
        canvas.withSave {
            //移
            transform.reset()
            transform.postTranslate(left + availabeHalfWidth, top + availabeHalfHeight)
            //transform.postRotate(degress)
            canvas.concat(transform)
            canvas.rotate(degress)
            //裁
            rect.set(-availabeHalfWidth * sqrt2, -availabeHalfHeight * sqrt2, availabeHalfWidth * sqrt2, 0f)
            canvas.withClip(rect) {
                //反移
                transform.reset()
                //transform.postRotate(-degress)
                canvas.rotate(-degress)
                transform.postTranslate(-(left + availabeHalfWidth), -(top + availabeHalfHeight))
                canvas.concat(transform)
                drawBitmap(canvas)
            }
        }
    }

    //传统方式
    private fun flipBottomCustom(canvas: Canvas) {
        canvas.save()
        canvas.translate(left + availabeHalfWidth, top + availabeHalfHeight)
        canvas.rotate(-degress)
        with(camera) {
            save()
            rotateX(xDegress)
            applyToCanvas(canvas)
            restore()
        }
        rect.set(-availabeHalfWidth, 0f, availabeHalfWidth, availabeHalfHeight)
        canvas.clipRect(rect)
        canvas.rotate(degress)
        canvas.translate(-(left + availabeHalfWidth), -(top + availabeHalfHeight))
        drawBitmap(canvas)
    }

    private fun flipBottomWithMatrix(canvas: Canvas) {
        canvas.withSave {
            //移
            transform.reset()
            transform.postTranslate(left + availabeHalfWidth, top + availabeHalfHeight)
            //transform.postRotate(degress)
            canvas.concat(transform)
            canvas.rotate(degress)
            //裁
            rect.set(
                -availabeHalfWidth * sqrt2,
                0f,
                availabeHalfWidth * sqrt2,
                availabeHalfHeight * sqrt2
            )
            canvas.withClip(rect) {
                //转
                with(camera) {
                    save()
                    rotateX(xDegress)
                    transform.reset()
                    getMatrix(transform)
                    canvas.concat(transform)
                    restore()
                }
                //反移
                transform.reset()
                //transform.postRotate(-degress)
                canvas.rotate(-degress)
                transform.postTranslate(-(left + availabeHalfWidth), -(top + availabeHalfHeight))
                canvas.concat(transform)
                drawBitmap(canvas)
            }
        }
    }


    private fun drawBitmap(canvas: Canvas) {
        scaleX = (right - left) / bitmap.width
        scaleY = (bottom - top) / bitmap.height
        transform.reset()
        transform.postTranslate(left, top)
        //方法1
        transform.postScale(scaleX, scaleY, left, top)
        //方法2
        //transform.postScale(scaleX, scaleY)
        //transform.postTranslate(-(left * scaleX - left), -(top * scaleY - top))
        canvas.drawBitmap(bitmap, transform, null)
    }
}