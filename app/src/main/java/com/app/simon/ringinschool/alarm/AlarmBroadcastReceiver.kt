package com.app.simon.ringinschool.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * desc: 广播接收系统通知
 * date: 2017/12/28
 *
 * @author xw
 */
class AlarmBroadcastReceiver : BroadcastReceiver() {
    private val TAG = AlarmBroadcastReceiver::class.java.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.run {
            Log.i(TAG, "AlarmBroadcastReceiver onReceive")
            //            toast("你设置的闹铃时间到了")
            val alarmIntent = Intent(context, AlarmService::class.java)
            //            val alarmIndex = intent?.getIntExtra(AlarmManagerHelper.EXTRA_ALARM_INDEX, -1)
            //            alarmIntent.putExtra(AlarmManagerHelper.EXTRA_ALARM_INDEX, alarmIndex)
            startService(alarmIntent)

            //            AlarmActivity.launch(context)
        }
    }
}