package com.practice.smallbee.view

import com.practice.common.recyclerview.SingleAdapter
import com.practice.smallbee.R
import com.practice.smallbee.databinding.ItemGalleryBinding

class GalleryAdapter2 : SingleAdapter<ItemGalleryBinding, Int>(R.layout.item_gallery) {
    override fun bindingItem(binding: ItemGalleryBinding, position: Int, data: ArrayList<Int>) {
        binding.ivGallery.setImageResource(data[position])
    }
}