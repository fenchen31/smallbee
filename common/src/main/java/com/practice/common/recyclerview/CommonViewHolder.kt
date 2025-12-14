package com.practice.common.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class CommonViewHolder<B : ViewDataBinding>(val binding: B):ViewHolder(binding.root)