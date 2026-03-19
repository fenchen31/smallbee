package com.practice.smallbee.adapter

import com.practice.common.recyclerview.SingleAdapter
import com.practice.smallbee.R
import com.practice.smallbee.databinding.ItemChooseJumpBinding
import com.practice.smallbee.response.ChooseJumpResponse
import com.practice.smallbee.viewmodel.ItemChooseJumpVM

class ChooseJumpAdapter :
    SingleAdapter<ItemChooseJumpBinding, ChooseJumpResponse>(R.layout.item_choose_jump) {
    override fun bindingItem(
        binding: ItemChooseJumpBinding,
        position: Int,
        data: ArrayList<ChooseJumpResponse>
    ) {
        val viewmodel = ItemChooseJumpVM()
        binding.viewmodel = viewmodel
        binding.context = context
        viewmodel.text.value = data[position].text
        viewmodel.jumpData.value = data[position].jumpData
    }
}