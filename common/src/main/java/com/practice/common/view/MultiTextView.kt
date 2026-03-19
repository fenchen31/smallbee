package com.practice.common.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.graphics.Rect
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.practice.common.R
import com.practice.common.util.BitmapUtil
import com.practice.common.util.dp
import kotlin.math.log

class MultiTextView @JvmOverloads constructor(
    context: Context, set: AttributeSet? = null, attr: Int = 0, defStyle: Int = 0
) : View(context, set, attr, defStyle) {
    private val text =
        "My mother and I first chose a Christmas tree, a" +
                "nd it was taller than me! Then, we began to pic" +
                "k out some beautiful little pendants to decorat" +
                "e the Christmas tree. I chose a beautiful big s" +
                "tar, outside is silver white, " +
                "inside is pink, with gold embr" +
                "oidered English \"Merry Christm" +
                "as\", looks like a glowing; and" +
                " then I chose a gold ring, a f" +
                "ew green leaves below it, ther" +
                "e are two bright small red and" +
                " a beautiful little red flowers and green leaves" +
                " in the middle. A mother picked up red grapes," +
                " with a yellow bow, bright grape can show my mo" +
                "ther and I smile. We also chose a cute little s" +
                "nowman, a red calico fan, three white foam flak" +
                "es and a box of colorful flashing lights."
    private val textPaint = TextPaint().apply {
        isAntiAlias = true
        textSize = 20.dp
    }
    private val TAG = MultiTextView::class.java.simpleName
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var builder: StaticLayout.Builder
    private lateinit var staticLayout: StaticLayout
    private lateinit var bitmap: Bitmap
    private val bitmapTargetWidth = 150.dp
    private var fontMetrics = FontMetrics()
    private var textBounds = Rect()
    private val pictureTop = 100.dp
    private var maxWidth = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        builder = StaticLayout.Builder.obtain(
            text, 0, text.length, textPaint, w - paddingStart - paddingEnd
        )
        bitmap = BitmapUtil.getAvatar(resources, R.mipmap.thumb07, bitmapTargetWidth.toInt())
    }

    override fun onDraw(canvas: Canvas) {/*staticLayout = builder.build()
        staticLayout.draw(canvas)*/
        textPaint.getFontMetrics(fontMetrics)
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        textPaint.color = ContextCompat.getColor(context, R.color.color_DB3A3A)
        canvas.drawBitmap(bitmap, width - paddingEnd - bitmapTargetWidth, pictureTop, bitmapPaint)

        //贴上贴左
        var start = 0
        var end: Int
        var startY = -fontMetrics.top + paddingTop//文字上边界 + paddingTop = 起始y
        while (start < text.length) {
            when {
                startY +  fontMetrics.bottom < pictureTop || startY + fontMetrics.top > pictureTop + bitmap.height ->
                    maxWidth = (width - paddingStart - paddingEnd).toFloat()

                else -> maxWidth = width - paddingStart - paddingEnd - bitmapTargetWidth
            }
            end = textPaint.breakText(text, start, text.length, true, maxWidth, null)
            canvas.drawText(
                text, start, start + end,
                -textBounds.left + paddingStart.toFloat(),
                startY, textPaint
            )
            start += end
            startY += textPaint.fontSpacing
        }
    }
}