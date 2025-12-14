package com.practice.smallbee.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.smallbee.R
import com.practice.smallbee.adapter.LocateAdapter
import com.practice.smallbee.databinding.ActivityMainBinding
import com.practice.smallbee.databinding.ActivityViewLocateBinding
import com.practice.smallbee.event.ViewLocateEvent
import com.practice.smallbee.viewmodel.ViewLocateVM

class ViewLocateActivity : AppCompatActivity() {

    val vm by lazy { ViewModelProvider(this)[ViewLocateVM::class.java] }
    val event by lazy { ViewLocateEvent() }
    val binding by lazy { ActivityViewLocateBinding.inflate(layoutInflater) }
    val adapter by lazy { LocateAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding.vm = vm
        binding.event = event
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
        loadNet()
        initObserver()
    }

    private fun initView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun initObserver() {
        vm.topData.observe(this){
            setTopToView(it)
        }
        vm.girlsData.observe(this){
            adapter.data = it
        }
    }
    fun setTopToView(datas: ArrayList<String>){
        if (datas.isNotEmpty()){

        }
    }

    fun loadNet(){
        vm.getLocatePictures()
        vm.getGirlsData()
    }
}