package com.practice.smallbee.adapter

import com.bumptech.glide.Glide
import com.practice.common.recyclerview.SingleAdapter
import com.practice.smallbee.R
import com.practice.smallbee.databinding.ItemLocateBinding

class LocateAdapter : SingleAdapter<ItemLocateBinding, String>(R.layout.item_locate) {
    override fun bindingItem(binding: ItemLocateBinding, position: Int, data: ArrayList<String>) {
        Glide.with(context).load(data[position]).into(binding.ivPicture)
    }
}