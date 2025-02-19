package com.test.smallbee.adapter

import android.content.Intent
import android.graphics.Color.parseColor
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.test.smallbee.activity.H5Activity
import com.test.smallbee.app.R
import com.test.smallbee.app.databinding.ItemPrivacyContentBinding
import com.test.smallbee.app.databinding.ItemPrivacyTitleBinding
import com.test.smallbee.base.CommonAdapter
import com.test.smallbee.response.PrivacyResponse
import com.test.smallbee.util.Const

class PrivacyMultiAdapter : CommonAdapter() {
    override fun bindItemData(dataBinding: ViewBinding?, itemData: List<Any>, position: Int) {
        when (dataBinding) {
            is ItemPrivacyTitleBinding -> {
                setData((itemData.get(0) as PrivacyResponse.PrivacyItem), dataBinding.tvText)
            }

            is ItemPrivacyContentBinding -> {
                setData((itemData.get(0) as PrivacyResponse.PrivacyItem), dataBinding.tvText)
            }
        }
    }

    private fun setData(data: PrivacyResponse.PrivacyItem, view: TextView) {
        val builder = SpannableStringBuilder()
        view.movementMethod = LinkMovementMethod.getInstance()
        view.isClickable = true
        for (i in 0..<data.item.size) {
            val child = data.item.get(i)
            if (child.link != null && child.link!!.trim().isNotEmpty()) {
                val clickString = SpannableString(child.text)
                val clickSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        context.startActivity(
                            Intent(context, H5Activity::class.java)
                                .putExtra(
                                    Const.data, H5Activity.Response(child.link.toString())
                                )
                        )
                    }

                }
                clickString.setSpan(
                    clickSpan,
                    0,
                    clickString.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                var color: Int
                try {
                    color = parseColor(child.color)
                } catch (e: Exception) {
                    color = context.getColor(R.color.color_D12424)
                }
                clickString.setSpan(
                    ForegroundColorSpan(color),
                    0,
                    clickString.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                builder.append(clickString)
            } else {
                builder.append(child.text)
            }
        }
        view.text = builder
    }
}