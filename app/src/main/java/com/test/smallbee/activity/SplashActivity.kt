package com.test.smallbee.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.test.smallbee.app.R
import com.test.smallbee.app.databinding.ActivitySplashBinding
import com.test.smallbee.base.BaseActivity
import com.test.smallbee.dialog.PrivacyDialog
import com.test.smallbee.util.Const
import com.test.smallbee.util.ScreenUtil
import com.test.smallbee.util.SharePreferencesUtil

class SplashActivity : BaseActivity() {
    private val TAG: String? = SplashActivity::class.simpleName
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (SharePreferencesUtil.getBoolean(Const.agree_privacy)) {
            object : CountDownTimer(1500, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                }

                override fun onFinish() {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            }.start()
        } else {
            PrivacyDialog(R.layout.dialog_privacy).show(supportFragmentManager, TAG)
        }
        getDrawable(R.drawable.ic_app_logo)?.let {
            val bounds = ScreenUtil.dp2px(this, 25F)
            it.setBounds(0, 0, bounds, bounds)
            binding.tvAppname.setCompoundDrawablesRelative(it, null, null, null)
            binding.tvAppname.compoundDrawablePadding = ScreenUtil.dp2px(this, 5f)
        }
    }
}