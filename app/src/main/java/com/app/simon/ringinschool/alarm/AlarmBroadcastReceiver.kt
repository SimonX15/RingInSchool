package com.app.simon.ringinschool.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

/**
 * desc: 广播接收系统通知
 * date: 2017/12/28
 *
 * @author xw
 */
class AlarmBroadcastReceiver : BroadcastReceiver(), AnkoLogger {
    override fun onReceive(context: Context?, intent: Intent?) {
        info("AlarmBroadcastReceiver onReceive")

        context?.run {
            toast("你设置的闹铃时间到了")
            startService(Intent(context, AlarmService::class.java))
        }
    }
}