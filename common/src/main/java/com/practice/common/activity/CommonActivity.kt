package com.practice.common.activity


import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.practice.annotation.Route
import com.practice.common.R
import com.practice.common.databinding.ActivityCommonBinding
import com.practice.common.timer.TimerHandler
import com.practice.common.timer.TimerListener
import com.practice.common.util.dp
import com.practice.common.view.ClipView

@Route("common/CommonActivity")
class CommonActivity : AppCompatActivity() {
    private val TAG: String = "commonActivity"
    lateinit var binding: ActivityCommonBinding
    var animateFinished: Boolean = true
    lateinit var animateListener: AnimatorListener
    lateinit var animator: ViewPropertyAnimator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_common)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.avBitmap.setOnClickListener {
            /*if (animateFinished) {
                bitmapAnimation(binding.avBitmap)
            } else {
                animationRecover(binding.avBitmap)
            }*/
            animate(binding.avBitmap)
        }
        animateListener = object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                animateFinished = false
            }

            override fun onAnimationEnd(animation: Animator) {
                animateFinished = true
                animationRecover(binding.avBitmap)
            }

            override fun onAnimationCancel(animation: Animator) {
                animateFinished = true
                animationRecover(binding.avBitmap)
            }

            override fun onAnimationRepeat(animation: Animator) {}
        }
        rotation(binding.clRotation)
    }

    private fun rotation(clRotation: ClipView) {
        var canClickBtnX = true
        var canClickBtnY = true
        var canClickBtnZ = true
        binding.btX.setOnClickListener {
            if (!canClickBtnX) {
                return@setOnClickListener
            }
            clRotation.degress = 0f
            TimerHandler().start(36000, 1, object : TimerListener {
                override fun onFinish() {
                    canClickBtnX = true
                    clRotation.degress = 0f
                }

                override fun onNotify(remainTime: Int) {
                    val degress = 360f - remainTime * 10
                    Log.d(TAG, "x轴旋转xDrgress：$degress")
                    clRotation.degress = degress
                }
            })
        }
        binding.btY.setOnClickListener {
            if (!canClickBtnY) {
                return@setOnClickListener
            }
            clRotation.yDrgess = 0f
            TimerHandler().start(36, 1, object : TimerListener {
                override fun onFinish() {
                    canClickBtnY = true
                    clRotation.yDrgess = 0f
                }

                override fun onNotify(remainTime: Int) {
                    val degress = 360f - remainTime * 10
                    Log.d(TAG, "y轴旋转yDrgress：$degress")
                    clRotation.yDrgess = degress
                }
            })
        }
        binding.btZ.setOnClickListener {
            if (!canClickBtnZ) {
                return@setOnClickListener
            }
            clRotation.zDrgess = 0f
            TimerHandler().start(36, 1, object : TimerListener {
                override fun onFinish() {
                    canClickBtnZ = true
                    clRotation.zDrgess = 0f
                }

                override fun onNotify(remainTime: Int) {
                    val degress = 360f - remainTime * 10
                    Log.d(TAG, "z轴旋转zDrgress：$degress")
                    clRotation.zDrgess = degress
                }
            })
        }
    }

    fun animate(view: View) {
        val animator = ObjectAnimator.ofFloat(view, "radius", 20.dp)
        animator.duration = 1500
        animator.start()
    }

    fun bitmapAnimation(view: View) {
        if (::animator.isInitialized) {
            animator.cancel()
        }
        animator =
            view.animate().scaleX(1.2f).scaleY(1.2f).translationX(100.dp).translationY(10.dp)
                .alpha(0.5f).setListener(animateListener).setDuration(1500)
        animator.start()
    }

    fun animationRecover(view: View) {
        view.animate().apply {
            scaleX(1f)
            scaleY(1f)
            duration = 0
            translationX(0f)
            translationY(0f)
            alpha(1f)
            setListener(null)
        }
    }
}