package com.app.simon.ringinschool.alarm

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.app.simon.ringinschool.utils.MediaPlayerUtil

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

        Log.i(TAG, "onStartCommand")
        AlarmManagerHelper.startPolling(this@AlarmService)

        /*Timer().schedule(object : TimerTask() {
            override fun run() {
                //polling
                AlarmManagerHelper.startPolling(this@AlarmService)
            }
        }, 1000 * 60)*/ //1分钟之后再更新，避免当前分钟内，重复响铃

        MediaPlayerUtil.prepare2Ring(this)

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