package com.test.smallbee.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.test.smallbee.adapter.PrivacyAdapter
import com.test.smallbee.databinding.DialogPrivacyBinding
import com.test.smallbee.response.PrivacyResponse


class PrivacyDialog(layoutId : Int) : DialogFragment(layoutId) {
    lateinit var adapter: PrivacyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding: DialogPrivacyBinding = DataBindingUtil.inflate<DialogPrivacyBinding>(
            LayoutInflater.from(context), R.layout.dialog_privacy, container, false
        )
        binding.rvPrivacy.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = PrivacyAdapter(R.layout.item_privacy)
        binding.rvPrivacy.adapter = adapter
        adapter.data = setData()
        return binding.root
    }

    fun setData(): ArrayList<PrivacyResponse> {
        val data: ArrayList<PrivacyResponse> = ArrayList()
        for (i in 0..4) {
            val response: PrivacyResponse = PrivacyResponse()
            response.text = "这是数据mm" + i
            data.add(response)
        }
        return data
    }
}