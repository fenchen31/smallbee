package com.practice.smallbee.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.practice.smallbee.R
import com.practice.smallbee.databinding.ActivityBinderProxyBinding
import com.practice.smallbee.event.BinderProxyActivityEvent
import com.practice.smallbee.viewmodel.BinderProxyViewModel

class BinderProxyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityBinderProxyBinding.inflate(layoutInflater) }
    private val viewmodel by lazy {
        ViewModelProvider(this)[BinderProxyViewModel::class.java]
            .apply { defaultData(getString(R.string.has_received_null)) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_binder_proxy)
        binding.lifecycleOwner = this
        binding.vm = viewmodel
        binding.event = BinderProxyActivityEvent(viewmodel)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}