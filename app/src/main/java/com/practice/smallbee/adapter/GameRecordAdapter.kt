package com.practice.smallbee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.practice.common.recyclerview.CommonViewHolder
import com.practice.smallbee.databinding.ItemGameRecordEmptyBinding
import com.practice.smallbee.databinding.ItemGameRecordItemBinding
import com.practice.smallbee.databinding.ItemGameRecordShowMoreBinding
import com.practice.smallbee.databinding.ItemGameRecordTitleBinding
import com.practice.smallbee.response.GemeEquityRecordBean

class GameRecordAdapter : Adapter<CommonViewHolder<*>>() {
    private lateinit var data: GemeEquityRecordBean
    private var showingListCount = 0//显示的list的item数量
    private var showingAllItemCount = 0//显示的全部item数量
    private var oneCreaseCount = 10//点击显示更多后默认加载的数量
    private var focusShowAll = false//是否强制显示全部list的item
    //应该显示的list的item数量，初始值也是第一次进入adapter的默认显示数量,adapter只通过设置它的值来改变显示的item数量
    private var shouldShowListCount = 4
    private var isLoading = false//是否正在加载更多数据

    init {
        showingAllItemCount = 0
        showingListCount = 0
        oneCreaseCount = 10
        shouldShowListCount = 4
        focusShowAll = false
        isLoading = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder<*> {
        when (viewType) {
            ItemType.TYPE_TITLE.ordinal -> return CommonViewHolder(
                ItemGameRecordTitleBinding.inflate(
                    LayoutInflater.from(parent.context)
                )
            )

            ItemType.TYPE_ITEM.ordinal -> return CommonViewHolder(
                ItemGameRecordItemBinding.inflate(
                    LayoutInflater.from(parent.context)
                )
            )

            ItemType.TYPE_SHOW_MORE.ordinal -> return CommonViewHolder(
                ItemGameRecordShowMoreBinding.inflate(
                    LayoutInflater.from(parent.context)
                )
            )

            ItemType.TYPE_EMPTY.ordinal -> return CommonViewHolder(
                ItemGameRecordEmptyBinding.inflate(
                    LayoutInflater.from(parent.context)
                )
            )

            else -> throw IllegalArgumentException("ItemType不存在，请检查getItemViewType()，当前viewType为：$viewType")
        }

    }

    override fun onBindViewHolder(holder: CommonViewHolder<*>, position: Int) {
        when (getItemViewType(position)) {
            ItemType.TYPE_TITLE.ordinal -> {
                val binding = holder.binding as ItemGameRecordTitleBinding
                if (::data.isInitialized) {
                binding.tvText.text = "${data.name}  共有${data.count}个权益"
                }
            }

            ItemType.TYPE_ITEM.ordinal -> {
                val binding = holder.binding as ItemGameRecordItemBinding
                if (::data.isInitialized) {
                    binding.tvName.text = data.recordList[position - 1].name
                    binding.tvTime.text =
                        data.recordList[position - 1].validityPeriodStart + "~" + data.recordList[position - 1].validityPeriodEnd
                }
            }

            ItemType.TYPE_SHOW_MORE.ordinal -> {
                val binding = holder.binding as ItemGameRecordShowMoreBinding
                binding.btShowMore.setOnClickListener {
                    if (::data.isInitialized) {
                        shouldShowListCount += oneCreaseCount
                        if (showingListCount >= data.recordList.size) {
                            //如果listSize小于应该显示的item数量说明没有更多数据，这时候只有title和items
                            shouldShowListCount = data.recordList.size
                        }
                        notifyItemRangeChanged(
                            showingListCount + 1,
                            shouldShowListCount - showingListCount
                        )
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (!::data.isInitialized ||data.recordList.isNullOrEmpty()) {
            //空布局
            showingListCount = 0
            showingAllItemCount = 1//1为空布局
            return showingAllItemCount
        }
        if (data.recordList.size <= shouldShowListCount || focusShowAll) {
            //listSize小于默认数量或者强制显示全部就一次性显示所有item，没有showMore
            showingListCount = data.recordList.size
            showingAllItemCount = showingListCount + 1//1是title
            return showingAllItemCount
        } else {
            if (data.recordList.size > shouldShowListCount) {
                //listSize大于应该显示的item数量说明还有更多数据，这时候需要显示更多
                showingListCount = shouldShowListCount
                showingAllItemCount = 1 + showingListCount + 1//title + items + showMore
                return showingAllItemCount
            } else {
                //listSize小于应该显示的item数量说明没有更多数据，这时候只有title和items
                showingListCount = data.recordList.size
                showingAllItemCount = 1 + showingListCount//title + items
                return showingAllItemCount
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        when {
            itemCount == 1 -> return ItemType.TYPE_EMPTY.ordinal
            position == 0 -> return ItemType.TYPE_TITLE.ordinal
            position < showingListCount + 1 -> return ItemType.TYPE_ITEM.ordinal
            position == showingListCount + 1 -> return ItemType.TYPE_SHOW_MORE.ordinal
            else -> return -1//走到这儿说明算法有问题，所以不应该做任何处理
        }
    }

    fun setData(data: GemeEquityRecordBean) {
        this.data = data
        notifyDataSetChanged()
    }

    enum class ItemType {
        TYPE_TITLE,
        TYPE_ITEM,
        TYPE_SHOW_MORE,
        TYPE_EMPTY
    }
}