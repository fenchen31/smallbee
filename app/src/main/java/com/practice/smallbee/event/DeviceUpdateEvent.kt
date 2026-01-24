package com.practice.smallbee.event

import androidx.fragment.app.FragmentManager
import com.practice.smallbee.dialog.DeviceUpdateDialog

class DeviceUpdateEvent {

    fun showDialog(manager:FragmentManager){
        DeviceUpdateDialog().show(manager, "DeviceUpdateDialog")
    }
}