package com.practice.smallbee.adapter

import android.widget.SimpleAdapter
import com.practice.common.recyclerview.SingleAdapter
import com.practice.smallbee.R
import com.practice.smallbee.databinding.ItemViewpagerBinding
import com.practice.smallbee.response.ViewPagerResponse

class ViewPagerAdapter:SingleAdapter<ItemViewpagerBinding, ViewPagerResponse>(R.layout.item_viewpager) {
    override fun bindingItem(
        binding: ItemViewpagerBinding,
        position: Int,
        data: ArrayList<ViewPagerResponse>
    ) {
        binding.response = data[position]
        //binding.ivGallery.setImageResource(data[position].resId)
    }
}