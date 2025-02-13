package com.test.smallbee.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.smallbee.adapter.PrivacyAdapter
import com.test.smallbee.app.R
import com.test.smallbee.app.databinding.DialogPrivacyBinding
import com.test.smallbee.response.PrivacyResponse


class PrivacyDialog(val layoutId: Int) : DialogFragment() {
    lateinit var adapter: PrivacyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding: DialogPrivacyBinding = DataBindingUtil.inflate<DialogPrivacyBinding>(
            LayoutInflater.from(context),
            layoutId,
            container,
            false
        )
        binding.rvPrivacy.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = PrivacyAdapter(R.layout.item_privacy)
        binding.rvPrivacy.adapter = adapter
        adapter.data = setData()
        isCancelable = false
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val window = it.window ?: return
            //去除dialog默认的白色背景
            window.decorView.setBackgroundResource(android.R.color.transparent)
            //重置dialog宽高
            val params = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = params
            //设置margin
            val margin = (10f * resources.displayMetrics.density + 0.5f).toInt()
            val contentView = (window.decorView as ViewGroup).getChildAt(0)
            if (contentView.layoutParams is ViewGroup.MarginLayoutParams){
                val marginParams = contentView.layoutParams as ViewGroup.MarginLayoutParams
                marginParams.setMargins(margin, 0, margin, 0)
                contentView.layoutParams = marginParams
            } else{
                val marginParams = ViewGroup.MarginLayoutParams(contentView.layoutParams)
                marginParams.setMargins(margin, 0, margin, 0)
                val parent = contentView.parent as? ViewGroup
                parent?.updateViewLayout(contentView, marginParams)
            }
        }
    }

    fun setData(): ArrayList<PrivacyResponse> {
        val data: ArrayList<PrivacyResponse> = ArrayList()
        for (i in 0..4) {
            val response: PrivacyResponse = PrivacyResponse()
            response.text = "这是数据mm" + i
            data.add(response)
        }
        return data
    }
}