package com.test.smallbee.base;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * 多布局的recyclerview的adapter
 * 1.所有添加到数据都将被转化成数据再添加到recyclerview当中，因此在使用时必须将对象从list再降解回指定对象再使用
 * 2.setItemData(int position, @NonNull Object newData)只是一个方法冗余，用于重置position位置的数据，前提是该position之前已经被设置过layoutId
 *      如果没有被设置过layoutId，应该先调用
 */
public abstract class CommonAdapter extends RecyclerView.Adapter<SimpleViewHolder> {
    public Context context;
    private static final String TAG = CommonAdapter.class.getSimpleName();
    private ArrayList<Integer> layoutIds = new ArrayList<>();
    private List<Object> datas = new ArrayList<>();

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        context = view.getRoot().getContext();
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        bindItemData(holder.getBinding(), (List<Object>) datas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return layoutIds.size();
    }

    @Override
    public int getItemViewType(int position) {
        return layoutIds.get(position);
    }

    public void setItemData(@IdRes int layoutId, @NonNull Object data){
        layoutIds.add(layoutId);
        datas.add(changeToList(data));
        notifyItemChanged(datas.size() - 1);
    }

    public void setItemData(@NonNull Object newData, int position){
        if (position > layoutIds.size() - 1){
            throw new RuntimeException(String.format("The data position to be set exceeds the longth of the array, you want to set position is %d, but the array length is %d", position, layoutIds.size() - 1));
        }
        newData = changeToList(newData);
        datas.set(position, newData);
        notifyItemChanged(position);
    }

    public void addItemData(@NonNull Object data, int position){
        if (position > layoutIds.size() - 1){
            throw new RuntimeException(String.format("The data position to be set exceeds the longth of the array, you want to set position is %d, but the array length is %d", position, layoutIds.size() - 1));
        }
        //确保position位置的原数据一定为数组
        List<Object> originData = changeToList(datas.get(position));
        datas.set(position, originData);
        //确保新添加的数据一定为数组
        List<Object> newData = changeToList(data);
        for (int i = 0; i < newData.size(); i++) {
            ((List<Object>)datas.get(position)).add(newData.get(i));
        }
        notifyItemChanged(position);
    }

    public void addItemData(@IdRes int layoutId, @NonNull Object data){
        List<Object> addData = changeToList(data);
        for (int i = 0; i < layoutIds.size(); i++) {
            if (layoutIds.get(i) == layoutId){
                for (int j = 0; j < addData.size(); j++) {
                    List<Object> originData = changeToList(datas.get(i));
                    originData.add(addData.get(j));
                }
                notifyItemChanged(i);
                return;
            }
        }
        Log.e(TAG, "no this type layoutId");
    }
    public void addDataToLast(@IdRes int layoutId, @NonNull Object data){
        data = changeToList(data);
        layoutIds.add(layoutId);
        datas.add(data);
        notifyItemChanged(datas.size() - 1);
    }

    //将对象转化成数组
    public List<Object> changeToList(@NonNull Object data){
        if (!(data instanceof List)){
            List<Object> newData = new ArrayList<>();
            newData.add(data);
            return newData;
        } else {
            return (List<Object>) data;
        }
    }

    public abstract void bindItemData(ViewBinding dataBinding, List<Object> itemData, int position);
}
