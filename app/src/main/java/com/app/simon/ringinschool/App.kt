package com.app.simon.ringinschool

import android.app.Application
import com.app.simon.ringinschool.alarm.models.Alarm

/**
 * desc: App
 * date: 2018/1/3
 *
 * @author xw
 */
class App : Application() {

    companion object {
        /** 闹钟列表 */
        val alarmList: ArrayList<Alarm> = ArrayList()
    }
}