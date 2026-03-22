package com.practice.bluetooth.activity

import android.bluetooth.BluetoothDevice
import androidx.fragment.app.Fragment
import com.practice.blueTooth.R
import com.practice.blueTooth.databinding.ActivityBlueToothBinding
import com.practice.bluetooth.fragment.ScanFragment
import com.practice.bluetooth.fragment.WriteDataFragment
import com.practice.bluetooth.utils.EventBusConst
import com.practice.common.base.BaseActivity
import com.practice.common.base.BaseEvent
import com.practice.common.base.BaseFragment

class BlueToothActivity : BaseActivity<ActivityBlueToothBinding>(R.layout.activity_blue_tooth) {


    override fun initView() {

    }

    override fun loadData() {
        jump(ScanFragment::class.java, true)
    }

    override fun onEventBusEvent(event: BaseEvent) {
        when (event.code) {
            EventBusConst.EVENT_JUMP_TO_SEND_MESSAGE -> {
                val arguments = HashMap<String, BluetoothDevice>().apply {
                    put(BaseFragment.DATA, event.data as BluetoothDevice)
                }
                jump(WriteDataFragment::class.java, data = arguments)
            }
            EventBusConst.EVENT_JUMP_TO_CONNECT_DEVICE -> jump(ScanFragment::class.java)
        }
    }

    private fun <T : BaseFragment<*>> jump(clazz: Class<T>, add: Boolean = true, data: HashMap<String, *> ?= null) {
        val fragment = BaseFragment.newInstance(clazz, data?.let { data })
        val transaction = supportFragmentManager.beginTransaction()
        if (add) {
            transaction.add(R.id.view_fragment, fragment)
        } else {
            transaction.replace(R.id.view_fragment, fragment)
        }
        transaction.commit()
    }

}