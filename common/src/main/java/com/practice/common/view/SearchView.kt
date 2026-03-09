package com.practice.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
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
    private val iconFinalRect = Rect()
    var iconVisible = true
        set(value) {
            field = value
            invalidate()
        }
    private var iconPressed = false
    var onIconClickListener: (() -> Unit)? = null

    init {
        val typeArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.SearchView, defAttr, 0)
        try {
            typeArray.apply {
                val radius = getDimension(R.styleable.SearchView_radius, -1f)
                topLeftRadius =
                    initRadius(radius, getDimension(R.styleable.SearchView_topLeftRadius, -1f))
                topRightRadius =
                    initRadius(radius, getDimension(R.styleable.SearchView_topRightRadius, -1f))
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
                iconVisible = getBoolean(R.styleable.SearchView_iconVisible, true)
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
        initLinePath(w, h)
        initIcon(w, h)
    }

    private fun initLinePath(w: Int, h: Int) {
        val rect = RectF(
            paddingStart.toFloat(),
            paddingTop.toFloat(),
            (w - paddingEnd).toFloat(),
            (h - paddingBottom).toFloat()
        )
        val roundRect = floatArrayOf(
            topLeftRadius, topLeftRadius,
            topRightRadius, topRightRadius,
            bottomRightRadius, bottomRightRadius,
            bottomLeftRadius, bottomLeftRadius,
        )
        path.reset()
        path.addRoundRect(rect, roundRect, Path.Direction.CW)
    }

    private fun initIcon(w: Int, h: Int) {
        val start = (w - paddingEnd - iconSize - iconPadding).toInt()
        val top = (avaiableHeight - iconSize).toInt() / 2
        iconFinalRect.set(start, top, (start + iconSize).toInt(), (top + iconSize).toInt())
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
        if (iconVisible) {
            iconResource.bounds = iconFinalRect
            iconResource.draw(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val rect = emplifyRect(iconFinalRect, 15f)
                if (onIconClickListener != null && iconVisible && rect.contains(event.x, event.y)) {
                    iconPressed = true
                    return true
                }
            }

            MotionEvent.ACTION_UP -> {
                val viewRect =
                    Rect(paddingStart, paddingTop, width - paddingEnd, height - paddingBottom)
                if (viewRect.contains(event.x.toInt(), event.y.toInt())) {
                    val rect = emplifyRect(iconFinalRect, 15f)
                    if (onIconClickListener != null && iconVisible && iconPressed && rect.contains(
                            event.x,
                            event.y
                        )
                    ) {
                        onIconClickListener?.invoke()
                    } else {
                        performClick()
                    }
                    return true
                }
                iconPressed = false
            }

            MotionEvent.ACTION_CANCEL -> {
                iconPressed = false
            }
        }
        return super.onTouchEvent(event)
    }

    private fun emplifyRect(rect: Rect, padding: Float): RectF {
        return RectF(
            rect.left - padding, rect.top - padding, rect.right + padding, rect.bottom + padding
        )
    }

    private fun drawRoundRect(canvas: Canvas, path: Path) {
        paint.color = lineColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = lineWidth
        canvas.drawPath(path, paint)
    }
}