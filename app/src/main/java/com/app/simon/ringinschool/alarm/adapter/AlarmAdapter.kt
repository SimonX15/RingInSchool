package com.app.simon.ringinschool.alarm.adapter

import android.content.Context
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
        //兼容性问题，在5.0以上，需要设置clipToOutline
        //        val cardView: CardView? = helper?.getView(R.id.cv_alarm)
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //            cardView?.clipToOutline = false
        //        }
        if (item == null) {
            return
        }
        val tvHour: TextView? = helper?.getView(R.id.tv_hour)
        val tvMinute: TextView? = helper?.getView(R.id.tv_minute)

        //        helper?.addOnClickListener(R.id.fl_alarm_operation)

        item.run {
            var hourString = hourOfDay.toString()
            if (hourOfDay < 10) {
                hourString = "0$hourString"
            }
            tvHour!!.text = hourString

            var minuteString = minute.toString()
            if (minute < 10) {
                minuteString = "0$minuteString"
            }
            tvMinute!!.text = minuteString
        }
    }
}