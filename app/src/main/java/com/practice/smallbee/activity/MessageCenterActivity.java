package com.practice.smallbee.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.practice.annotation.Route;
import com.practice.common.util.ClickUtil;
import com.practice.smallbee.adapter.MessageAdapter2;
import com.practice.smallbee.databinding.ActivityMessageCenterBinding;
import com.practice.smallbee.response.MessageResponse;
import com.practice.smallbee.response.MessageResponseItem;
import com.practice.smallbee.viewmodel.MessageCenterVM;

import java.util.ArrayList;
import java.util.List;

@Route(path = "app/MessageCenterActivity")
public class MessageCenterActivity extends AbstractActivity<ActivityMessageCenterBinding> {

    private static final String TAG = MessageCenterActivity.class.getSimpleName();
    private MessageAdapter2 adapter;
    private ArrayList<Integer> selectIds;
    private ArrayList<Integer> waitingDeleteIds;
    private ArrayList<Integer> waitingReadIds;

    private int pageNum = 1;
    private int pageSize = 10;
    private MessageResponse data = null;
    private MessageCenterVM viewModel;

    @Override
    public void initView() {
        viewModel = new ViewModelProvider(this).get(MessageCenterVM.class);
        binding.refresh.setOnRefreshListener(refreshLayout -> {
            reset();
        });
        binding.refresh.setOnLoadMoreListener(refreshLayout -> {
            pageNum ++;
            initData();
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter = new MessageAdapter2());
        binding.cbSelect.setOnClickListener(v -> {
            viewModel.changeData(binding.cbSelect.isChecked());
        });
        viewModel.getData().observe(this, response -> {
            data = response;
            if (data == null || response.getList().isEmpty()){
                showEmpty();
            } else {
                adapter.setData(response.getList());
                setCheckBoxStatus();
            }
        });
        viewModel.getLoadFinished().observe(this, loadFinished->{
            if (loadFinished){
                binding.refresh.finishLoadMore();
                binding.refresh.finishRefresh();
            }
        });
        binding.tvBatchDelete.setOnClickListener(v -> {
            if (ClickUtil.Companion.isFastClick(500)){
                return;
            }
            showSelect(true);
            adapter.setSelectVisiable(true);
        });
        binding.tvCancel.setOnClickListener(v -> {
            showSelect(false);
        });
        adapter.setListener(()-> {
            for (int i = 0; i < adapter.getData().size(); i++){
                if (!adapter.getData().get(i).getSelect()){
                    binding.cbSelect.setChecked(false);
                    return;
                }
            }
            binding.cbSelect.setChecked(true);
        });
    }

    @Override
    public void initData() {
        viewModel.loadData(pageNum);
    }
    private void reset(){
        pageNum = 1;
        pageSize = 10;
        initData();
    }
    private void setCheckBoxStatus(){
        if (data == null){
            binding.cbSelect.setChecked(false);
            return;
        }
        for (int i = 0; i < data.getList().size(); i++) {
            if (!data.getList().get(i).getSelect()){
                binding.cbSelect.setChecked(false);
                return;
            }
        }
        binding.cbSelect.setChecked(true);
    }
    private void showEmpty(){
        binding.llEmpty.setVisibility(VISIBLE);
        binding.llClick.setVisibility(GONE);
        binding.llSelect.setVisibility(GONE);
    }
    private void showSelect(boolean  show){
        if (show) {
            binding.llSelect.setVisibility(VISIBLE);
            binding.llClick.setVisibility(GONE);
        } else {
            binding.llClick.setVisibility(VISIBLE);
            binding.llSelect.setVisibility(GONE);
            binding.cbSelect.setChecked(false);
        }
        resetItemsStatus(show);
    }

    private void resetItemsStatus(boolean show){
        for (int i = 0; i < adapter.getData().size(); i++){
            adapter.getData().get(i).setSelect(false);
        }
        adapter.setSelectVisiable(show);
        adapter.notifyDataSetChanged();
    }
}