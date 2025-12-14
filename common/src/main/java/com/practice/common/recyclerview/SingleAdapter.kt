package com.practice.common.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.Adapter

abstract class SingleAdapter<B : ViewDataBinding, D : Any>(private val layoutId: Int) : Adapter<CommonViewHolder<B>>() {
    
    var data: ArrayList<D> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder<B> {
        return CommonViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CommonViewHolder<B>, position: Int) {
        bindingItem(holder.binding, position, data)
    }

    abstract fun bindingItem(binding: B, position: Int, data: ArrayList<D>)
}