package com.test.smallbee.base

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.test.smallbee.common.R
import com.test.smallbee.common.databinding.ActivityErrorBinding

class ErrorActivity : AppCompatActivity() {
    companion object{
        @JvmStatic val DATA = "data"
    }
    lateinit var binding: ActivityErrorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_error)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        try {
            binding.tvText.movementMethod = ScrollingMovementMethod()
            binding.tvText.text = intent.getStringExtra(DATA).toString()
        } catch (e: Exception){
            binding.tvText.text = getString(R.string.no_error_message)
        }
    }
}