package com.app.simon.ringinschool.alarm.models

import java.text.SimpleDateFormat
import java.util.*

/**
 * desc: 闹钟时间
 * date: 2018/1/3
 *
 * @author xw
 */
data class Alarm(
        /** mills */
        var timeInMills: Long = 0,
        /** 小时 */
        var hourOfDay: Int = 0,
        /** 分钟 */
        var minute: Int = 0,
        /** requestCode，用于闹钟设置 */
        var requestCode: Int = 0,
        /** 是否打开，打开就要响铃 */
        var isOpening: Boolean = false
) {
    override fun toString(): String {
        return transDate(timeInMills) + "——Alarm(timeInMills=$timeInMills, hourOfDay=$hourOfDay, minute=$minute, requestCode=$requestCode, isOpening=$isOpening)\n"
    }

    private fun transDate(timeInMills: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val chooseDate = Date(timeInMills)
        return sdf.format(chooseDate)
    }
}