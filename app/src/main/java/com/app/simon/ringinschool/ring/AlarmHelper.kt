package com.app.simon.ringinschool.ring

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

/**
 * desc:
 * date: 2017/12/28
 *
 * @author xw
 */
object AlarmHelper {
    private val TAG = AlarmHelper::class.java.simpleName

    private var alarmManager: AlarmManager? = null


    /** 设置闹钟 */
    fun setAlarmTime(context: Context, seconds: Long, timeInMills: Long = 0) {
        if (alarmManager == null) {
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        Log.i(TAG, ": ")

        //19以上，不可以重复
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //固定时间通知
            //                        alarmManager?.setWindow(AlarmManager.RTC_WAKEUP, timeInMills, 1000, pendingIntent)

            //            val calendar = Calendar.getInstance()
            //            //当前时间上加一分钟
            //            calendar.add(Calendar.MINUTE, 1)
            //            calendar.set(Calendar.SECOND, 0)
            //相对时间通知
            alarmManager?.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + seconds * 1000, pendingIntent)
        } else {
            alarmManager?.setRepeating(AlarmManager.RTC_WAKEUP, timeInMills, 1000 * 60, pendingIntent)
        }
    }

    /** 取消闹钟 */
    fun cancelAlarm(context: Context, action: String) {
        alarmManager?.let {
            val intent = Intent(context, AlarmBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            //取消正在执行的服务
            alarmManager?.cancel(pendingIntent)
        }

    }

}
