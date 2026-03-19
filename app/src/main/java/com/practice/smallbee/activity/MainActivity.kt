package com.practice.smallbee.activity

import android.content.Intent
import android.content.res.Resources
import android.content.res.TypedArray
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.practice.annotation.Route
import com.practice.common.base.BaseActivity
import com.practice.core.ARouter
import com.practice.smallbee.R
import com.practice.smallbee.adapter.ChooseJumpAdapter
import com.practice.smallbee.databinding.ActivityMainBinding
import com.practice.smallbee.view.GalleryAdapter
import com.practice.smallbee.view.GalleryTransformer
import com.practice.smallbee.viewmodel.MainViewModel
import kotlin.math.abs

@Route("app/MainActivity")
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewmodel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    var startX = 0f
    var startY = 0f
    var isScroll = false
    val TAG = "scrollEvent"
    var touchSlop: Int = 0
    private val adapter by lazy {ChooseJumpAdapter()}
    val data = arrayListOf(
        R.mipmap.thumb01,
        R.mipmap.thumb02,
        R.mipmap.thumb03,
        R.mipmap.thumb04,
        R.mipmap.thumb05,
        R.mipmap.thumb06,
        R.mipmap.thumb07,
        R.mipmap.thumb08,
        R.mipmap.thumb09,
        R.mipmap.thumb10
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.root.setOnClickListener({
            ARouter.getInstance().jumpActivity(this,"app/DeviceUpdateActivity", null)
            //ARouter.getInstance().jumpActivity(this, "app/DatabindingActivity", null)
        })
        binding.dash.setOnClickListener { startActivity(Intent(this, BindingRecyclerviewActivity::class.java)) }
        binding.testv.setOnClickListener { ARouter.getInstance().jumpActivity(this, "app/DeviceUpdateActivity") }
        touchSlop = ViewConfiguration.get(this).scaledTouchSlop
        setViewPager()
    }

    override fun initView() {
        binding.adapter = adapter
        viewmodel.jumpData.observe(this) {
            adapter.data = (it)
        }
    }

    override fun loadData() {
        viewmodel.initData()
    }

    fun setViewPager() {
        val screenWidth = resources.displayMetrics.widthPixels
        val pageWidth = screenWidth / 3
        val pageMargin = (screenWidth - pageWidth) / 2 // 左右边距
        val adapter = GalleryAdapter(data)
        binding.viewpager.apply {
            setPageTransformer(GalleryTransformer())
            this.adapter = adapter
            currentItem = 1
            offscreenPageLimit = 1
            // 关键设置：通过 padding 实现画廊效果
            setPadding(pageMargin, 0, pageMargin, 0)
            clipToPadding = false // 允许显示 padding 区域的 item
            clipChildren = false // 允许子视图超出父布局
            setOnTouchListener { v, event ->
                return@setOnTouchListener scrollEvent(
                    event,
                    binding.viewpager.getChildAt(0) as RecyclerView
                )
            }
            (getChildAt(0) as RecyclerView).apply {
                //禁用过度滚动效果，不然事件会被交给viewpager处理导致出现问题
                overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            }
        }
    }

    private fun scrollEvent(event: MotionEvent, recyclerView: RecyclerView): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                binding.viewpager.parent.requestDisallowInterceptTouchEvent(false)
            }

            MotionEvent.ACTION_MOVE -> {
                val moveX = abs(event.x - startX)
                val moveY = abs(event.y - startY)
                if (moveX > touchSlop && moveX > moveY) {
                    binding.viewpager.parent.requestDisallowInterceptTouchEvent(true)
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                binding.viewpager.parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        recyclerView.onTouchEvent(event)
        return true
    }
}