package com.practice.common.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.BitmapRegionDecoder
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import java.io.InputStream
import kotlin.math.max
import kotlin.math.min

class BigImageView constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    private val TAG = "BigImageView"
    private var bitmapWidth: Int = 0
    private var bitmapHeight: Int = 0
    private val availableRect by lazy { Rect() }
    private var availableWidth = 0//可用宽度
    private var availableHeight = 0//可用高度
    private var scale = 1f//图片/控件
    private var decoder: BitmapRegionDecoder? = null
    private var imageResource: Any? = null
    private val paint by lazy { Paint().apply { isAntiAlias = true } }
    private val options by lazy { BitmapFactory.Options() }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        availableWidth = max(w - paddingStart - paddingEnd, 0)
        availableHeight = max(h - paddingTop - paddingBottom, 0)
    }

    override fun setImageResource(resId: Int) {
        calculateImageSize(resId)
        initDecoder(resId)
        invalidate()
        availableRect.set(0, 0, availableWidth, availableHeight)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        imageResource = drawable
        if (drawable is BitmapDrawable) {
            drawable.bitmap.let { bitmapWidth = it.width;bitmapHeight = it.height }
        }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        imageResource = bm
        bm?.let { bitmapWidth = it.width; bitmapHeight = it.height }
    }

    override fun setImageURI(uri: Uri?) {
        imageResource = uri
        uri?.let { calculateImageSize(it) }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        super.setImageDrawable(null)
        when (val source = imageResource) {
            is Int -> drawFromResource(canvas, source)
            is Bitmap -> drawFromBitmap(canvas, source)
            is Drawable -> drawFromDrawable(canvas, source)
            is Uri -> drawFromuri(canvas, source)
        }
    }

    private fun drawFromuri(canvas: Canvas, source: Uri) {
        if (isLargeImage()) {
            drawLargeImage(canvas, options)
        } else {
            //小图直接加载
            try {
                val stream = context.contentResolver.openInputStream(source)
                val bitmap = BitmapFactory.decodeStream(stream)
                stream?.close()
                canvas.drawBitmap(bitmap, 0f, 0f, paint)
                bitmap.recycle()
            } catch (e: Exception) {
                Log.e(TAG, "drawFromuri 错误$e")
            }
        }
    }

    private fun drawFromDrawable(canvas: Canvas, source: Drawable) {
        if (source is BitmapDrawable) {
            drawFromBitmap(canvas, source.bitmap)
        } else {
            source.setBounds(0, 0, availableWidth, availableHeight)
            source.draw(canvas)
        }
    }

    private fun drawFromBitmap(canvas: Canvas, source: Bitmap) {
        if (isLargeImage()) {
            drawLargeImage(canvas, options)
        } else {
            canvas.drawBitmap(source, 0f, 0f, paint)
        }
    }

    private fun drawFromResource(canvas: Canvas, source: Int) {
        if (isLargeImage()) {
            drawLargeImage(canvas, options)
        } else {
            val bitmap = BitmapFactory.decodeResource(resources, source)
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
            bitmap.recycle()
        }
    }

    private fun drawLargeImage(canvas: Canvas, options: Options) {
        decoder?.let { decoder ->
            val rect = getRect()
            val sampleSize = calculateSampleSize()
            val adjustRect = Rect(
                rect.left, rect.top, min(rect.right, bitmapWidth), min(rect.bottom, bitmapHeight)
            )

            options.inSampleSize = sampleSize
            val bitmap = decoder.decodeRegion(adjustRect, options)
            canvas.drawBitmap(bitmap, rect.left.toFloat(), rect.right.toFloat(), paint)
            bitmap.recycle()
        }
    }

    private fun calculateSampleSize(): Int {
        val scaleX = bitmapWidth.toFloat() / availableWidth
        //val scaleY
        return 0
    }

    private fun isLargeImage(): Boolean {
        //1.宽或高超过控件的2倍
        val sizeCondition = bitmapWidth > availableWidth * 2 || bitmapHeight > availableHeight * 2
        if (sizeCondition) {
            return true//直接return，优化
        }
        //2.图片总像素超过控件所占像素的4倍
        val pixelThreshold = availableWidth * availableHeight * 4
        val pixelCount = bitmapWidth * bitmapHeight
        val qualityCondition = pixelCount > pixelThreshold
        return qualityCondition
    }

    private fun initDecoder(resId: Int) {
        var inputStream: InputStream? = null
        try {
            inputStream = context.resources.openRawResource(resId)
            decoder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val bytes = inputStream.readBytes()
                BitmapRegionDecoder.newInstance(bytes, 0, bytes.size)
            } else {
                BitmapRegionDecoder.newInstance(inputStream, false)!!
            }
            bitmapHeight = decoder!!.height
            bitmapWidth = decoder!!.width
        } catch (e: Exception) {
            Log.e(TAG, "initDecoder 失败$e")
        } finally {
            try {
                inputStream?.close()
            } catch (e: Exception) {
                Log.e(TAG, "initDecoder inputStream.close() 错误$e")
            }
        }
    }

    private fun calculateImageSize(data: Any) {
        when (data) {
            is Int -> getBitmapSize(options) { BitmapFactory.decodeResource(resources, data, it) }
            is Uri -> {
                try {
                    val stream = context.contentResolver.openInputStream(data)
                    getBitmapSize(options) { BitmapFactory.decodeStream(stream, null, it) }
                    stream?.close()
                } catch (e: Exception) {
                    Log.e(TAG, "calculateImageSize 错误$e")
                }
            }
        }
    }

    private fun getBitmapSize(
        options: Options, block: (Options) -> Unit
    ) {
        options.inJustDecodeBounds = true
        block(options)
        bitmapWidth = options.outWidth
        bitmapHeight = options.outHeight
        options.inJustDecodeBounds = false
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        decoder?.recycle()
    }

    private fun getRect(): Rect {
        //上下分别拓展0.2倍availableHeight
        val left = availableRect.left
        val top = max((availableRect.top - availableHeight * 0.2).toInt(), 0)
        val right = availableRect.right
        val bottom = min((availableRect.bottom + availableHeight * 0.2).toInt(), bitmapHeight)
        return Rect(left, top, right, bottom)
    }
}