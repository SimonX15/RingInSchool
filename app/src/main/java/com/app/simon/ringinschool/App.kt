package com.app.simon.ringinschool

import android.app.Application
import android.media.MediaPlayer
import com.app.simon.ringinschool.alarm.models.Alarm
import com.app.simon.ringinschool.ring.models.Ring
import com.app.simon.ringinschool.utils.DefaultUtil
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
        initData()
    }

    private fun initRealm() {
        Realm.init(this)
    }

    private fun initData() {
        //从db取数据
        //        alarmList
        //        ring
        //默认
        DefaultUtil.setAlarmDefault(this)
        DefaultUtil.setRingDefault()
    }

    companion object {
        /** 闹钟列表 */
        val alarmList: ArrayList<Alarm> = ArrayList()
        /** 全局的播放器 */
        var mediaPlayer: MediaPlayer? = null
        /** 响铃铃声 */
        val ring = Ring()
    }
}