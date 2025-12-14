package com.practice.smallbee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.practice.smallbee.databinding.DatabindingItemBinding
import com.practice.smallbee.viewholder.DatabindingViewHolder
import com.practice.smallbee.viewmodel.DatabindingItemVM

open class DatabindingAdapter(val context: Context) : Adapter<DatabindingViewHolder>() {
    private var viewmodelList: List<DatabindingItemVM> ?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatabindingViewHolder {
        return DatabindingViewHolder(DatabindingItemBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return viewmodelList?.size ?:0
    }

    override fun onBindViewHolder(holder: DatabindingViewHolder, position: Int) {
        holder.bind(viewmodelList?.getOrNull(position))
    }

    fun setViewmodelList(list:List<DatabindingItemVM>){
        viewmodelList = list
    }
}