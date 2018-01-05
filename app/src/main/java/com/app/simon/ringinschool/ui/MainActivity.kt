package com.app.simon.ringinschool.ui

import android.app.TimePickerDialog
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.util.Log
import com.app.simon.ringinschool.App
import com.app.simon.ringinschool.R
import com.app.simon.ringinschool.alarm.AlarmManagerHelper
import com.app.simon.ringinschool.alarm.adapter.AlarmAdapter
import com.app.simon.ringinschool.utils.TimeUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.util.*

class MainActivity : AppCompatActivity() {

    var adapter: AlarmAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        adapter = AlarmAdapter(this, App.alarmList)
        recycler_view.adapter = adapter

        adapter?.setOnItemChildClickListener { adapter, view, position ->

            when (view.id) {
                R.id.switch_compat -> {
                    val switchCompat = view as SwitchCompat
                    val alarm = App.alarmList[position]
                    //修改是否响铃的状态
                    alarm.isOpening = switchCompat.isChecked
                    //添加或取消响铃
                    if (switchCompat.isChecked) {
                        AlarmManagerHelper.addAlarm(this@MainActivity, alarm)
                    } else {
                        AlarmManagerHelper.cancelAlarm(this@MainActivity, alarm)
                    }
                    //notify
                    adapter?.notifyItemChanged(position)
                    Log.i(TAG, "alarm: $alarm")
                }
                else -> {
                }
            }
        }


        btn_start_alarm.onClick {
            val calendar = Calendar.getInstance()
            //当前时间上加一分钟
            calendar.add(Calendar.MINUTE, 1)

            TimePickerDialog(this@MainActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                //                toast("$hourOfDay+$minute")
                if (TimeUtil.isSet(hourOfDay, minute)) {
                    toast("已经设置过该时间的闹钟")
                    return@OnTimeSetListener
                }
                AlarmManagerHelper.addAlarm(this@MainActivity, hourOfDay, minute)
                adapter?.notifyItemInserted(App.alarmList.size - 1)
                //                adapter?.addData(App.alarmList[App.alarmList.size - 1])
                //                refreshViews()
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                    .show()
        }

        btn_search_music.onClick {
            var cursor: Cursor? = null
            try {
                cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER)
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                cursor?.close()
            }

        }
    }

    private fun refreshViews() {

    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
