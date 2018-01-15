package com.app.simon.ringinschool

import android.app.Application
import android.media.MediaPlayer
import android.preference.PreferenceManager
import android.util.Log
import com.app.simon.ringinschool.alarm.models.Alarm
import com.app.simon.ringinschool.ring.models.Ring
import com.app.simon.ringinschool.utils.SpUtil
import com.f2prateek.rx.preferences2.RxSharedPreferences

/**
 * desc: App
 * date: 2018/1/3
 *
 * @author xw
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Application onCreate")
        initSp()
        initRealm()
        //        initDataFromDB()
    }

    private fun initSp() {
        SpUtil.rxSharedPreferences = RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(this))
    }

    private fun initRealm() {
        //        Realm.init(this)
        //Stetho初始化
        //        Stetho.initialize(
        //                Stetho.newInitializerBuilder(this)
        //                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
        //                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
        //                        .build()
        //        )
    }

    @Deprecated("不用DB")
    private fun initDataFromDB() {
        //从db取数据
        /*AlarmDBHelper.queryAlarmList(object : OnDBCompleteListener {
            override fun onResult(results: List<Any>?) {
                results?.forEach {
                    alarmList.add(it as Alarm)
                }
                if (alarmList.isEmpty()) {
                    //默认
                    DefaultUtil.setAlarmDefault(this@App)
                }
            }

            override fun onSuccess() {
                Log.i(TAG, "queryAlarmList onSuccess: ")
            }

            override fun onError(throwable: Throwable) {
                toast("读取闹钟列表失败")
                Log.e(TAG, "queryAlarmList $throwable")
                //默认
                DefaultUtil.setAlarmDefault(this@App)
            }
        })
        //从db取数据
        AlarmDBHelper.queryRing(object : OnDBCompleteListener {
            override fun onResult(results: List<Any>?) {
                results?.apply {
                    val ringResult = results[0] as Ring
                    ring.startMusic = ringResult.startMusic
                    ring.endMusic = ringResult.endMusic
                    ring.graceMusic = ringResult.graceMusic
                }
            }

            override fun onSuccess() {
                Log.i(TAG, "queryRing onSuccess: ")
            }

            override fun onError(throwable: Throwable) {
                toast("读取闹钟列表失败")
                Log.e(TAG, "queryRing $throwable")
                DefaultUtil.setRingDefault()
            }
        })*/
    }

    companion object {
        private val TAG = App::class.java.simpleName
        /** 闹钟列表 */
        val alarmList: ArrayList<Alarm> = ArrayList()
        /** 全局的播放器 */
        var mediaPlayer: MediaPlayer? = null
        /** 响铃铃声 */
        val ring = Ring()
    }
}