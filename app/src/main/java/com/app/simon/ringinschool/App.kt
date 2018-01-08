package com.app.simon.ringinschool

import android.app.Application
import android.media.MediaPlayer
import com.app.simon.ringinschool.alarm.models.Alarm
import io.realm.Realm

/**
 * desc: App
 * date: 2018/1/3
 *
 * @author xw
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
    }

    companion object {
        /** 闹钟列表 */
        val alarmList: ArrayList<Alarm> = ArrayList()
        /** 全局的播放器 */
        val mediaPlayer = MediaPlayer()
    }
}