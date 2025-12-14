package com.practice.smallbee.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.annotation.Route
import com.practice.smallbee.R
import com.practice.smallbee.adapter.EquityRecordAdapter
import com.practice.smallbee.adapter.GameRecordAdapter
import com.practice.smallbee.databinding.ActivityMultiRecyclerViewBinding
import com.practice.smallbee.decoration.EquityRecordDecoration
import com.practice.smallbee.response.GemeEquityRecordBean
import com.practice.smallbee.response.GemeEquityRecordBean.RecordBean
import com.practice.smallbee.viewmodel.MultiRecyclerViewViewModel
import com.practice.smallbee.viewmodel.MultiSimpleRecyclerViewViewModel

@Route("app/MultiRecyclerViewActivity")
class MultiRecyclerViewActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMultiRecyclerViewBinding.inflate(layoutInflater) }
    private val vm by lazy { MultiRecyclerViewViewModel(adapter) }
    private val adapter by lazy { EquityRecordAdapter() }
    private val simpleAdapter by lazy { GameRecordAdapter() }
    private val simpleVM by lazy { MultiSimpleRecyclerViewViewModel(simpleAdapter) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initSimple()
        //initGame()
    }

    private fun initSimple(){
        simpleVM.loadData()
        binding.llSimple.visibility = View.VISIBLE
        binding.llGame.visibility = View.GONE
        binding.rvContent.adapter = simpleAdapter
        binding.rvContent.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun initGame(){
        vm.loadData()
        binding.rvContent.adapter = adapter
        binding.llGame.visibility = View.VISIBLE
        binding.llSimple.visibility = View.GONE
        binding.btEmpty.setOnClickListener { vm.deleteAlldata() }
        binding.btGameDelete.setOnClickListener { vm.deleteGameData() }
        binding.btGameIncrease.setOnClickListener { vm.gameIncrease() }
        binding.btTrafficIncrease.setOnClickListener { vm.trafficIncrease() }
        binding.btTrafficDelete.setOnClickListener { vm.trafficDelete() }
        val manager = GridLayoutManager(this, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (adapter.getItemViewType(position) == EquityRecordAdapter.RecordType.TYPE_ITEM.ordinal){
                    return 1
                } else {
                    return 2
                }
            }
        }
        binding.rvContent.addItemDecoration(EquityRecordDecoration())
        binding.rvContent.layoutManager = manager
    }
}