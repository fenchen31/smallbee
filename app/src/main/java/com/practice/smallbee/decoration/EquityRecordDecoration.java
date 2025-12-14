package com.practice.smallbee.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.practice.smallbee.adapter.EquityRecordAdapter;

public class EquityRecordDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
        EquityRecordAdapter adapter = (EquityRecordAdapter) parent.getAdapter();
        if (manager == null || adapter == null){
            return;
        }
        int position = parent.getChildAdapterPosition(view);
        if (adapter.getItemViewType(position) == EquityRecordAdapter.RecordType.TYPE_TITLE.ordinal()){
            outRect.set(100, 0, 0, 0);
        } else if (adapter.getItemViewType(position) == EquityRecordAdapter.RecordType.TYPE_ITEM.ordinal()){
            if (adapter.judgeLeft(position)){
                outRect.set(150, 0, 0, 0);
            } else {
                outRect.set(20, 0, 0, 0);
            }
        } else if (adapter.getItemViewType(position) == EquityRecordAdapter.RecordType.TYPE_SHOW_MORE.ordinal()) {

        }
    }
}
