package com.practice.smallbee.response

import java.io.Serializable

data class UserResponse(val id:Int,
                        var online:Boolean,
                        var name:String,) : Serializable {
}