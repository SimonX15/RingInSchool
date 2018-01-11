package com.app.simon.ringinschool.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.app.simon.ringinschool.App
import com.app.simon.ringinschool.alarm.models.Alarm
import com.app.simon.ringinschool.utils.TimeUtil

/**
 * desc: 闹钟管理类
 * date: 2017/12/28
 *
 * @author xw
 */
object AlarmManagerHelper {
    private val TAG = AlarmManagerHelper::class.java.simpleName

    /** 闹钟管理 */
    private var alarmManager: AlarmManager? = null

    /** index */
    val EXTRA_ALARM_INDEX = "EXTRA_ALARM_INDEX"


    /**
     * 添加闹钟
     *
     * 添加/更新/删除操作——>取消全部闹钟——>更新list——>添加全部闹钟
     *
     */
    fun addAlarm(context: Context, hourOfDay: Int, minute: Int, onCompletedListener: OnCompletedListener? = null) {
        //        Log.i(TAG, "addAlarm start: ${App.alarmList}")
        //先取消闹钟
        cancelAllAlarm(context)
        //new
        val currentAlarm = Alarm(hourOfDay, minute)
        //插入的位置
        var toPosition = App.alarmList.size
        loop@ for (index in 0 until App.alarmList.size) {
            val alarm = App.alarmList[index]
            //如果index的标准时间大于当前的标准时间，则加入
            if (alarm.standardTime() > currentAlarm.standardTime()) {
                toPosition = index
                break@loop
            }
        }
        //插入数据
        App.alarmList.add(toPosition, currentAlarm)
        //返回index，作为更新使用
        onCompletedListener?.addAtPosition(toPosition)
        //重置，主要是更新时间和闹钟的code
        TimeUtil.resetAllAlarmWithCode()
        //开启所有闹钟
        startAllAlarm(context)
        //        Log.i(TAG, "addAlarm end: ${App.alarmList}")
    }

    /**
     * 更新闹钟
     */
    fun updateAlarm(context: Context, fromPosition: Int, hourOfDay: Int, minute: Int, alarmType: Int, onCompletedListener: OnCompletedListener? = null) {
        //        Log.i(TAG, "updateAlarm start: ${App.alarmList}")
        //先取消闹钟
        cancelAllAlarm(context)
        //删除
        App.alarmList.removeAt(fromPosition)
        //new
        val currentAlarm = Alarm(hourOfDay, minute, alarmType = alarmType)
        //插入的位置
        var toPosition = App.alarmList.size
        loop@ for (index in 0 until App.alarmList.size) {
            val alarm = App.alarmList[index]
            //如果index的标准时间大于当前的标准时间，则加入
            if (alarm.standardTime() > currentAlarm.standardTime()) {
                toPosition = index
                break@loop
            }
        }
        //插入数据
        App.alarmList.add(toPosition, currentAlarm)
        //返回index，作为更新使用
        onCompletedListener?.updateAtPosition(fromPosition, toPosition)
        //重置，主要是更新时间和闹钟的code
        TimeUtil.resetAllAlarmWithCode()
        //开启所有闹钟
        startAllAlarm(context)
        //        Log.i(TAG, "updateAlarm end: ${App.alarmList}")
    }

    /**
     * 删除闹钟
     */
    fun deleteAlarm(context: Context, fromPosition: Int, onCompletedListener: OnCompletedListener? = null) {
        //        Log.i(TAG, "deleteAlarm start: ${App.alarmList}")
        //先取消闹钟
        cancelAlarm(context, App.alarmList[fromPosition])
        //删除
        App.alarmList.removeAt(fromPosition)
        //返回index，作为更新使用
        onCompletedListener?.deleteAtPosition(fromPosition)
        //        Log.i(TAG, "deleteAlarm end: ${App.alarmList}")
    }

    /** 开启闹钟 */
    fun startAlarm(context: Context, alarm: Alarm) {
        //添加闹钟的时候重新设置mills
        TimeUtil.refreshAlarmTime(alarm)

        //        Log.i(TAG, "startAlarm：$alarm")
        //如果不是开启的，则不需设置
        if (!alarm.isOpening) {
            return
        }
        //        Log.i(TAG, App.alarmList.toString())
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


    /** 开启闹钟 */
    fun startAllAlarm(context: Context) {
        App.alarmList.forEach {
            startAlarm(context, it)
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
    fun cancelAllAlarm(context: Context) {
        //取消所有闹钟
        App.alarmList.forEach {
            cancelAlarm(context, it)
        }
    }

    /** 取消当前闹钟 */
    fun cancelAlarm(context: Context, alarm: Alarm) {
        cancelAlarmWithCode(context, alarm.requestCode)
    }


    /** 取消闹钟 */
    private fun cancelAlarmWithCode(context: Context, requestCode: Int) {
        if (alarmManager == null) {
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
        alarmManager?.apply {
            val intent = Intent(context, AlarmBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            //取消正在执行的服务
            cancel(pendingIntent)
        }
    }

}
