package com.practice.common.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.toRectF
import com.practice.common.R
import com.practice.common.util.dp

class SearchView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defAttr: Int = 0
) : View(context, attributeSet, defAttr) {

    private val paint: Paint
    private var topLeftRadius: Float
    private var topRightRadius: Float
    private var bottomLeftRadius: Float
    private var bottomRightRadius: Float
    private var lineColor: Int
    private var lineWidth: Float
    private var iconResource: Drawable
    private var iconSize: Float
    private var iconPadding: Float
    private var avaiableWidth = 0
    private var avaiableHeight = 0
    private var path = Path()
    private val finalRect = Rect()

    init {
        val typeArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.SearchView, defAttr, 0)
        try {
            typeArray.apply {
                val radius = getDimension(R.styleable.SearchView_radius, -1f)
                topLeftRadius =
                    initRadius(radius, getDimension(R.styleable.SearchView_topLeftRadius, -1f))
                topRightRadius = initRadius(radius, getDimension(R.styleable.SearchView_topRightRadius, -1f))
                bottomLeftRadius =
                    initRadius(radius, getDimension(R.styleable.SearchView_bottomLeftRadius, -1f))
                bottomRightRadius =
                    initRadius(radius, getDimension(R.styleable.SearchView_bottomRightRadius, -1f))
                lineColor = getColor(
                    R.styleable.SearchView_lineColor, context.getColor(R.color.color_000000)
                )
                lineWidth = getDimension(R.styleable.SearchView_lineWidth, 3f.dp)
                iconResource = getDrawable(R.styleable.SearchView_iconResource)
                    ?: AppCompatResources.getDrawable(context, R.drawable.ic_delete)!!
                iconSize = getDimension(R.styleable.SearchView_iconSize, 3f.dp)
                iconPadding = getDimension(R.styleable.SearchView_iconPadding, 3f.dp)
            }
        } finally {
            typeArray.recycle()
        }
        paint = Paint().apply {
            color = lineColor
            strokeWidth = lineWidth
            style = Paint.Style.STROKE
            flags = Paint.ANTI_ALIAS_FLAG
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        avaiableWidth = w - paddingStart - paddingEnd
        avaiableHeight = h - paddingTop - paddingBottom
        val rect = RectF(
            paddingStart.toFloat(),
            paddingTop.toFloat(),
            (w - paddingEnd).toFloat(),
            (h - paddingBottom).toFloat()
        )
        val roundRect = floatArrayOf(
            topLeftRadius, topLeftRadius,
            topRightRadius,topRightRadius,
            bottomRightRadius, bottomRightRadius,
            bottomLeftRadius, bottomLeftRadius,
        )
        initIcon(w, h)
        path.reset()
        path.addRoundRect(rect, roundRect, Path.Direction.CW)
    }

    private fun initIcon(w: Int, h: Int){
        val start = (w - paddingEnd - iconSize - iconPadding).toInt()
        val top = (avaiableHeight - iconSize).toInt() / 2
        finalRect.set(start, top, (start + iconSize).toInt(), (top + iconSize).toInt())
    }

    private fun initRadius(radius: Float, selfRadius: Float): Float {
        if (selfRadius != -1f) return selfRadius
        if (radius != -1f) return radius
        return 3f.dp
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (lineWidth > 0f) {
            drawRoundRect(canvas, path)
        }
        iconResource.bounds = finalRect
        iconResource.draw(canvas)
    }

    private fun drawRoundRect(canvas: Canvas, path: Path) {
        paint.color = lineColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = lineWidth
        canvas.drawPath(path, paint)
    }
}