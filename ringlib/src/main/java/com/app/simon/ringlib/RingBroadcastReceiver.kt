package com.app.simon.ringlib

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * desc: 广播接收系统通知
 * date: 2017/12/28
 *
 * @author xw
 */
class RingBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.getStringExtra()
    }
}