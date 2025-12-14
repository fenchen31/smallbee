package com.practice.common.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children

class TagLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val bounds = mutableListOf<Rect>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        /*for ((index, child) in children.withIndex()){
            val params = child.layoutParams
            var childWidth = 0
            when(params.width){
                MeasureSpec.EXACTLY ->{
                    when(widthSpecMode){
                        MeasureSpec.EXACTLY ->{
                            childWidth = MeasureSpec.makeMeasureSpec(usedWidth, MeasureSpec.EXACTLY)
                        }
                        MeasureSpec.AT_MOST -> {
                            childWidth = MeasureSpec.makeMeasureSpec(usedWidth, MeasureSpec.AT_MOST)
                        }
                        else -> {
                            childWidth = MeasureSpec.makeMeasureSpec(usedWidth, MeasureSpec.AT_MOST)
                        }
                    }
                }
                MeasureSpec.AT_MOST -> {
                    when(widthSpecMode){
                        MeasureSpec.EXACTLY ->{

                        }
                    }
                }
            }
        }
        val childWidthSpec = MeasureSpec.makeMeasureSpec(childSize, childMode)
        child.measure(childWidthSpec, childHeightSpec)*/
    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for ((index, child) in children.withIndex()){
            child.layout(bounds[index].left, bounds[index].top, bounds[index].right, bounds[index].bottom)
        }
    }
}