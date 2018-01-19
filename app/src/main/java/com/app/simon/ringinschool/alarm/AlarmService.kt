package com.app.simon.ringinschool.alarm

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.app.simon.ringinschool.App
import com.app.simon.ringinschool.R
import com.app.simon.ringinschool.utils.DefaultUtil
import com.app.simon.ringinschool.utils.MediaPlayerUtil
import com.app.simon.ringinschool.utils.TimeUtil
import java.util.*


/**
 * desc: 闹钟 service
 * date: 2017/12/29
 *
 * @author xw
 */
class AlarmService : Service() {

    private var timer: Timer? = null

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")

        //        createNotification()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")

        createNotification()

        Timer().schedule(object : TimerTask() {
            override fun run() {
                //polling
                AlarmManagerHelper.startPolling(this@AlarmService)
            }
        }, 1000 * 60) //1分钟之后再更新，避免当前分钟内，重复响铃

        MediaPlayerUtil.prepare2Ring(this)

        //        cancel()
        //        init()

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
        return START_STICKY
        //        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.i(TAG, "onDestroy")
    }

    private fun init() {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                //重置闹钟
                TimeUtil.resetAllAlarmWithCode()
                DefaultUtil.saveAlarm()

                Log.i(TAG, "schedule\n" + App.alarmList.toString())
                //检查
                MediaPlayerUtil.prepare2Ring(this@AlarmService)
            }
        }, 0, 1000 * 60)
    }

    private fun cancel() {
        timer?.cancel()
        timer = null
    }

    /**
     * Notification
     */
    private fun createNotification() {
        //使用兼容版本
        val builder = NotificationCompat.Builder(this, "com.app.simon.ringinschool")
        //设置状态栏的通知图标
        builder.setSmallIcon(R.mipmap.ic_launcher)
        //设置通知栏横条的图标
        //        builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.screenflash_logo))
        //禁止用户点击删除按钮删除
        builder.setAutoCancel(false)
        //禁止滑动删除
        builder.setOngoing(true)
        //右上角的时间显示
        builder.setShowWhen(true)
        //设置通知栏的标题内容
        builder.setContentTitle("关了我，闹钟就不响了")
        //创建通知
        val notification = builder.build()
        //设置为前台服务
        startForeground(NOTIFICATION_DOWNLOAD_PROGRESS_ID, notification)
    }


    companion object {
        private val TAG = AlarmService::class.java.simpleName

        /**
         * id不可设置为0,否则不能设置为前台service
         */
        private val NOTIFICATION_DOWNLOAD_PROGRESS_ID = 0x0001

    }
}