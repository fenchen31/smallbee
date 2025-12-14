package com.practice.smallbee.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.practice.smallbee.R
import com.practice.smallbee.databinding.ItemGalleryBinding

class GalleryAdapter(@DrawableRes private val data: ArrayList<Int>) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    private val TAG: String = "GalleryAdapter"
    private var viewPager: ViewPager2? = null

    class GalleryViewHolder(val binding: ItemGalleryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        // 获取ViewPager2实例
        viewPager = recyclerView.parent as? ViewPager2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = DataBindingUtil.inflate<ItemGalleryBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_gallery,
            parent,
            false
        )
        return GalleryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.binding.ivGallery.setImageResource(data[position])
    }
}