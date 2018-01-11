package com.app.simon.ringinschool

import android.app.Application
import android.media.MediaPlayer
import android.preference.PreferenceManager
import android.util.Log
import com.app.simon.ringinschool.alarm.AlarmManagerHelper
import com.app.simon.ringinschool.alarm.models.Alarm
import com.app.simon.ringinschool.ring.models.Ring
import com.app.simon.ringinschool.utils.DefaultUtil
import com.app.simon.ringinschool.utils.GsonUtil
import com.app.simon.ringinschool.utils.SpUtil
import com.app.simon.ringinschool.utils.TimeUtil
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.gson.reflect.TypeToken

/**
 * desc: App
 * date: 2018/1/3
 *
 * @author xw
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initSp()
        initRealm()
        //        initDataFromDB()
        initDataFromSp()
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

    private fun initDataFromSp() {
        val alarmSpJson = SpUtil.spAlarmList
        Log.i(TAG, "alarmSpJson:${alarmSpJson.get()} ")
        val type = object : TypeToken<ArrayList<Alarm>>() {
        }.type
        val alarmListSP = GsonUtil.toObj<ArrayList<Alarm>>(alarmSpJson.get(), type)
        if (alarmListSP == null || alarmListSP.isEmpty()) {
            DefaultUtil.setAlarmDefault(this@App)
        } else {
            //先取消闹钟
            AlarmManagerHelper.cancelAllAlarm(this)
            //清空数据
            App.alarmList.clear()
            //插入数据
            alarmList.addAll(alarmListSP)
            //重置，主要是更新时间和闹钟的code
            TimeUtil.resetAllAlarmWithCode()
            //开启所有闹钟
            AlarmManagerHelper.startAllAlarm(this)
        }

        val ringSpJson = SpUtil.spRing
        Log.i(TAG, "ringSpJson:${ringSpJson.get()} ")
        val ringSp = GsonUtil.toObj<Ring>(ringSpJson.get(), Ring::class.java)
        if (ringSp == null) {
            DefaultUtil.setRingDefault()
        } else {
            ring.startMusic = ringSp.startMusic
            ring.endMusic = ringSp.endMusic
            ring.graceMusic = ringSp.graceMusic
        }

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