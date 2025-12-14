package com.practice.smallbee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.practice.common.recyclerview.CommonViewHolder
import com.practice.smallbee.databinding.ItemFooterBinding
import com.practice.smallbee.databinding.ItemHeaderBinding
import com.practice.smallbee.databinding.ItemItemBinding

class HeaderFooterRecyclerView : Adapter<CommonViewHolder<*>>() {

    var hasMore = true
    private var data: ArrayList<Any>? = null
    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder<*> {
        inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE.HEADER.ordinal -> CommonViewHolder(ItemHeaderBinding.inflate(inflater, parent, false) as ViewDataBinding)
            TYPE.HEADER.ordinal -> {
                val binding = ItemHeaderBinding.inflate(inflater, parent, false)
                CommonViewHolder(binding as ViewDataBinding) as CommonViewHolder<*>
            }
            TYPE.ITEM.ordinal -> CommonViewHolder(ItemItemBinding.inflate(inflater, parent, false) as ViewDataBinding)
            TYPE.FOOTER.ordinal -> CommonViewHolder(ItemFooterBinding.inflate(inflater, parent, false) as ViewDataBinding)
            else -> throw IllegalArgumentException("未知类型的viewtype类型，viewtype：$viewType")
        }
    }

    override fun getItemCount(): Int {
        if (data.isNullOrEmpty()) {
            return 0//无数据
        } else {
            return if (hasMore) {
                1 + data!!.size + 1//header + items + footer
            } else {
                1 + data!!.size//header + items
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        when {
            //0一定是header
            position == 0 -> return TYPE.HEADER.ordinal
            //有更多数据时最后一个一定是footer
            position == itemCount - 1 && hasMore -> return TYPE.FOOTER.ordinal
            //其余都是item
            else -> return TYPE.ITEM.ordinal
        }
    }

    override fun onBindViewHolder(holder: CommonViewHolder<*>, position: Int) {
        TODO("Not yet implemented")
    }

    enum class TYPE {
        HEADER, ITEM, FOOTER
    }
}