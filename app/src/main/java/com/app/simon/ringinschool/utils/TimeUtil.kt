package com.app.simon.ringinschool.utils

import com.app.simon.ringinschool.App
import com.app.simon.ringinschool.alarm.models.Alarm
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
    fun resetAllAlarmWithCode() {
        App.alarmList.forEach {
            val mills = getNextMills(it.hourOfDay, it.minute)
            //下一次的响铃时间
            it.timeInMills = mills
            //重置code
            it.requestCode = App.alarmList.indexOf(it)
        }
    }

    /**
     * 重置当前闹钟，重置时间
     */
    fun refreshAlarmTime(alarm: Alarm, isOpening: Boolean? = null): Alarm {
        val mills = getNextMills(alarm.hourOfDay, alarm.minute)
        //下一次的响铃时间
        alarm.timeInMills = mills
        if (isOpening != null) {
            alarm.isOpening = isOpening
        }
        return alarm
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
     * 获取最近的时间
     */
    private fun getNextMills(hourOfDay: Int, minute: Int): Long {
        val current = Calendar.getInstance()
        current.set(Calendar.SECOND, 0)

        val choose = Calendar.getInstance()
        choose.set(Calendar.HOUR_OF_DAY, hourOfDay)
        choose.set(Calendar.MINUTE, minute)
        choose.set(Calendar.SECOND, 0)

        //如果选择的时间<=当前的时间，闹钟要设置在一天之后
        //小于小时
        if (hourOfDay < current.get(Calendar.HOUR_OF_DAY)) {
            choose.add(Calendar.DAY_OF_YEAR, 1)
        }
        //小时相同，小于或等于分钟
        if (hourOfDay == current.get(Calendar.HOUR_OF_DAY) && minute <= current.get(Calendar.MINUTE)) {
            choose.add(Calendar.DAY_OF_YEAR, 1)
        }

        //-1分钟，提前一分钟响
        choose.add(Calendar.MINUTE, -1)

        /*val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val chooseDate = choose.time
        val chooseFormat = sdf.format(chooseDate)

        Log.i(TAG, chooseFormat)*/

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