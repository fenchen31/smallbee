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
import com.practice.smallbee.databinding.ItemGameRecordItemBinding;
import com.practice.smallbee.databinding.ItemGameRecordShowMoreBinding;
import com.practice.smallbee.databinding.ItemGameRecordTitleBinding;
import com.practice.smallbee.databinding.ItemTrafficRecordItemBinding;
import com.practice.smallbee.databinding.ItemTrafficRecordShowMoreBinding;
import com.practice.smallbee.databinding.ItemTrafficRecordTitleBinding;
import com.practice.smallbee.response.GemeEquityRecordBean;
import com.practice.smallbee.viewholder.CommonHolder;


public class EquityRecordAdapter extends RecyclerView.Adapter<CommonHolder<ViewBinding>> {

    private static final String TAG = EquityRecordAdapter.class.getSimpleName();
    private GemeEquityRecordBean gamedata;
    private GemeEquityRecordBean trafficData;
    private int gameShowingCount;
    private int trafficShowingCount;
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
            case TYPE_GAME_TITLE: {
                return new CommonHolder<>(ItemEquityRecordTitleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
            case TYPE_GAME_ITEM: {
                return new CommonHolder<>(ItemEquityRecordItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
            case TYPE_GAME_SHOW_MORE: {
                return new CommonHolder<>(ItemEquityRecordShowMoreBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
            case TYPE_TRAFFIC_TITLE: {
                return new CommonHolder<>(ItemEquityRecordTitleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
            case TYPE_TRAFFIC_ITEM: {
                return new CommonHolder<>(ItemEquityRecordItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
            case TYPE_TRAFFIC_SHOW_MORE: {
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
        if (viewBinding instanceof ItemGameRecordTitleBinding){
            int pos = position - 0;
            if (judgePosition(gamedata, gameShowAll, pos, gameDataCount, gameShowingCount)) {
                setTitleData((ItemGameRecordTitleBinding) viewBinding, gamedata);
                return;
            }
        } else if (viewBinding instanceof ItemGameRecordItemBinding){

        } else if (viewBinding instanceof ItemGameRecordShowMoreBinding) {

        } else if (viewBinding instanceof ItemTrafficRecordTitleBinding) {

        } else if (viewBinding instanceof ItemTrafficRecordItemBinding) {

        } else if (viewBinding instanceof ItemTrafficRecordShowMoreBinding) {

        } else if (viewBinding instanceof ItemEquityRecordEmptyBinding) {

        }
        if (viewBinding instanceof ItemGameRecordTitleBinding) {
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
            int pos = position - 0;
            Log.e(TAG, "pos:" + pos);
            if (judgePosition(gamedata, gameShowAll, pos, gameDataCount, gameShowingCount)) {
                setItemData((ItemEquityRecordItemBinding) viewBinding, gamedata.getRecordList().get(pos), pos);
                return;
            }
            pos = position - gameDataCount - 0;
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
            return count;
        }
    }

    private boolean showAll(int dataSize, int showingCount) {
        return Math.min(showingCount + oneIncreaseCount, dataSize) == dataSize;
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
            return getRecordType(true, gamedata, pos, gameShowAll, gameShowingCount).ordinal();
        }
        pos = position - gameDataCount - 0;
        if (judgePosition(trafficData, trafficShowAll, pos - gameDataCount, trafficDataCount, trafficShowingCount)) {
            return getRecordType(false,trafficData, pos, trafficShowAll, trafficShowingCount).ordinal();
        }
        return -1;
    }

    //根据itemcount和showall判断position是否在当前的大item中，大item目前只有两种，游戏权益和流量权益
    private boolean judgePosition(GemeEquityRecordBean data, boolean focusShowAll, int position, int itemCount, int showingCount) {
        if (itemCount <= 0) {
            return false;
        }
        boolean showAll = showAll(data.getRecordList().size(), showingCount) || focusShowAll;
        //超过正在展示的item数时
        if (itemCount > 1 + showingCount) {
            if (showAll) {
                if (position < itemCount) {
                    return true;
                }
            } else {
                //未showall时：标题+正在展示的item个数+查看更多
                if (position < 1 + showingCount + 1) {
                    return true;
                }
            }
        } else {
            if (position < itemCount) {
                return true;
            }
        }
        return false;
    }

    //根据position和showall判断item类型，这个position是当前item在大item中的位置
    private RecordType getRecordType(boolean game, GemeEquityRecordBean data, int position, boolean focusShowAll, int showingCount) {
        if (position == 0) {
            if (game)
                return RecordType.TYPE_GAME_TITLE;
            else
                return RecordType.TYPE_TRAFFIC_TITLE;
        }
        boolean showAll = focusShowAll || showAll(data.getRecordList().size(), showingCount);
        if (showAll) {
            if (game)
                return RecordType.TYPE_GAME_ITEM;
            else
                return RecordType.TYPE_TRAFFIC_ITEM;
        }
        if (position < 1 + showingCount) {//标题 + 当前展示的小items
            if (game)
                return RecordType.TYPE_GAME_ITEM;
            else
                return RecordType.TYPE_TRAFFIC_ITEM;
        } else {
            if (game)
                return RecordType.TYPE_GAME_SHOW_MORE;
            else
                return RecordType.TYPE_TRAFFIC_SHOW_MORE;
        }

    }

    private void setGameTitleData(ItemGameRecordTitleBinding b, GemeEquityRecordBean data) {
        b.tvText.setText(TextUtils.isEmpty(data.getName()) ? "" : data.getName());
    }
    private void setTrafficTitleData(ItemGameRecordTitleBinding b, GemeEquityRecordBean data) {
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
    }

    public void setTrafficData(GemeEquityRecordBean trafficData) {
        this.trafficData = trafficData;
    }

    enum RecordType {
        TYPE_GAME_TITLE,//游戏权益标题
        TYPE_TRAFFIC_TITLE,//流量权益标题
        TYPE_GAME_ITEM,//游戏ITEM内容
        TYPE_TRAFFIC_ITEM,//流量ITEM内容
        TYPE_GAME_SHOW_MORE,//游戏查看更多
        TYPE_TRAFFIC_SHOW_MORE,//流量查看更多
        TYPE_EMPTY//空数据
    }
}

