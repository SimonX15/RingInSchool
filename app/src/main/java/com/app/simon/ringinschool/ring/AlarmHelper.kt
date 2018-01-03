package com.app.simon.ringinschool.ring

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.app.simon.ringinschool.App
import com.app.simon.ringinschool.ui.models.Alarm
import com.app.simon.ringinschool.utils.TimeUtil

/**
 * desc:
 * date: 2017/12/28
 *
 * @author xw
 */
object AlarmHelper {
    private val TAG = AlarmHelper::class.java.simpleName

    private var alarmManager: AlarmManager? = null

    /**
     * 通过时间设置闹钟
     *
     */
    fun addAlarm(context: Context, hourOfDay: Int, minute: Int) {
        val mills = TimeUtil.trans2Mills(hourOfDay, minute)
        val alarm = Alarm(mills, hourOfDay, minute, App.alarmList.size, true)
        App.alarmList.add(alarm)
        setAlarm(context, alarm)
    }

    /** 设置闹钟 */
    private fun setAlarm(context: Context, alarm: Alarm) {
        if (alarmManager == null) {
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, alarm.requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        Log.i(TAG, ": ")

        //固定时间通知
        alarmManager?.setWindow(AlarmManager.RTC_WAKEUP, alarm.timeInMills, 1000, pendingIntent)

        //            val calendar = Calendar.getInstance()
        //            //当前时间上加一分钟
        //            calendar.add(Calendar.MINUTE, 1)
        //            calendar.set(Calendar.SECOND, 0)
        //相对时间通知
        //        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + seconds * 1000, pendingIntent)

    }

    /** 取消闹钟 */
    fun cancelAlarm(context: Context, requestCode: Int) {
        alarmManager?.let {
            val intent = Intent(context, AlarmBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            //取消正在执行的服务
            alarmManager?.cancel(pendingIntent)
        }

    }

}
