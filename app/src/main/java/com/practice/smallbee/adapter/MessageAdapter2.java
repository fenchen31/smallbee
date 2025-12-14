package com.practice.smallbee.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practice.common.recyclerview.CommonViewHolder;
import com.practice.smallbee.databinding.ItemMessageBinding;
import com.practice.smallbee.response.MessageResponseItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liliangliang
 * 消息中心adapter
 */
public class MessageAdapter2 extends RecyclerView.Adapter<CommonViewHolder<ItemMessageBinding>> {


    private static final String TAG = MessageAdapter2.class.getSimpleName();

    private List<MessageResponseItem> data;
    private OnItemSelectListener listener;
    private OnItemReadListener readListener;
    private boolean selectVisiable;

    @NonNull
    @Override
    public CommonViewHolder<ItemMessageBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommonViewHolder<>(ItemMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder<ItemMessageBinding> holder, int position) {
        ItemMessageBinding binding = holder.getBinding();
        MessageResponseItem item = data.get(position);
         setData(binding, data.get(position), position);
    }

    private void setData(ItemMessageBinding binding, MessageResponseItem item, int position) {
        binding.cbSelect.setVisibility(selectVisiable ? VISIBLE : GONE);
        binding.cbSelect.setChecked(item.getSelect());
        binding.viewRedPoint.setVisibility(item.getStatus() == 0 ? VISIBLE : GONE);
        setResult(item.getType(), binding.tvResult);
        binding.tvDetail.setText(item.getContent());
        binding.getRoot().setOnClickListener(v -> {
            if (selectVisiable) {
                item.setSelect(!item.getSelect());
                binding.cbSelect.setChecked(item.getSelect());
                notifyItemChanged(position);
                if (listener != null){
                    listener.onChange();
                }
            } else {
                if (readListener != null) {
                    readListener.onItemRead(position, data);
                }
                Dialog dialog = new Dialog(v.getContext());
                dialog.show();
            }
        });
    }

    private void setResult(int result, TextView view) {
        //1-设备告警，2-连接成功，3-版本更新，4-删除设备，5-新品上架',
        if (result == 1) {
            view.setText("设备告警");
        } else if (result == 2) {
            view.setText("连接成功");
        } else if (result == 3) {
            view.setText("版本更新");
        } else if (result == 4) {
            view.setText("删除设备");
        } else if (result == 5) {
            view.setText("新品上架");
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setData(List<MessageResponseItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(List<MessageResponseItem> data, RecyclerView view) {
        if (data == null) {
            return;
        }
        if (this.data == null) {
            this.data = data;
            notifyDataSetChanged();
        } else {
            int start = data.size() - 1;
            this.data.addAll(data);
            notifyItemRangeChanged(start, data.size() - start);
        }
    }

    public boolean isSelectAll() {
        if (data == null || data.isEmpty()) {
            return false;
        }
        for (MessageResponseItem item : data) {
            if (!item.getSelect()) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Integer> getSelectIds(){
        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getSelect()){
                list.add(data.get(i).getId());
            }
        }
        return list;
    }

    public void setSelectVisiable(boolean selectVisiable) {
        this.selectVisiable = selectVisiable;
        notifyDataSetChanged();
    }

    public List<MessageResponseItem> getData() {
        return data;
    }
    public void setListener(OnItemSelectListener listener) {
        this.listener = listener;
    }

    public void setReadListener(OnItemReadListener readListener) {
        this.readListener = readListener;
    }

    public interface OnItemReadListener {
        void onItemRead(int position, List<MessageResponseItem> data);
    }
    public interface OnItemSelectListener {
        void onChange();
    }

}