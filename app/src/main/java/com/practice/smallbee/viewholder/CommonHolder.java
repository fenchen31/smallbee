package com.practice.smallbee.viewholder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public class CommonHolder<T extends ViewBinding> extends RecyclerView.ViewHolder{

    private final T binding;
    public CommonHolder(@NonNull T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public T getBinding() {
        return  binding;
    }
}
