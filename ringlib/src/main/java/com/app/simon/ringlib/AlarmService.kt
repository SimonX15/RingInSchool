package com.app.simon.ringlib

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 * desc: 闹钟 service
 * date: 2017/12/29
 *
 * @author xw
 */
class AlarmService() : Service() {
    private val TAG = AlarmService::class.java.simpleName

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        AlarmHelper.setAlarmTime(this, 1000)
        return super.onStartCommand(intent, flags, startId)
    }

}