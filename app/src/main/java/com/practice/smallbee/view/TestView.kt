package com.practice.smallbee.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class TestView : View {
    lateinit var paint : Paint
    constructor(context: Context):this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, -1)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr){
        paint = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 20f
        }
        val pa = layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    override fun onDraw(canvas: Canvas) {
        var point = height / 2f
        canvas.drawLine(0f, point, width.toFloat(), point, paint)
        canvas.save()
        var threePercent = height * 3 / 4f
        paint.color = Color.BLUE
        canvas.translate(width / 2f, point)
        canvas.rotate(10f)
        canvas.drawLine(-width / 2f, 0f, width / 2f, 0f, paint)
        canvas.restore()
        paint.color = Color.BLACK
        canvas.drawLine(0f, threePercent, width.toFloat(), threePercent, paint)
    }
}