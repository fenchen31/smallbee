package com.practice.smallbee.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import androidx.viewbinding.ViewBindings
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.practice.annotation.Route
import com.practice.smallbee.R
import com.practice.smallbee.adapter.ViewPagerAdapter
import com.practice.smallbee.databinding.ActivityViewPagerBinding
import com.practice.smallbee.databinding.ItemIndicatorBinding
import com.practice.smallbee.viewmodel.ViewPagerViewModel

@Route("app/ViewPagerActivity")
class ViewPagerActivity : AppCompatActivity() {

    private val binding by lazy { ActivityViewPagerBinding.inflate(LayoutInflater.from(this)) }
    private val viewmodel by lazy { ViewPagerViewModel() }
    private lateinit var adapter:ViewPagerAdapter
    private var currectPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        adapter = ViewPagerAdapter()
        binding.adapter = adapter
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initTablayout()
        viewmodel.loadData()
        viewmodel.data.observe(this){
            adapter.data = it
        }
    }
    fun setViewPager(){
        binding.vpImage.adapter = adapter
        TabLayoutMediator(binding.tlIndicator, binding.vpImage, {tab, position->
            /*val indicator = LayoutInflater.from(this).inflate(R.layout.item_indicator, binding.tlIndicator, false)
            tab.customView = indicator
            indicator.isSelected = (position == currectPosition)
            */
            val lastTab = binding.tlIndicator.getTabAt(currectPosition)
            lastTab?.view?.isSelected = false
            val  tab = binding.tlIndicator.getTabAt(position)
            tab?.view?.isSelected = true
            currectPosition = position
        }).attach()
        binding.vpImage.registerOnPageChangeCallback(object : OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position != currectPosition){
                    binding.tlIndicator.getTabAt(currectPosition)?.view?.isSelected = false
                    binding.tlIndicator.getTabAt(position)?.view?.isSelected = true
                    currectPosition = position
                }
            }
        })
        binding.tlIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }
        })
    }

    fun initTablayout(){
        val data = arrayListOf("1", "2", "3", "4", "5")
        for (i in data.indices){
            val tab = binding.tlIndicator.newTab()
            tab.customView = ItemIndicatorBinding.inflate(LayoutInflater.from(this)).root
            binding.tlIndicator.addTab(tab)
        }
        binding.tlIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.isSelected = true
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.isSelected = false
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }
        })
    }
}