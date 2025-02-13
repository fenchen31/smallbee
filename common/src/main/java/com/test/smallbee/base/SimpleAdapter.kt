package com.test.smallbee.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewbinding.ViewBinding

abstract class SimpleAdapter<T, B : ViewBinding>(var layoutId: Int) : Adapter<SimpleViewHolder>() {
    var data: ArrayList<T> = arrayListOf<T>()
        set(value){
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        return SimpleViewHolder(
            DataBindingUtil.bind<ViewDataBinding?>(
                LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size;
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        bindItemData(holder.binding as B, data[position], position)
    }

    fun addData(addData: ArrayList<T>) {
        this.data.addAll(addData)
        notifyItemRangeChanged(data.size - addData.size, data.size)
    }

    abstract fun bindItemData(binding: B, data: T, position: Int);
}