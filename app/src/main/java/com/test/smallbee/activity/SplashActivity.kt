package com.test.smallbee.activity

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.test.smallbee.app.R
import com.test.smallbee.app.databinding.ActivitySplashBinding
import com.test.smallbee.dialog.PrivacyDialog

class SplashActivity : AppCompatActivity() {
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
        var dialog = PrivacyDialog(R.layout.dialog_privacy)
        dialog.show(supportFragmentManager, PrivacyDialog::class.simpleName)
        val text = "文字1dk分给恶心了昂打开我想拉屎；"

        val clickSpan = object : ClickableSpan(){
            override fun onClick(widget: View) {
                Toast.makeText(this@SplashActivity, "点击", Toast.LENGTH_SHORT).show()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = getColor(R.color.color_8BC34A)
                ds.isUnderlineText = false//去除下划线
            }

        }
        val span = SpannableString(text)
        span.setSpan(clickSpan, 0, text.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        span.setSpan(ForegroundColorSpan(Color.RED), 0, 6, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        //binding.tvTest.text = span
        binding.tvTest.movementMethod = LinkMovementMethod.getInstance()
        var builder = SpannableStringBuilder()
        builder.append(span)
        binding.tvTest.setText(builder)
    }
}