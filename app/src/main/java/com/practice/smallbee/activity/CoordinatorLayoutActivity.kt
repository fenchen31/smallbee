package com.practice.smallbee.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.practice.annotation.Route
import com.practice.smallbee.R
import com.practice.smallbee.adapter.CoordiantorAdapter
import com.practice.smallbee.databinding.ActivityCoordinatorLayoutBinding
import com.practice.smallbee.viewmodel.CoordinatorlayoutViewModel

@Route("app/CoordinatorLayoutActivity")
class CoordinatorLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoordinatorLayoutBinding
    private val viewmodel by lazy {
        ViewModelProvider(this)[CoordinatorlayoutViewModel::class.java]
    }
    private val adapter = CoordiantorAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCoordinatorLayoutBinding.inflate(layoutInflater)
        binding.vm = viewmodel
        binding.adapter = adapter
        binding.lifecycleOwner = this
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setListener()
        var lastNumber = 0
        viewmodel.number.observe(this) {
            when{
                lastNumber <= it -> ViewCompat.offsetTopAndBottom(binding.btInit, 10)
                else -> ViewCompat.offsetTopAndBottom(binding.btInit, -10)
            }
            lastNumber = it
        }
    }

    override fun onResume() {
        super.onResume()
        viewmodel.loadRecyclerViewData()
        viewmodel.recyclerData.observe(this){
            adapter.data = it
        }
    }

    private fun setListener() {
        binding.btInit.setOnClickListener { viewmodel.increase() }
        binding.tvSport.setOnClickListener { viewmodel.reduce() }
    }
}