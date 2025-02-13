package com.test.smallbee.adapter

import com.test.smallbee.app.databinding.ItemPrivacyBinding
import com.test.smallbee.base.SimpleAdapter
import com.test.smallbee.response.PrivacyResponse

class PrivacyAdapter(layoutId: Int) : SimpleAdapter<PrivacyResponse, ItemPrivacyBinding>(layoutId) {
    override fun bindItemData(binding: ItemPrivacyBinding, data: PrivacyResponse, position: Int) {
        binding.tvText.setText(data.text)
    }
}