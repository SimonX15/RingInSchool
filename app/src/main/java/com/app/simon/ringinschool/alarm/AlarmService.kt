package com.app.simon.ringinschool.alarm

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.app.simon.ringinschool.App
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
        val alarmIndex = intent?.getIntExtra(AlarmHelper.EXTRA_ALARM_INDEX, -1)
        Log.i(TAG, "onStartCommand：$alarmIndex")
        if (alarmIndex != -1) {
            val alarm = App.Companion.alarmList[alarmIndex!!]
            TimeUtil.setAlarm2NextDay(alarm)
            AlarmHelper.addAlarm(this@AlarmService, alarm)
            Log.i(TAG, App.alarmList.toString())
        }
        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        private val TAG = AlarmService::class.java.simpleName
    }
}