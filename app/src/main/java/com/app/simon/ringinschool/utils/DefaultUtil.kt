package com.app.simon.ringinschool.utils

import android.content.Context
import android.util.Log
import com.app.simon.ringinschool.App
import com.app.simon.ringinschool.alarm.AlarmManagerHelper
import com.app.simon.ringinschool.alarm.models.Alarm
import com.app.simon.ringinschool.alarm.models.AlarmType.TYPE_END
import com.app.simon.ringinschool.alarm.models.AlarmType.TYPE_GRACE
import com.app.simon.ringinschool.alarm.models.AlarmType.TYPE_START
import com.app.simon.ringinschool.ring.models.Music

/**
 * desc: 默认工具
 * date: 2018/1/10
 *
 * @author xw
 */
object DefaultUtil {
    private val TAG = DefaultUtil::class.java.simpleName

    /** 设置默认闹钟 */
    fun setAlarmDefault(context: Context) {
        Log.i(TAG, "setDefault start: ${App.alarmList}")
        //先取消闹钟
        AlarmManagerHelper.cancelAllAlarm(context)
        //清空数据
        App.alarmList.clear()
        //插入数据
        App.alarmList.add(Alarm(8, 30, alarmType = TYPE_START))
        App.alarmList.add(Alarm(8, 40, alarmType = TYPE_START))
        App.alarmList.add(Alarm(9, 20, alarmType = TYPE_END))
        App.alarmList.add(Alarm(9, 30, alarmType = TYPE_START))
        App.alarmList.add(Alarm(10, 10, alarmType = TYPE_END))
        App.alarmList.add(Alarm(10, 30, alarmType = TYPE_START))
        App.alarmList.add(Alarm(11, 10, alarmType = TYPE_END))
        App.alarmList.add(Alarm(11, 20, alarmType = TYPE_START))
        App.alarmList.add(Alarm(12, 0, alarmType = TYPE_END))
        App.alarmList.add(Alarm(13, 0, alarmType = TYPE_START))
        App.alarmList.add(Alarm(13, 20, alarmType = TYPE_END))
        App.alarmList.add(Alarm(13, 30, alarmType = TYPE_START))
        App.alarmList.add(Alarm(14, 10, alarmType = TYPE_END))
        App.alarmList.add(Alarm(14, 20, alarmType = TYPE_START))
        App.alarmList.add(Alarm(15, 0, alarmType = TYPE_END))
        App.alarmList.add(Alarm(15, 10, alarmType = TYPE_START))
        App.alarmList.add(Alarm(15, 50, alarmType = TYPE_END))
        App.alarmList.add(Alarm(16, 0, alarmType = TYPE_GRACE))
        //重置，主要是更新时间和闹钟的code
        TimeUtil.resetAllAlarmWithCode()
        //开启所有闹钟
        AlarmManagerHelper.startAllAlarm(context)
        Log.i(TAG, "setDefault end: ${App.alarmList}")
    }

    /** 设置默认的铃声 */
    fun setRingDefault() {
        App.ring.startMusic = Music("assets/default_start.wav", "默认上课铃声")
        App.ring.endMusic = Music("assets/default_end.wav", "默认下课铃声")
        App.ring.graceMusic = Music("assets/amazing_grace.mp3", "默认赞美之歌")
    }
}