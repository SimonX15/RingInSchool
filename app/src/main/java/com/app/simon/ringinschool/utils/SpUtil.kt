package com.app.simon.ringinschool.utils

import android.support.annotation.CheckResult
import com.f2prateek.rx.preferences2.RxSharedPreferences

/**
 * desc: 缓存
 * date: 2017/12/5
 *
 * @author xw
 */
object SpUtil {
    /** 闹钟缓存 */
    private val SP_ALARM_LIST = "SP_ALARM_LIST"
    /** 音乐缓存 */
    private val SP_RING = "SP_RING"

    lateinit var rxSharedPreferences: RxSharedPreferences

    //    rxSharedPreferences = RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(context))

    val spAlarmList by lazy { getString(SP_ALARM_LIST) }

    val spRing by lazy { getString(SP_RING) }

    /** Create a string preference for `key`. Default is `""`.  */
    @CheckResult
    private fun getString(key: String) = rxSharedPreferences.getString(key)
}