package com.practice.smallbee.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.practice.annotation.Route
import com.practice.smallbee.R
import com.practice.smallbee.databinding.ActivityDeviceUpdateBinding
import com.practice.smallbee.dialog.DeviceUpdateDialog
import com.practice.smallbee.event.DeviceUpdateEvent
import com.practice.smallbee.viewmodel.DeviceUpdateVM

@Route("app/DeviceUpdateActivity")
class DeviceUpdateActivity : AppCompatActivity() {

    val binding by lazy { ActivityDeviceUpdateBinding.inflate(layoutInflater) }
    val event by lazy { DeviceUpdateEvent() }
    val viewmodel by lazy { ViewModelProvider(this)[DeviceUpdateVM::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding.viewmodel = viewmodel
        binding.event = event
        binding.manager = supportFragmentManager
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        DeviceUpdateDialog().show(supportFragmentManager, "DeviceUpdateDialog")
    }
}