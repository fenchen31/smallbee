package com.test.smallbee.util

import android.os.Build
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class UrlTransfer {
    companion object {
        //将转义过的url解码成原来的url
        @JvmStatic
        fun transferToOriginUrl(incodeUrl: String): String {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                return URLDecoder.decode(incodeUrl, StandardCharsets.UTF_8)
            } else {
                return URLDecoder.decode(incodeUrl, "UTF-8")
            }
        }
    }
}
