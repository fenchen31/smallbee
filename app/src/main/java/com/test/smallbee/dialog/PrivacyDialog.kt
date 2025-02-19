package com.test.smallbee.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.smallbee.activity.MainActivity
import com.test.smallbee.activity.SplashActivity
import com.test.smallbee.adapter.PrivacyMultiAdapter
import com.test.smallbee.app.R
import com.test.smallbee.app.databinding.DialogPrivacyBinding
import com.test.smallbee.response.PrivacyResponse

class PrivacyDialog(val layoutId: Int) : DialogFragment() {
    lateinit var binding: DialogPrivacyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<DialogPrivacyBinding>(
            LayoutInflater.from(context), layoutId, container, false
        )
        binding.rvPrivacy.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val commonAdapter = PrivacyMultiAdapter()
        binding.rvPrivacy.adapter = commonAdapter
        val data = setData()
        for (i in 0..data.data.size - 1) {
            val layoutId = when {
                i == 0 -> R.layout.item_privacy_title
                else -> R.layout.item_privacy_content
            }
            commonAdapter.setItemData(layoutId, data.data.get(i))
        }
        binding.btnAgree.setOnClickListener {
            startActivity(
                Intent(
                    context, MainActivity::class.java
                )
            )
        }
        binding.btnDoNotAgree.setOnClickListener { (context as SplashActivity).finish() }
        isCancelable = false
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val window = it.window ?: return
            //1.去除dialog默认的白色背景
            window.decorView.setBackgroundResource(android.R.color.transparent)
            //2.重置dialog宽高
            val params = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = params
            //3.使用viewTreeObserver设置margin，兼容Android 11及更高版本
            val margin = (10f * resources.displayMetrics.density + 0.5f).toInt()
            window.decorView.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val contentView = (window.decorView as ViewGroup).getChildAt(0)
                    if (contentView.layoutParams is ViewGroup.MarginLayoutParams) {
                        val marginParams = contentView.layoutParams as ViewGroup.MarginLayoutParams
                        marginParams.setMargins(margin, 0, margin, 0)
                        contentView.layoutParams = marginParams
                    } else {
                        val marginParams = ViewGroup.MarginLayoutParams(contentView.layoutParams)
                        marginParams.setMargins(margin, 0, margin, 0)
                        val parent = contentView.parent as? ViewGroup
                        parent?.updateViewLayout(contentView, marginParams)
                    }
                    contentView.requestLayout()
                }
            })
        }
    }

    fun setData(): PrivacyResponse {
        val data = PrivacyResponse(ArrayList())
        data.data.add(
            PrivacyResponse.PrivacyItem(
                arrayListOf(PrivacyResponse.PrivacyItem.PrivacyItemChild(text = "我们如何收集、处理个人信息。"))
            )
        )
        data.data.add(
            PrivacyResponse.PrivacyItem(
                arrayListOf(
                    PrivacyResponse.PrivacyItem.PrivacyItemChild(text = "1.你可以查看完整版"),
                    PrivacyResponse.PrivacyItem.PrivacyItemChild(
                        "《用户服务协议》",
                        context?.getColor(R.color.color_D12424).toString(),
                        "https://www.baidu.com"
                    ),
                    PrivacyResponse.PrivacyItem.PrivacyItemChild(text = "奥德赛来看我儿子跑去看拉屎都开始爱上对方克拉拉；啊")
                )
            )
        )
        data.data.add(
            PrivacyResponse.PrivacyItem(
                arrayListOf(
                    PrivacyResponse.PrivacyItem.PrivacyItemChild(text = "2.根据上两节课氟利昂，同意"),
                    PrivacyResponse.PrivacyItem.PrivacyItemChild(
                        "《基本功能数据处理规则》",
                        context?.getColor(R.color.color_D12424).toString(),
                        "https://www.baidu.com/s?wd=%E6%96%87%E5%BF%83%E4%B8%80%E8%A8%80%E5%9C%A8%E7%BA%BF%E4%BD%BF%E7%94%A8&rsv_spt=1&rsv_iqid=0xdd6646730023858c&issp=1&f=3&rsv_bp=1&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_dl=ts_0&rsv_enter=1&rsv_sug3=6&rsv_sug1=6&rsv_sug7=100&rsv_sug2=0&rsv_btype=i&prefixsug=wenxin&rsp=0&inputT=2610&rsv_sug4=2610&rsv_sug=1"
                    ),
                    PrivacyResponse.PrivacyItem.PrivacyItemChild(
                        text = "，仅代表年地方看爱上了大家快去微弱啊说了的空间发的发生的尺码没抵抗力强诶偶然的积分卡的发生的快乐" + "仅代表年地方看爱上了大家快去微弱啊说了的空间发的发生的尺码没抵抗力强诶偶然的积分卡的发生的快乐" + "仅代表年地方看爱上了大家快去微弱啊说了的空间发的发生的尺码没抵抗力强诶偶然的积分卡的发生的快乐" + "仅代表年地方看爱上了大家快去微弱啊说了的空间发的发生的尺码没抵抗力强诶偶然的积分卡的发生的快乐"
                    ),
                    PrivacyResponse.PrivacyItem.PrivacyItemChild(
                        "《快点拉拉草莓》",
                        context?.getColor(R.color.color_D12424).toString(),
                        "https://www.aliyun.com"
                    ),
                )
            )
        )
        data.data.add(
            PrivacyResponse.PrivacyItem(
                arrayListOf(
                    PrivacyResponse.PrivacyItem.PrivacyItemChild(text = "3.请问哦as居民，同意"),
                    PrivacyResponse.PrivacyItem.PrivacyItemChild(
                        "《隐私政策》",
                        context?.getColor(R.color.color_D12424).toString(),
                        "https://www.baidu.com"
                    ),
                    PrivacyResponse.PrivacyItem.PrivacyItemChild(
                        text = "，亲我亲我看手机的女女安安啦是的我啊是哦亲" + "亲我亲我看手机的女女安安啦是的我啊是哦亲" + "亲我亲我看手机的女女安安啦是的我啊是哦亲" + "亲我亲我看手机的女女安安啦是的我啊是哦亲"
                    )
                )
            )
        )
        data.data.add(
            PrivacyResponse.PrivacyItem(
                arrayListOf(
                    PrivacyResponse.PrivacyItem.PrivacyItemChild(
                        text = "4.看来技巧的风景快乐，阿里斯顿服了；求人情况骄傲；的发生的玛莎拉蒂去爬山打开" + "来技巧的风景快乐，阿里斯顿服了；求人情况骄傲；的发生的" + "来技巧的风景快乐，阿里斯顿服了；求人情况骄傲；的发生的" + "来技巧的风景快乐，阿里斯顿服了；求人情况骄傲；的发生的"
                    )
                )
            )
        )
        return data
    }
}