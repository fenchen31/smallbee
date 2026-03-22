package com.practice.common.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseFragment<B : ViewDataBinding>(@LayoutRes val layoutId: Int) : Fragment() {

    open lateinit var binding: B
    private var argumentsMap: HashMap<String, Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argumentsMap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(DATA, HashMap::class.java) as HashMap<String, Any>?
        } else {
            arguments?.getSerializable(DATA) as HashMap<String, Any>?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        EventBus.getDefault().register(this)
        initBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(argumentsMap)
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    abstract fun initBinding()//设置xml中的变量,如binding.viewmodel = viewmodel

    abstract fun initView(arguments: HashMap<String, Any>? = null)

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEventBusEvent(event: BaseEvent) {
    }

    companion object {
        open val DATA = "data"
        fun <T : BaseFragment<out ViewDataBinding>> newInstance(
            clazz: Class<T>, data: HashMap<String, *>? = null
        ): T {
            val fragment = clazz.getDeclaredConstructor().newInstance() as T
            data?.let {
                fragment.arguments = Bundle().apply { putSerializable(DATA, data) }
            }
            return fragment
        }
    }
}