package com.app.simon.ringinschool.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.app.simon.ringinschool.App
import com.app.simon.ringinschool.alarm.models.Alarm
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

    val EXTRA_ALARM_INDEX = "EXTRA_ALARM_INDEX"

    /**
     * 添加闹钟
     */
    fun addAlarm(context: Context, hourOfDay: Int, minute: Int) {
        val mills = TimeUtil.trans2Mills(hourOfDay, minute)
        val alarm = Alarm(mills, hourOfDay, minute, App.alarmList.size, true)
        App.alarmList.add(alarm)
        addAlarm(context, alarm)
    }

    /** 添加闹钟 */
    fun addAlarm(context: Context, alarm: Alarm) {
        Log.i(TAG, "addAlarm：$alarm")
        //如果不是开启的，则不需设置
        if (!alarm.isOpening) {
            return
        }
        Log.i(TAG, App.alarmList.toString())
        if (alarmManager == null) {
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        intent.putExtra(EXTRA_ALARM_INDEX, App.alarmList.indexOf(alarm))
        val pendingIntent = PendingIntent.getBroadcast(context, alarm.requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        //固定时间通知
        alarmManager?.setWindow(AlarmManager.RTC_WAKEUP, alarm.timeInMills, 1000, pendingIntent)

        //            val calendar = Calendar.getInstance()
        //            //当前时间上加一分钟
        //            calendar.add(Calendar.MINUTE, 1)
        //            calendar.set(Calendar.SECOND, 0)
        //相对时间通知
        //        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + seconds * 1000, pendingIntent)
    }


    /** 重新开启闹钟 */
    fun addAllAlarm(context: Context) {
        //取消所有闹钟
        cancelAllAlarm(context)
        //重置
        TimeUtil.resetAllAlarmWithCode()
        //开启
        App.alarmList.forEach {
            addAlarm(context, it)
        }
    }

    /** 删除闹钟 */
    fun deleteAlarm(context: Context, alarm: Alarm) {
        //取消当前闹钟
        cancelAlarm(context, alarm.requestCode)
        //删除当前闹钟
        App.alarmList.remove(alarm)

        //取消所有闹钟
        App.alarmList.forEach {
            //取消闹钟
            cancelAlarm(context, it.requestCode)
            //重置code
            it.requestCode = App.alarmList.indexOf(it)
            //重启闹钟
            addAlarm(context, alarm)
        }
    }

    /** 删除所有闹钟 */
    fun deleteAllAlarm(context: Context) {
        //取消所有闹钟
        cancelAllAlarm(context)
        //删除所有
        App.alarmList.clear()
    }


    /** 取消所有闹钟 */
    private fun cancelAllAlarm(context: Context) {
        //取消所有闹钟
        App.alarmList.forEach {
            cancelAlarm(context, it)
        }
    }

    /** 取消当前闹钟 */
    private fun cancelAlarm(context: Context, alarm: Alarm) {
        cancelAlarm(context, alarm.requestCode)
    }


    /** 取消闹钟 */
    private fun cancelAlarm(context: Context, requestCode: Int) {
        if (alarmManager == null) {
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
        alarmManager?.let {
            val intent = Intent(context, AlarmBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            //取消正在执行的服务
            alarmManager?.cancel(pendingIntent)
        }
    }

}
