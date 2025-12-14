package com.practice.smallbee.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.practice.annotation.Route
import com.practice.smallbee.R
import com.practice.smallbee.adapter.DatabindingAdapter
import com.practice.smallbee.databinding.ActivityDatabindingBinding
import com.practice.smallbee.viewmodel.DatabindingListVM

@Route("app/DatabindingActivity")
class DatabindingActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDatabindingBinding
    private val viewmodel: DatabindingListVM by viewModels()
    private lateinit var adapter: DatabindingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_databinding)
        binding.lifecycleOwner = this
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        adapter = DatabindingAdapter(this)
        viewmodel.itemViewModels.observe(this, Observer { data->
            adapter.setViewmodelList(data)
            adapter.notifyDataSetChanged()
        })
        binding.rvUser.adapter = adapter
    }
}