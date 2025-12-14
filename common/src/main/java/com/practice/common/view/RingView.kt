package com.practice.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.practice.common.R
import com.practice.common.util.dp

class RingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defattr: Int = 0, defStyle: Int = 0
) : View(context, attrs, defattr, defStyle) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textBounds = Rect()
    private var text = "IndioPg"
    private var fontMetrics = FontMetrics()

    init {
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 10.dp
            color = ContextCompat.getColor(context, R.color.color_4CAF50)
        }
    }

    override fun onDraw(canvas: Canvas) {
        //TODO 1.画圆环
        val centerX = (width - paddingStart - paddingEnd) / 2f
        val centerY = (height - paddingTop - paddingBottom) / 2f
        val radius = (width - paddingStart - paddingEnd) / 4f
        canvas.drawCircle(
            centerX, centerY, radius, paint
        )
        //TODO 2.画进度
        paint.apply {
            strokeCap = Paint.Cap.ROUND//设置两端为圆形
            color = ContextCompat.getColor(context, R.color.color_DB3A3A)
            style = Paint.Style.STROKE
        }
        canvas.drawArc(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius,
            -90f,
            290f,
            false,
            paint
        )
        paint.reset()
        paint.color = ContextCompat.getColor(context, R.color.color_000000)
        canvas.drawLine(0f, centerY, width.toFloat(), centerY, paint)
        //TODO 3.画文字
        paint.apply {
            color = ContextCompat.getColor(context, R.color.color_FFC107)
            style = Paint.Style.FILL
            textSize = 20.dp
            textAlign = Paint.Align.CENTER
        }
        /*//静态文字适合绝对居中
        paint.getTextBounds(text, 0, text.length, textBounds)
        canvas.drawText(
            text,
            (paddingStart + (width - paddingStart - paddingEnd) / 2f),
            paddingTop + (height - paddingTop - paddingBottom) / 2f - (textBounds.top + textBounds.bottom) / 2,//垂直居中
            paint
        )*/
        //动态文字适合权重居中
        paint.getFontMetrics(fontMetrics)
        canvas.drawText(text, paddingStart + width / 2f,
            paddingTop + (height - paddingTop - paddingBottom) / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2,
            paint
            )
        //文字贴边
        paint.textAlign = Paint.Align.LEFT
        fontMetrics = paint.getFontMetrics()
        paint.textSize = 120.dp
        paint.getTextBounds(text, 0, text.length, textBounds)
        canvas.drawText(text, textBounds.left.toFloat(), -textBounds.top.toFloat(), paint)

        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 120.dp
        fontMetrics = paint.getFontMetrics()
        paint.getTextBounds(text, 0, text.length, textBounds)
        canvas.drawText(text, -textBounds.left.toFloat(), -textBounds.top.toFloat() + paint.fontSpacing, paint)
    }
}