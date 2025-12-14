package com.practice.smallbee.adapter

import com.practice.common.recyclerview.SingleAdapter
import com.practice.smallbee.R
import com.practice.smallbee.databinding.ItemCoordinatorlayoutScrollBinding
import com.practice.smallbee.response.CoordinatorResponse

class BindingRecyclerViewAdapter :
    SingleAdapter<ItemCoordinatorlayoutScrollBinding, CoordinatorResponse>(R.layout.item_coordinatorlayout_scroll) {
    override fun bindingItem(
        binding: ItemCoordinatorlayoutScrollBinding,
        position: Int,
        data: ArrayList<CoordinatorResponse>
    ) {
        binding.item = data[position]
    }
}