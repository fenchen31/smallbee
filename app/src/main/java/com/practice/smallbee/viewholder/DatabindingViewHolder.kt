package com.practice.smallbee.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.practice.smallbee.databinding.DatabindingItemBinding
import com.practice.smallbee.viewmodel.DatabindingItemVM

class DatabindingViewHolder(val binding: DatabindingItemBinding) : ViewHolder(binding.root) {
    fun bind(vm: DatabindingItemVM?){
        binding.vm = vm
        binding.executePendingBindings()//触发绑定，更新UI
    }
}