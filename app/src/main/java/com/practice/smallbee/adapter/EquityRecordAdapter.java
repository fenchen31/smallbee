package com.practice.smallbee.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.practice.smallbee.databinding.ItemEquityRecordEmptyBinding;
import com.practice.smallbee.databinding.ItemEquityRecordItemBinding;
import com.practice.smallbee.databinding.ItemEquityRecordShowMoreBinding;
import com.practice.smallbee.databinding.ItemEquityRecordTitleBinding;
import com.practice.smallbee.response.GemeEquityRecordBean;
import com.practice.smallbee.viewholder.CommonHolder;


public class EquityRecordAdapter extends RecyclerView.Adapter<CommonHolder<ViewBinding>> {

    private static final String TAG = EquityRecordAdapter.class.getSimpleName();
    private GemeEquityRecordBean gamedata;
    private GemeEquityRecordBean trafficData;
    private int gameShowingCount;//游戏权益大item中不包含title+查看全部item
    private int trafficShowingCount;//流量权益大item中不包含title+查看全部item
    private boolean isEmpty;
    //这两个count指实际展示的数量（showall时：标题+items，未showall时：标题+items+查看全部item）
    private int gameDataCount;
    private int trafficDataCount;
    private boolean gameShowAll;
    private boolean trafficShowAll;
    private final int oneIncreaseCount = 10;

    public EquityRecordAdapter() {
        gameShowingCount = 4;
        trafficShowingCount = 4;
    }

