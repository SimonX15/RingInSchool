package com.app.simon.ringinschool.alarm.models

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*

/**
 * desc: 闹钟时间
 * date: 2018/1/3
 *
 * @author xw
 */
data class Alarm(
        /** mills */
        var timeInMills: Long = 0,
        /** 小时 */
        var hourOfDay: Int = 0,
        /** 分钟 */
        var minute: Int = 0,
        /** requestCode，用于闹钟设置 */
        var requestCode: Int = 0,
        /** 是否打开，打开就要响铃 */
        var isOpening: Boolean = false
) : Parcelable {
    override fun toString(): String {
        return "Alarm(timeInMills=$timeInMills, hourOfDay=$hourOfDay, minute=$minute, requestCode=$requestCode, isOpening=$isOpening) " + transDate(timeInMills) + "\n"
    }

    private fun transDate(timeInMills: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val chooseDate = Date(timeInMills)
        return sdf.format(chooseDate)
    }

    constructor(source: Parcel) : this(
            source.readLong(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(timeInMills)
        writeInt(hourOfDay)
        writeInt(minute)
        writeInt(requestCode)
        writeInt((if (isOpening) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Alarm> = object : Parcelable.Creator<Alarm> {
            override fun createFromParcel(source: Parcel): Alarm = Alarm(source)
            override fun newArray(size: Int): Array<Alarm?> = arrayOfNulls(size)
        }
    }
}