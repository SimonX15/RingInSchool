package com.app.simon.ringinschool

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.app.simon.ringlib.AlarmBroadcastReceiver
import com.app.simon.ringlib.Test
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        intent.putExtra("intervalMillis", 1000)

        start_alarm.setOnClickListener {

            Test.setAlarmTime(this, 5000, intent)
        }

    }


}