    @NonNull
    @Override
    public CommonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecordType type = RecordType.values()[viewType];
        switch (type) {
            case TYPE_TITLE: {
                return new CommonHolder<>(ItemEquityRecordTitleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
            case TYPE_ITEM: {
                return new CommonHolder<>(ItemEquityRecordItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
            case TYPE_SHOW_MORE: {
                return new CommonHolder<>(ItemEquityRecordShowMoreBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
            case TYPE_EMPTY: {
                return new CommonHolder<>(ItemEquityRecordEmptyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CommonHolder holder, int position) {
        ViewBinding viewBinding = holder.getBinding();
        if (viewBinding instanceof ItemEquityRecordTitleBinding) {
            int pos = position - 0;
            if (judgePosition(gamedata, gameShowAll, pos, gameDataCount, gameShowingCount)) {
                setTitleData((ItemEquityRecordTitleBinding) viewBinding, gamedata);
                return;
            }
            pos = position - gameDataCount - 0;
            if (judgePosition(trafficData, trafficShowAll, pos, trafficDataCount, trafficShowingCount)) {
                setTitleData((ItemEquityRecordTitleBinding) viewBinding, trafficData);
            }
        } else if (viewBinding instanceof ItemEquityRecordItemBinding) {
            //计算position的时候应该减去title
            int pos = position - 1 - 0;
            Log.e(TAG, "pos:" + pos);
            if (judgePosition(gamedata, gameShowAll, pos, gameDataCount, gameShowingCount)) {
                setItemData((ItemEquityRecordItemBinding) viewBinding, gamedata.getRecordList().get(pos), pos);
                return;
            }
            pos = position - 1 - gameDataCount - 0;
            if (judgePosition(trafficData, trafficShowAll, pos, trafficDataCount, trafficShowingCount)) {
                setItemData((ItemEquityRecordItemBinding) viewBinding, trafficData.getRecordList().get(pos), pos);
            }
        } else if (viewBinding instanceof ItemEquityRecordShowMoreBinding) {
            setShowMoreData((ItemEquityRecordShowMoreBinding) viewBinding, position);
        } else if (viewBinding instanceof ItemEquityRecordEmptyBinding) {
            setEmptyData((ItemEquityRecordEmptyBinding) viewBinding);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        //游戏权益
        gameDataCount = getItemCount(gamedata, gameShowingCount);
        count += gameDataCount;
        //流量权益
        trafficDataCount = getItemCount(trafficData, trafficShowingCount);
        count += trafficDataCount;
        if (count == 0) {
            isEmpty = true;
            return 1;//无数据页
        } else {
            isEmpty = false;
            return count;
        }
    }

    private boolean showAll(int dataSize, int showingCount) {
        return showingCount == dataSize;
    }

    private int getItemCount(GemeEquityRecordBean data, int showingCount) {
        if (data == null || (data.getRecordList() == null || data.getRecordList().isEmpty())) {
            return 0;
        }
        int result;
        int dataSize = data.getRecordList().size();
        if (dataSize > showingCount) {
            boolean showAll = showAll(dataSize, showingCount);
            if (!showAll) {
                result = 1 + showingCount + 1;//标题 + item + 查看更多
            } else {
                result = 1 + dataSize;//标题 + item
            }
        } else {
            result = 1 + dataSize;//标题 + item
        }
        return result;
    }

    @Override
    public int getItemViewType(int position) {
        if (isEmpty) {
            return RecordType.TYPE_EMPTY.ordinal();
        }
        int pos = position - 0;
        if (judgePosition(gamedata, gameShowAll, pos, gameDataCount, gameShowingCount)) {
            return getRecordType(gamedata, pos, gameShowAll, gameShowingCount).ordinal();
        }
        //    position - 游戏大item总个数 -   （游戏title + 可能有的展示全部item）  - 游戏前面item个数（这个只是作为保留，方便理解）
        pos = position - gameDataCount - 0;
        if (judgePosition(trafficData, trafficShowAll, pos, trafficDataCount, trafficShowingCount)) {
            return getRecordType(trafficData, pos, trafficShowAll, trafficShowingCount).ordinal();
        }
        return -1;
    }

    //根据itemcount和showall判断position是否在当前的大item中，大item目前只有两种，游戏权益和流量权益
    private boolean judgePosition(GemeEquityRecordBean data, boolean focusShowAll, int position, int itemCount, int showingCount) {
        if (itemCount <= 0) {
            return false;
        }
        boolean showAll = showAll(data.getRecordList().size(), showingCount) || focusShowAll;
        if (showAll) {
            return position < itemCount;
        } else {
            if (position < 1 + showingCount + 1) {
                return true;
            }
        }
        return false;
    }

    //根据position和showall判断item类型，这个position是当前item在大item中的位置
    private RecordType getRecordType(GemeEquityRecordBean data, int position, boolean focusShowAll, int showingCount) {
        if (position == 0) {
            return RecordType.TYPE_TITLE;
        }
        boolean showAll = focusShowAll || showAll(data.getRecordList().size(), showingCount);
        if (showAll) {
            return RecordType.TYPE_ITEM;
        }
        if (position < 1 + showingCount) {//标题 + 当前展示的小items
            return RecordType.TYPE_ITEM;
        } else {
            return RecordType.TYPE_SHOW_MORE;
        }
    }

    public boolean judgeLeft(int position) {
        int pos = position - 0;
        if (judgePosition(gamedata, gameShowAll, pos, gameDataCount, gameShowingCount)) {
            return isLeft(gamedata, pos, gameShowAll, gameShowingCount);
        }
        //    position - 游戏大item总个数 -   （游戏title + 可能有的展示全部item）  - 游戏前面item个数（这个只是作为保留，方便理解）
        pos = position - gameDataCount - 0;
        if (judgePosition(trafficData, trafficShowAll, pos, trafficDataCount, trafficShowingCount)) {
            return isLeft(trafficData, pos, trafficShowAll, trafficShowingCount);
        }
        return false;
    }

    private boolean isLeft(GemeEquityRecordBean data, int position, boolean focusShowAll, int showingCount) {
        boolean showAll = focusShowAll || showAll(data.getRecordList().size(), showingCount);
        if (showAll) {
            return (position - 1) % 2 == 0;
        }
        if (position < 1 + showingCount) {//标题 + 当前展示的小items
            return (position - 1) % 2 == 0;
        }
        return false;
    }

    private void setTitleData(ItemEquityRecordTitleBinding b, GemeEquityRecordBean data) {
        b.tvText.setText(TextUtils.isEmpty(data.getName()) ? "" : data.getName());
    }

    private void setItemData(ItemEquityRecordItemBinding b, GemeEquityRecordBean.RecordBean data, int position) {
        b.tvIncreaseMinutes.setText(TextUtils.isEmpty(data.getValidityPeriodDuration()) ? "" : data.getValidityPeriodDuration());
        b.tvName.setText(TextUtils.isEmpty(data.getName()) ? "" : data.getName());
        String time = data.getValidityPeriodStart() + "-" + data.getValidityPeriodEnd();
        b.tvTime.setText(TextUtils.isEmpty(time) ? "" : time);
    }

    private void setShowMoreData(ItemEquityRecordShowMoreBinding b, int position) {
        b.getRoot().setOnClickListener(v -> {
            int pos = position - 0;
            if (judgePosition(gamedata, gameShowAll, pos, gameDataCount, gameShowingCount)) {
                //当前点击为游戏的展示更多
                gameShowingCount = Math.min(gamedata.getRecordList().size(), gameShowingCount + oneIncreaseCount);
                if (gameShowingCount == gamedata.getRecordList().size()) {
                    gameShowAll = true;
                }
                notifyItemRangeChanged(position, getItemCount() - 1);
                return;
            }
            pos = position - gameDataCount - 0;
            if (judgePosition(trafficData, trafficShowAll, pos, trafficDataCount, trafficShowingCount)) {
                //当前点击为流量的展示更多
                trafficShowingCount = Math.min(trafficData.getRecordList().size(), trafficShowingCount + oneIncreaseCount);
                if (trafficShowingCount == trafficData.getRecordList().size()) {
                    trafficShowAll = true;
                }
                notifyItemRangeChanged(position, getItemCount() - 1);
                return;
            }
        });
    }

    private void setEmptyData(ItemEquityRecordEmptyBinding b) {

    }

    public void setGameShowAll(boolean gameShowAll) {
        this.gameShowAll = gameShowAll;
    }

    public void setTrafficShowAll(boolean trafficShowAll) {
        this.trafficShowAll = trafficShowAll;
    }

    public void setGamedata(GemeEquityRecordBean gamedata) {
        this.gamedata = gamedata;
        if (gamedata == null || gamedata.getRecordList() == null || gamedata.getRecordList().size() <= 0) {
            gameShowingCount = 0;
        } else if (gamedata.getRecordList().size() < 4) {
            gameShowingCount = gamedata.getRecordList().size();
        } else {
            gameShowingCount = 4;
        }
        notifyDataSetChanged();
    }

    public void setTrafficData(GemeEquityRecordBean trafficData) {
        this.trafficData = trafficData;
        if (trafficData == null || trafficData.getRecordList() == null || trafficData.getRecordList().size() <= 0) {
            trafficShowingCount = 0;
        } else if (trafficData.getRecordList().size() < 4) {
            trafficShowingCount = trafficData.getRecordList().size();
        } else {
            trafficShowingCount = 4;
        }
        notifyDataSetChanged();
    }

    public enum RecordType {
        TYPE_TITLE,//权益标题
        TYPE_ITEM,//ITEM内容
        TYPE_SHOW_MORE,//查看更多
        TYPE_EMPTY//空数据
    }
}

