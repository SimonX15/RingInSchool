package com.app.simon.ringinschool.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.app.simon.ringinschool.R
import com.app.simon.ringinschool.ring.AlarmHelper
import com.app.simon.ringinschool.utils.TimeUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                AlarmHelper.addAlarm(this@MainActivity, hourOfDay, minute)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                    .show()
        }
    }
}
