package com.practice.smallbee.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.practice.smallbee.response.UseRecodeBean;
import com.practice.smallbee.viewholder.CommonHolder;

import java.util.List;

public class UseRecordAdapter extends RecyclerView.Adapter<CommonHolder<ViewBinding>> {
    private UseRecodeBean data;
    private static final int TITLE = 1;
    private static final int SHOW_MORE = 1;
    private List<UseRecodeBean.UseRecodeItemBean> listData;
    private boolean focusShowAll;
    private int allShowingCount;
    private int showListCount;
    @NonNull
    @Override
    public CommonHolder<ViewBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CommonHolder<ViewBinding> holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (data == null || data.getUseRecodeItem() == null || data.getUseRecodeItem().isEmpty()){
            return 1;
        }
        if (allShowingCount <= listData.size() + TITLE){
            return listData.size() + TITLE;
        } else {
            return listData.size() + TITLE + SHOW_MORE;
        }
    }


    enum ViewType {
        TYPE_TITLE,
        TYPE_ITEM,
        TYPE_EMPTY,
        TYPE_MORE
    }
}
