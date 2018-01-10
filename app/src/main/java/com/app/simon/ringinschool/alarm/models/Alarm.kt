package com.app.simon.ringinschool.alarm.models

import io.realm.RealmModel
import io.realm.annotations.RealmClass
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * desc: 闹钟时间
 * date: 2018/1/3
 *
 * @author xw
 */
@RealmClass
open class Alarm(
        /** 小时 */
        var hourOfDay: Int = 0,
        /** 分钟 */
        var minute: Int = 0,
        /** mills */
        var timeInMills: Long = 0,
        /** requestCode，用于闹钟设置 */
        var requestCode: Int = 0,
        /** 是否打开，打开就要响铃 */
        var isOpening: Boolean = true,
        /** 铃声类型 */
        var alarmType: Int? = null

) : RealmModel {
    /** 上课铃 */
    val TYPE_START = 0
    /** 下课铃 */
    val TYPE_END = 1
    /** 恩典之歌 */
    val TYPE_GRACE = 2

    /** 标准时间 */
    fun standardTime(): String {
        val df = DecimalFormat("00")  //保留两位数，如果不足两位则自动补零
        return df.format(hourOfDay) + " : " + df.format(minute)
    }

    override fun toString(): String {
        return transDate(timeInMills) + " —— Alarm(hourOfDay=$hourOfDay, minute=$minute, timeInMills=$timeInMills, requestCode=$requestCode, isOpening=$isOpening, alarmType=$alarmType)"
    }

    private fun transDate(timeInMills: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val chooseDate = Date(timeInMills)
        return sdf.format(chooseDate)
    }
}

