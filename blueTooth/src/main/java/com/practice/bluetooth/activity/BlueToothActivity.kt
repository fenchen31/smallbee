package com.practice.bluetooth.activity

import com.practice.blueTooth.R
import com.practice.blueTooth.databinding.ActivityBlueToothBinding
import com.practice.bluetooth.fragment.ScanFragment
import com.practice.common.base.BaseActivity
import com.practice.common.base.BaseFragment

class BlueToothActivity : BaseActivity<ActivityBlueToothBinding>(R.layout.activity_blue_tooth) {


    override fun initView() {

    }

    override fun loadData() {
        val fragment = BaseFragment.newInstance(ScanFragment::class.java)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.view_fragment, fragment).commit()
    }

}