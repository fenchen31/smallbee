package com.practice.common.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.practice.common.R
import com.practice.common.util.BitmapUtil

class ScalableImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?, defStyleAttr: Int = -1, defStyleRes: Int = -1
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var bitmap = BitmapUtil.getAvatar(resources, R.mipmap.girls1)
    private var offsetX = 0f
    private var offsetY = 0f
    private var smallScale = 1f
    private var bigScale = 1f
    private var scaleInside = false //图片是以大边缩放（大边缩放则图片全部在view中，小边缩放则大的边会被部分裁剪）
    private var scaleProgress = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val scaleAnimator by lazy {
        ObjectAnimator.ofFloat(this, "scaleProgress", 0f, 1f).apply { duration = 300 }
    }
    private var availableWidth = 0
    private var availableHeight = 0
    private val gestureDetector =
        GestureDetector(context, object : GestureDetector.OnGestureListener {
            override fun onDown(e: MotionEvent): Boolean {
                return true//返回true代表需要消费事件
            }

            //按下状态的回调
            override fun onShowPress(e: MotionEvent) {}

            //单击（不支持长按时，按下抬起就表示一次点击,支持长按时需要自己和单点做区分，这个方法是抬起之后立即响应，
            // 而onSingleTapConfirmed是已经确认不是双击之后响应（缺点就是会延迟300ms之后调用，原理就是300ms之内没有第二次down事件就触发单击））
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true//true和false无所谓，这个返回值是给系统做记录的
            }

            //手指滑动的时候就会被调用，类似onMove的效果，参数里面的distance都是 last - currect
            override fun onScroll(
                e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float
            ): Boolean {
                return true//返回值无用
            }

            override fun onLongPress(e: MotionEvent) {}

            //手指快速滑动然后抬起时调用，用来做惯性滑动
            override fun onFling(
                e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float
            ): Boolean {
                return true//返回值无用
            }
        }).apply {
            setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {

                //控件支持长按时，在第一次up事件之后的300ms之内没有第二次down事件就触发单击
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    return false
                }

                //第二次按下与第一次抬起间隔300ms时触发（已具有防抖动功能）
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    scaleInside = !scaleInside
                    if (scaleAnimator.isRunning) {
                        scaleAnimator.end()
                    }
                    if (scaleInside) {
                        scaleAnimator.start()
                    } else {
                        scaleAnimator.reverse()
                    }
                    return true
                }

                //老安卓手机中双击之后拦截后续事件，之后事件并不会触发原屏幕控件的触摸响应
                override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                    return false
                }
            })
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        availableWidth = w - paddingStart - paddingEnd
        availableHeight = h - paddingTop - paddingBottom
        offsetX = (availableWidth - bitmap.width) / 2f
        offsetY = (availableHeight - bitmap.height) / 2f
        when {
            availableWidth / bitmap.width.toFloat() > availableHeight / bitmap.height.toFloat() -> {
                smallScale = availableHeight / bitmap.height.toFloat()
                bigScale = availableWidth / bitmap.width.toFloat()
            }

            else -> {
                smallScale = availableWidth / bitmap.width.toFloat()
                bigScale = availableHeight / bitmap.height.toFloat()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val scale = smallScale + (bigScale - smallScale) * scaleProgress
        canvas.scale(scale, scale, availableWidth / 2f, availableHeight / 2f)
        canvas.drawBitmap(bitmap, offsetX, offsetY, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
}