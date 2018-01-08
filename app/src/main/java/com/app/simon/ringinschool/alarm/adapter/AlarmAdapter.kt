package com.app.simon.ringinschool.alarm.adapter

import android.content.Context
import android.support.v7.widget.SwitchCompat
import android.widget.TextView
import com.app.simon.ringinschool.R
import com.app.simon.ringinschool.alarm.models.Alarm
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * desc: AlarmAdapter
 * date: 2018/1/3
 *
 * @author xw
 */
class AlarmAdapter(val context: Context, data: MutableList<Alarm>) : BaseQuickAdapter<Alarm, BaseViewHolder>(R.layout.item_alarm, data) {

    override fun convert(helper: BaseViewHolder?, item: Alarm?) {
        if (item == null) {
            return
        }
        val tvTime: TextView? = helper?.getView(R.id.tv_time)
        val tvIsOpening: TextView? = helper?.getView(R.id.tv_is_opening)
        val switchCompat: SwitchCompat? = helper?.getView(R.id.switch_compat)

        helper?.addOnClickListener(R.id.tv_time)
        helper?.addOnClickListener(R.id.switch_compat)

        item.apply {
            tvTime!!.text = standardTime()

            switchCompat!!.isChecked = isOpening

            /*tvIsOpening!!.text = if (isOpening) {
                "已开启"
            } else {
                "已关闭"
            }*/
        }
    }
}