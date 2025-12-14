package com.practice.smallbee.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

object BindingCoordinatorlayoutAdapter {
    @BindingAdapter("app:adapter")
    @JvmStatic
    fun setAdapter(recyclerView: RecyclerView, adapter: Adapter<*>){
        recyclerView.adapter = adapter
    }
}