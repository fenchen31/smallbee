package com.practice.common.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView

class SquareImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet ?= null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr){

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(measuredWidth, widthMeasureSpec)
        val height = resolveSize(measuredHeight, heightMeasureSpec)
        val size = when{
            width - paddingStart - paddingEnd > height - paddingTop - paddingBottom->   height
            width - paddingStart - paddingEnd < height - paddingTop - paddingBottom ->  width
            else -> if (width > height) height else width
        }
        setMeasuredDimension(size, size)
    }
}