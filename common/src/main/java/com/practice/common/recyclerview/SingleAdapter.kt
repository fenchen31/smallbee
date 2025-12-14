package com.practice.common.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.Adapter

abstract class SingleAdapter<B : ViewDataBinding, D : Any>(private val layoutId: Int) : Adapter<CommonViewHolder<B>>() {

    lateinit var context:Context
    var data: ArrayList<D> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder<B> {
        context = parent.context
        return CommonViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CommonViewHolder<B>, position: Int) {
        bindingItem(holder.binding, position, data)
    }

    abstract fun bindingItem(binding: B, position: Int, data: ArrayList<D>)
}