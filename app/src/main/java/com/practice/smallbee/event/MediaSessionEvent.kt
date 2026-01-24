package com.practice.smallbee.event

import com.practice.common.util.MediaSessionUtil
import com.practice.smallbee.viewmodel.MediaSessionVM

class MediaSessionEvent {
    fun clickGetMediaInfo(vm: MediaSessionVM) {
        vm.checkPermission.value = true
    }
}