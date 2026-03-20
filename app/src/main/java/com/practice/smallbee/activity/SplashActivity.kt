package com.practice.smallbee.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practice.annotation.Route
import com.practice.bluetooth.activity.BlueToothActivity
import com.practice.core.ARouter
import com.practice.smallbee.R
import com.practice.smallbee.databinding.ActivitySplashBinding
import com.practice.smallbee.viewmodel.SplashActivityViewModel

@Route("app/SplashActivity")
class SplashActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    private val viewmodel by lazy { SplashActivityViewModel() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding.lifecycleOwner = this
        binding.vm = viewmodel
        setContentView(binding.root)
        viewmodel.loadData()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewmodel.data.observe(this){
            if (it.data.isNotBlank()){
                startActivity(Intent(this, BlueToothActivity::class.java))
                //ARouter.getInstance().jumpActivity(this, "app/TestActivity")
                finish()
            }
        }
    }


}