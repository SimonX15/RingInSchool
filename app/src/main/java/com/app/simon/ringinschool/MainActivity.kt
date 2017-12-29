package com.app.simon.ringinschool

import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.app.simon.ringlib.AlarmHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
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
                val calculateMills = calculateMills(hourOfDay, minute)
                Log.i(TAG, calculateMills.toString())
                AlarmHelper.setAlarmTime(this@MainActivity, calculateMills)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                    .show()
        }
    }

    /** 选择的时间与当前时间差 */
    private fun calculateMills(hourOfDay: Int, minute: Int): Long {
        val current = Calendar.getInstance()
        current.set(Calendar.SECOND, 0)

        val choose = Calendar.getInstance()
        choose.set(Calendar.HOUR_OF_DAY, hourOfDay)
        choose.set(Calendar.MINUTE, minute)
        choose.set(Calendar.SECOND, 0)

        /*val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val currentData = current.time
        val currentFormat = sdf.format(currentData)

        Log.i(TAG, currentFormat)

        val chooseDate = choose.time
        val chooseFormat = sdf.format(chooseDate)

        Log.i(TAG, chooseFormat)*/

        return choose.timeInMillis - current.timeInMillis
    }
}
