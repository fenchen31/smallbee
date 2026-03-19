package com.practice.smallbee.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practice.annotation.Route
import com.practice.common.base.BaseActivity
import com.practice.common.util.dp
import com.practice.smallbee.R
import com.practice.smallbee.databinding.ActivityTestBinding

@Route("app/TestActivity")
class TestActivity : BaseActivity<ActivityTestBinding>(R.layout.activity_test) {
    override fun initView() {
        var right = 10f.dp.toInt()
        binding.searchview.onIconClickListener = {
            Toast.makeText(this, "点击了icon", Toast.LENGTH_SHORT).show()
        }
        /*binding.searchview.setOnClickListener {
            Toast.makeText(this, "点击了view", Toast.LENGTH_SHORT).show()
        }*/
        binding.root.setOnClickListener {
            right += 1
            binding.searchview.width += right
        }
    }

    override fun loadData() {

    }

}