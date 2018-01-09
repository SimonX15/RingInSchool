package com.app.simon.ringinschool.alarm

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.app.simon.ringinschool.App
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
        val alarmIndex = intent?.getIntExtra(AlarmManagerHelper.EXTRA_ALARM_INDEX, -1)
        Log.i(TAG, "alarmIndex=$alarmIndex")
        Log.i(TAG, App.alarmList.toString())
        alarmIndex?.run {
            if (alarmIndex != -1 && alarmIndex < App.alarmList.size) {
                val alarm = App.alarmList[alarmIndex]
                AlarmManagerHelper.updateAlarm(this@AlarmService, alarmIndex, alarm.hourOfDay, alarm.minute)
                //闹钟开始
                MediaPlayerUtil.play(this@AlarmService)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        private val TAG = AlarmService::class.java.simpleName
    }
}