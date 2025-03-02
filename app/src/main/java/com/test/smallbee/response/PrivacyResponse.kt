package com.test.smallbee.response

import java.io.Serializable

class PrivacyResponse(var data: ArrayList<PrivacyItem>) : Serializable {
    class PrivacyItem(var item: ArrayList<PrivacyItemChild>) : Serializable {
        class PrivacyItemChild(
            var text: String,
            var color: String? = null,
            var link: String? = null
        ) : Serializable
    }
}