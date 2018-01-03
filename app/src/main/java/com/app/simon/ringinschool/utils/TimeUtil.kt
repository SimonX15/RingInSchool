package com.app.simon.ringinschool.utils

import android.util.Log
import com.app.simon.ringinschool.App
import com.app.simon.ringinschool.alarm.models.Alarm
import java.text.SimpleDateFormat
import java.util.*

/**
 * desc: 闹钟工具
 * date: 2018/1/3
 *
 * @author xw
 */
object TimeUtil {
    private val TAG = TimeUtil::class.java.simpleName

    /**
     * 重置全部闹钟，重置时间
     */
    fun resetAllAlarmWithCode(isOpening: Boolean? = null) {
        App.alarmList.forEach {
            val mills = trans2Mills(it.hourOfDay, it.minute)
            //下一次的响铃时间
            it.timeInMills = mills
            //重置code
            it.requestCode = App.alarmList.indexOf(it)
            if (isOpening != null) {
                it.isOpening = isOpening
            }
        }
    }

    /**
     * 重置当前闹钟，重置时间
     */
    fun resetAlarm(alarm: Alarm, isOpening: Boolean? = null) {
        val mills = trans2Mills(alarm.hourOfDay, alarm.minute)
        //下一次的响铃时间
        alarm.timeInMills = mills
        if (isOpening != null) {
            alarm.isOpening = isOpening
        }
    }

    /**
     * 这个时间是否设置过了
     */
    fun isSet(hourOfDay: Int, minute: Int): Boolean {
        App.alarmList.forEach {
            if (it.hourOfDay == hourOfDay && it.minute == minute) {
                return true
            }
        }
        return false
    }

    /**
     * 转换时间为mills
     */
    fun trans2Mills(hourOfDay: Int, minute: Int): Long {
        val current = Calendar.getInstance()
        current.set(Calendar.SECOND, 0)

        val choose = Calendar.getInstance()
        choose.set(Calendar.HOUR_OF_DAY, hourOfDay)
        choose.set(Calendar.MINUTE, minute)
        choose.set(Calendar.SECOND, 0)

        if (choose.timeInMillis < current.timeInMillis) {
            choose.add(Calendar.DAY_OF_YEAR, 1)
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val chooseDate = choose.time
        val chooseFormat = sdf.format(chooseDate)

        Log.i(TAG, chooseFormat)

        /*val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val currentData = current.time
        val currentFormat = sdf.format(currentData)

        Log.i(TAG, currentFormat)

        val chooseDate = choose.time
        val chooseFormat = sdf.format(chooseDate)

        Log.i(TAG, chooseFormat)*/

        return choose.timeInMillis
    }

}