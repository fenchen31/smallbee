package com.practice.smallbee.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.smallbee.R
import com.practice.smallbee.adapter.BindingRecyclerViewAdapter
import com.practice.smallbee.databinding.ActivityBindingRecyclerviewBinding
import com.practice.smallbee.viewmodel.BindingRecyclerviewViewmodel

class BindingRecyclerviewActivity : AppCompatActivity() {
    lateinit var binding : ActivityBindingRecyclerviewBinding
    val adapter = BindingRecyclerViewAdapter()
    val viewmodel: BindingRecyclerviewViewmodel by lazy {
        ViewModelProvider(this)[BindingRecyclerviewViewmodel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_binding_recyclerview)
        binding.adapter = adapter
        binding.lifecycleOwner = this
        viewmodel.loadData()
        viewmodel.data.observe(this){
            adapter.data = it
            adapter.notifyDataSetChanged()
        }
        binding.btChangedata.setOnClickListener { viewmodel.changeData() }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}