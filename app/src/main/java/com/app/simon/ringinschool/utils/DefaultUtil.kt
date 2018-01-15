package com.app.simon.ringinschool.utils

import android.content.Context
import com.app.simon.ringinschool.App
import com.app.simon.ringinschool.alarm.AlarmManagerHelper
import com.app.simon.ringinschool.alarm.models.Alarm
import com.app.simon.ringinschool.alarm.models.AlarmType.TYPE_END
import com.app.simon.ringinschool.alarm.models.AlarmType.TYPE_GRACE
import com.app.simon.ringinschool.alarm.models.AlarmType.TYPE_START
import com.app.simon.ringinschool.ring.models.Music
import com.app.simon.ringinschool.ring.models.Ring
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * desc: 默认工具
 * date: 2018/1/10
 *
 * @author xw
 */
object DefaultUtil {
    private val TAG = DefaultUtil::class.java.simpleName

    /** 初始化闹钟 */
    fun initAlarmFromSp(context: Context) {
        val alarmSpJson = SpUtil.spAlarmList
        //        Log.i(TAG, "alarmSpJson:${alarmSpJson.get()} ")
        val type = object : TypeToken<ArrayList<Alarm>>() {
        }.type
        val alarmListSP = GsonUtil.toObj<ArrayList<Alarm>>(alarmSpJson.get(), type)
        if (alarmListSP == null || alarmListSP.isEmpty()) {
            DefaultUtil.setAlarmDefault(context)
        } else {
            //先取消闹钟
            AlarmManagerHelper.cancelAllAlarm(context)
            //清空数据
            App.alarmList.clear()
            //插入数据
            App.alarmList.addAll(alarmListSP)
            //重置，主要是更新时间和闹钟的code
            TimeUtil.resetAllAlarmWithCode()
            //开启所有闹钟
            AlarmManagerHelper.startAllAlarm(context)
        }

        val ringSpJson = SpUtil.spRing
        //        Log.i(TAG, "ringSpJson:${ringSpJson.get()} ")
        val ringSp = GsonUtil.toObj<Ring>(ringSpJson.get(), Ring::class.java)
        if (ringSp == null) {
            setRingDefault()
        } else {
            App.ring.startMusic = ringSp.startMusic
            App.ring.endMusic = ringSp.endMusic
            App.ring.graceMusic = ringSp.graceMusic
        }

    }

    /** 设置默认闹钟 */
    fun setAlarmDefault(context: Context) {
        //        Log.i(TAG, "setDefault start: ${App.alarmList}")
        //先取消闹钟
        AlarmManagerHelper.cancelAllAlarm(context)
        //清空数据
        App.alarmList.clear()
        //插入数据
        //                getAlarmList().forEach {
        getDebugAlarmList().forEach {
            App.alarmList.add(it)
        }
        //重置，主要是更新时间和闹钟的code
        TimeUtil.resetAllAlarmWithCode()
        //开启所有闹钟
        AlarmManagerHelper.startAllAlarm(context)
        //        Log.i(TAG, "setDefault end: ${App.alarmList}")
    }

    private fun getAlarmList(): ArrayList<Alarm> {
        val list = ArrayList<Alarm>()
        list.add(Alarm(8, 30, alarmType = TYPE_START))
        list.add(Alarm(8, 40, alarmType = TYPE_START))
        list.add(Alarm(9, 20, alarmType = TYPE_END))
        list.add(Alarm(9, 30, alarmType = TYPE_START))
        list.add(Alarm(10, 10, alarmType = TYPE_END))
        list.add(Alarm(10, 30, alarmType = TYPE_START))
        list.add(Alarm(11, 10, alarmType = TYPE_END))
        list.add(Alarm(11, 20, alarmType = TYPE_START))
        list.add(Alarm(12, 0, alarmType = TYPE_END))
        list.add(Alarm(13, 0, alarmType = TYPE_START))
        list.add(Alarm(13, 20, alarmType = TYPE_END))
        list.add(Alarm(13, 30, alarmType = TYPE_START))
        list.add(Alarm(14, 10, alarmType = TYPE_END))
        list.add(Alarm(14, 20, alarmType = TYPE_START))
        list.add(Alarm(15, 0, alarmType = TYPE_END))
        list.add(Alarm(15, 10, alarmType = TYPE_START))
        list.add(Alarm(15, 50, alarmType = TYPE_END))
        list.add(Alarm(16, 0, alarmType = TYPE_GRACE))
        return list
    }

    private fun getDebugAlarmList(): ArrayList<Alarm> {
        val list = ArrayList<Alarm>()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 2)
        list.add(Alarm(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), alarmType = TYPE_GRACE))
        calendar.add(Calendar.MINUTE, 2)
        list.add(Alarm(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), alarmType = TYPE_START))
        calendar.add(Calendar.MINUTE, 2)
        list.add(Alarm(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), alarmType = TYPE_END))
        calendar.add(Calendar.MINUTE, 2)
        list.add(Alarm(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), alarmType = TYPE_GRACE))
        calendar.add(Calendar.MINUTE, 2)
        list.add(Alarm(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), alarmType = TYPE_START))
        calendar.add(Calendar.MINUTE, 2)
        list.add(Alarm(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), alarmType = TYPE_END))
        return list
    }

    /** 设置默认的铃声 */
    fun setRingDefault() {
        App.ring.startMusic = Music("assets/default_start.wav", "默认上课铃声")
        App.ring.endMusic = Music("assets/default_end.mp3", "默认下课铃声")
        App.ring.graceMusic = Music("assets/amazing_grace.mp3", "默认赞美之歌")
    }
}