package com.app.simon.ringinschool.alarm

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.app.simon.ringinschool.utils.DefaultUtil
import com.app.simon.ringinschool.utils.MediaPlayerUtil
import com.app.simon.ringinschool.utils.TimeUtil

/**
 * desc: 闹钟 service
 * date: 2017/12/29
 *
 * @author xw
 */
class AlarmService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //        val alarmIndex = intent?.getIntExtra(AlarmManagerHelper.EXTRA_ALARM_INDEX, -1)
        //        Log.i(TAG, "alarmIndex=$alarmIndex")
        //polling
        AlarmManagerHelper.startPolling(this)
        val alarmList = DefaultUtil.getAlarmFromSp()

        Log.i(TAG, "onStartCommand\n" + alarmList.toString())

        alarmList?.forEach {
            if (TimeUtil.isCurrentTime(it.hourOfDay, it.minute)) {
                //闹钟开始
                MediaPlayerUtil.play(this@AlarmService, it.alarmType)
                //添加闹钟的时候重新设置mills
                TimeUtil.resetAllAlarmWithCode()
                return@forEach
            }
        }

        /*alarmIndex?.run {
            if (alarmIndex != -1 && alarmIndex < App.alarmList.size) {
                val alarm = App.alarmList[alarmIndex]
                //闹钟开始
                MediaPlayerUtil.play(this@AlarmService, alarm.alarmType)
                //延迟设置，避免重复响铃
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        AlarmManagerHelper.updateAlarm(this@AlarmService, alarmIndex, alarm.hourOfDay, alarm.minute, alarm.alarmType)
                    }
                }, 1000 * 90) //1分半钟之后再更新，避免当前分钟内，重复响铃

            }
        }*/
        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        private val TAG = AlarmService::class.java.simpleName
    }
}