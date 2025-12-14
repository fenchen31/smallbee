package com.practice.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.graphics.PathMeasure
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.practice.common.R
import com.practice.common.util.dp
import com.practice.common.util.px
import kotlin.math.cos
import kotlin.math.sin

private const val ANGEL = 120
private val DASH_WIDTH = 2f.dp
private val DASH_LENGTH = 20f.dp
private val DASH_LENGTH_THICK = 40f.dp
private val DASH_WIDTH_THICK = 3f.dp
private val RADIUS = 150f.px
private val POINTER_LENGTH = 100f.dp
private val PIECES = 20

/**
 * @author      黎亮亮
 * @Date        2025.9.9
 * @Description 汽车仪表盘
 */
class DashBoardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)//抗锯齿
    private val markGraphics = Path()//细刻度的形状
    private val thickMarkGraphics = Path()//细刻度的形状
    private val circleArcPath = Path()
    private lateinit var pathEffect: PathDashPathEffect//细刻度
    private lateinit var thickPathEffect: PathDashPathEffect//粗刻度
    private var centerX: Float = 0.0f
    private var centerY: Float = 0.0f
    private var pointer: Int = 10

    init {
        paint.style = Paint.Style.STROKE//划线模式
        paint.strokeWidth = 3f.dp
        markGraphics.addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CCW)
        thickMarkGraphics.addRect(0f, 0f, DASH_WIDTH_THICK, DASH_LENGTH_THICK, Path.Direction.CCW)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = (w - paddingStart - paddingEnd) / 2f
        centerY = (h - paddingTop - paddingBottom) / 2f
        /*circleArcPath.addArc(
            centerX - RADIUS,
            centerY - RADIUS,
            centerX + RADIUS,
            centerY + RADIUS,
            90 + ANGEL / 2f,
            360f - ANGEL
        )*/
        circleArcPath.addArc(
            paddingStart.toFloat() + paint.strokeWidth / 2,
            paddingTop.toFloat() + paint.strokeWidth / 2,
            w - paddingEnd.toFloat() - paint.strokeWidth / 2,
            h - paddingBottom.toFloat() - paint.strokeWidth / 2,
            90 + ANGEL / 2f,
            360f - ANGEL
        )
        val measure = PathMeasure(circleArcPath, false)
        //PathDashPathEffect用另一个path来做path的效果 ， advance：多少距离之后才开始绘制刻度，phase：间隔多少绘制一个刻度
        // 但是Android将这两个弄反了，所以adnvace和phase这两个要反着写。如从200开始绘制，间隔50，那么advance应该为50，phase为200
        pathEffect = PathDashPathEffect(
            markGraphics,
            (measure.length - DASH_WIDTH) / PIECES,
            0f,
            PathDashPathEffect.Style.ROTATE
        )
        thickPathEffect = PathDashPathEffect(
            thickMarkGraphics,
            (measure.length - DASH_WIDTH_THICK) / 4f,
            0f,
            PathDashPathEffect.Style.ROTATE
        )
    }

    override fun onDraw(canvas: Canvas) {
        paint.color = ContextCompat.getColor(context, R.color.color_000000)
        //TODO 1.绘制弧线
        canvas.drawPath(circleArcPath, paint)
        //TODO 2.绘制细刻度
        paint.pathEffect = pathEffect
        canvas.drawPath(circleArcPath, paint)
        //TODO 3.绘制粗刻度
        paint.pathEffect = thickPathEffect
        canvas.drawPath(circleArcPath, paint)
        paint.pathEffect = null
        //TODO 4.绘制指针
        //指针角度 =    圆弧起始角度    +     该点占据总弧度的角度值
        val angle = (90 + ANGEL / 2) + (360 - ANGEL) / PIECES * pointer.toDouble()
        paint.color = ContextCompat.getColor(context, R.color.color_DB3A3A)
        val small = Math.min((width - paddingStart - paddingEnd), (height - paddingTop - paddingBottom))
        val dashLength = (small - paint.strokeWidth) / 2 * 0.8
        //Android中三角函数使用的是弧度制，angle只是角度，需要用Math.toRadians转换成弧度制
        canvas.drawLine(
            centerX,
            centerY,
            ((centerX + dashLength * cos(Math.toRadians(angle))).toFloat()),
            ((centerY + dashLength * sin(Math.toRadians(angle))).toFloat()),
            paint
        )
    }

    fun setPointer(pointer: Int) {
        this.pointer = pointer
        invalidate()
    }
}