package com.practice.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.practice.common.R
import com.practice.common.util.px
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author      黎亮亮
 * @Date        2025.9.9
 * @Description 饼图
 */
private var OFFSET_LENGTH = 10

class CakeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var data: Array<IntArray> = arrayOf(
        intArrayOf(ContextCompat.getColor(context, R.color.color_DB3A3A), 10),
        intArrayOf(ContextCompat.getColor(context, R.color.color_000000), 30),
        intArrayOf(ContextCompat.getColor(context, R.color.color_2196F3), 20),
        intArrayOf(ContextCompat.getColor(context, R.color.color_FFC107), 10),
        intArrayOf(ContextCompat.getColor(context, R.color.color_4CAF50), 30)
    )
    private val path = Path()
    private var startAngle = 0f
    private var onePieceAngle = 0f
    private val rect = RectF()
    private var selectCake = 3

    init {
        paint.strokeWidth = 1f.px
        paint.style = Paint.Style.FILL
        onePieceAngle = 360f / 100
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val blankWidth = (w - paddingStart - paddingEnd) / 4f
        val blankHeight = (h - paddingTop - paddingBottom) / 4f
        rect.set(
            paddingStart + blankWidth,
            paddingTop + blankHeight,
            w - paddingEnd - blankWidth,
            h - paddingBottom - blankHeight
        )
    }

    override fun onDraw(canvas: Canvas) {
        startAngle = 0f
        var endAngle = 0f
        path.reset()
        for ((index, i) in data.withIndex()) {
            startAngle += endAngle
            endAngle = i[1] * onePieceAngle
            paint.color = i[0]
            if (index == selectCake) {
                canvas.save()
                val offsetX =
                    OFFSET_LENGTH * cos(Math.toRadians((endAngle / 2f + startAngle).toDouble())).toFloat()
                val offsetY =
                    OFFSET_LENGTH * sin(Math.toRadians((endAngle / 2f + startAngle).toDouble())).toFloat()
                canvas.translate(offsetX, offsetY)
            }
            canvas.drawArc(rect, startAngle, endAngle, true, paint)
            if (index == selectCake) {
                canvas.restore()
            }
        }
    }

    //选中的圆饼（x轴下方第一个index = 0）
    fun setSelectCake(selectCake: Int) {
        this.selectCake = selectCake
        invalidate()
    }
}