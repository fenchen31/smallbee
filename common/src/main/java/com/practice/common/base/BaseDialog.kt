package com.practice.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

abstract class BaseDialog<B : ViewDataBinding>(
    private val bind: B? = null, @LayoutRes val layoutId: Int? = null
) : DialogFragment() {

    init {
        require(bind != null || layoutId != null) {
            "binding和layoutId必须传入一个，不能同时为空"
        }
    }

    lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = bind ?: DataBindingUtil.inflate(inflater, layoutId!!, container, false)
        initView()
        initData()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        resizeDialog()
    }

    private fun resizeDialog() {
        dialog?.window?.let {
            val rootParams = binding.root.layoutParams
            val windowParams = it.attributes

            // 直接使用XML中定义的尺寸属性
            windowParams.width = rootParams.width
            windowParams.height = rootParams.height

            // 特殊处理wrap_content
            if (rootParams.width == ViewGroup.LayoutParams.WRAP_CONTENT ||
                rootParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                it.setLayout(
                    rootParams.width,
                    rootParams.height
                )
            } else {
                it.attributes = windowParams
            }
        }
    }

    abstract fun initView()

    abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
        if (::binding.isInitialized) {
            binding.unbind()
        }
    }

}