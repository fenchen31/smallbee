package com.practice.smallbee.view

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class GalleryTransformer : ViewPager2.PageTransformer {

    private val CYLINDER_RADIUS = 800f
    private val MIN_SCALE = 0.8F
    private val MAX_SCALE = 1f
    private val MAX_ROTATION = 30f
    private val MIN_ROTATION = -30f
    override fun transformPage(page: View, position: Float) {
        /*when{
            position < -1 -> {
                page.rotationY = 90 * (position + 1)
            }
            position > 1 -> {
                page.rotationY = 90 * (position - 1)
            }
            else ->{
                page.rotationY = position
            }
        }*/
        page.cameraDistance = 2000f

        // 设置旋转中心点
        page.pivotX = page.width / 2f
        page.pivotY = page.height / 2f

        if (position >= -1f && position <= 1f) {
            // 计算当前页面在圆柱体上的角度（弧度）
            val angle = position * Math.toRadians(MAX_ROTATION.toDouble())

            // 计算X轴位移，形成圆弧排列
            val translateX = CYLINDER_RADIUS * sin(angle)

            // 计算Y轴旋转角度
            val rotationY = Math.toDegrees(angle).toFloat()

            // 计算Z轴位移，形成深度感
            val translationZ = -CYLINDER_RADIUS * (1 - cos(angle)).toFloat()

            // 计算缩放因子
            val scale = MIN_SCALE + (1 - MIN_SCALE) * (1 - abs(position))

            page.apply {
                this.rotationY = rotationY
                this.translationX = translateX.toFloat()
                this.translationZ = translationZ
                this.scaleX = scale
                this.scaleY = scale
            }
        } else {
            // 不可见页面重置变换
            page.apply {
                this.rotationY = 0f
                this.translationX = 0f
                this.translationZ = 0f
                this.scaleX = MIN_SCALE
                this.scaleY = MIN_SCALE
            }
        }
    }
    /* override fun transformPage(page: View, position: Float) {

         when {
             //scale
             -1 <= position && position <= 1 -> {
                 val scale = MIN_SCALE + (1 - MIN_SCALE) * (1 - abs(position))
                 page.scaleX = scale
                 page.scaleY = scale
             }
             else -> {
                 page.scaleX = MIN_SCALE
                 page.scaleY = MIN_SCALE
             }
         }
     }*/
}