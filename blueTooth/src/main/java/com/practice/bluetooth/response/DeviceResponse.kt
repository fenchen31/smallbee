package com.practice.bluetooth.response

import androidx.annotation.DrawableRes

data class DeviceResponse(
    val name: String,
    @DrawableRes val resourceId: Int
)
