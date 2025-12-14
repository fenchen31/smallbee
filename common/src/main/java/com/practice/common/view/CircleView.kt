package com.practice.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.practice.common.R
import com.practice.common.util.dp

class CircleView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var radius: Float = 0f
    private var availableWidth = 0
    private var availableHeight = 0
    private var startX = 0f
    private var startY = 0f
    private val stokenWidth = 15.dp
    private val paint = Paint().apply{
        style = Paint.Style.STROKE
        strokeWidth = stokenWidth
        flags = Paint.ANTI_ALIAS_FLAG
        color = ContextCompat.getColor(getContext(), R.color.color_DB3A3A)
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(measuredWidth, widthMeasureSpec)
        val height = resolveSize(measuredHeight, heightMeasureSpec)
        availableWidth = width - paddingStart - paddingEnd
        availableHeight = height - paddingTop - paddingBottom
        radius = when{
            availableWidth > availableHeight -> availableHeight / 2 - stokenWidth / 2
            else -> availableWidth / 2 - stokenWidth / 2
        }
        startX = paddingStart + availableWidth / 2f
        startY = paddingTop + availableHeight / 2f
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(startX, startY, radius, paint)
    }
}