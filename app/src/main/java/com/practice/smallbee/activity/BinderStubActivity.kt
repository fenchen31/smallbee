package com.practice.smallbee.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.practice.smallbee.databinding.ActivityBinderStubBinding
import com.practice.smallbee.event.BinderStubActivityEvent
import com.practice.smallbee.viewmodel.BinderStubViewModel

class BinderStubActivity : AppCompatActivity() {
    private val binding by lazy { ActivityBinderStubBinding.inflate(layoutInflater) }
    private val viewmodel by lazy { ViewModelProvider(this)[BinderStubViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.viewmodel = viewmodel
        binding.event = BinderStubActivityEvent(viewmodel)
        binding.lifecycleOwner = this
        viewmodel.loadData()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}